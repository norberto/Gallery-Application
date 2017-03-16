package edu.norbertzardin.test;

import edu.norbertzardin.entities.ByteData;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestCatalogueService {

    @PersistenceContext(name = "imagePersistence")
    private EntityManager em;

    @Autowired
    private CatalogueService catalogueService;
    @Autowired
    private ImageService imageService;

    private CatalogueEntity entity;
    private String testTitle;
    private CatalogueEntity defaultCat;
    private String defaultTitle = "Non-categorized";

    @Before
    public void setUp() {
        defaultCat = new CatalogueEntity();
        defaultCat.setCreatedDate(new Date());
        defaultCat.setTitle(defaultTitle);
        catalogueService.create(defaultCat);

        testTitle = "test";
        entity = new CatalogueEntity();
        entity.setCreatedDate(new Date());
        entity.setTitle(testTitle);
    }


    @After
    public void tearDown() {
    }

    @Test
    @Rollback
    public void testSave() {
        catalogueService.create(entity);
        Assert.assertTrue(em.contains(entity));
    }

    @Test
    @Rollback
    public void testLoadById() {
        catalogueService.create(entity);
        Assert.assertEquals(catalogueService.load(entity.getId()).getTitle(), testTitle);
    }

    @Test
    @Rollback
    public void testLoadByString() {
        catalogueService.create(entity);
        Assert.assertEquals(catalogueService.load(testTitle).getTitle(), entity.getTitle());
    }

    @Test
    @Rollback
    public void testLoadByStringNonExistingEntity() {
        Assert.assertEquals(catalogueService.load(testTitle), null);
    }

    @Test
    @Rollback
    public void testSaveDuplicate() {
        CatalogueEntity entity2 = new CatalogueEntity();
        entity2.setTitle(testTitle);
        entity2.setCreatedDate(new Date());
        catalogueService.create(entity);
        Assert.assertFalse(catalogueService.create(entity2));
    }

    @Test
    @Rollback
    public void testRemove() {
        catalogueService.create(entity);
        CatalogueEntity loaded = catalogueService.load(entity.getTitle());
        Assert.assertEquals("Catalogue was not persisted.", testTitle, loaded.getTitle());
        catalogueService.remove(loaded);
        Assert.assertEquals("Removed entity still exists in database.", null, catalogueService.load(loaded.getId()));
    }

    @Test
    @Rollback
    public void testCountIncludeDefault() {
        for (int i = 0; i < 10; ++i) {
            CatalogueEntity cat = new CatalogueEntity();
            cat.setTitle(testTitle + i);
            cat.setCreatedDate(new Date());
            catalogueService.create(cat);
        }
        Assert.assertEquals("Calculated page count is not what expected", 4,
                catalogueService.count(3, null, true).intValue());
    }

    @Test
    @Rollback
    public void testCountExcludeDefault() {
        for (int i = 0; i < 3; ++i) {
            CatalogueEntity cat = new CatalogueEntity();
            cat.setTitle(testTitle + i);
            cat.setCreatedDate(new Date());
            catalogueService.create(cat);
        }
        Assert.assertEquals("Calculated page count is not what expected", 2,
                catalogueService.count(2, null, false).intValue());
    }

    @Test
    @Rollback
    public void testImageCount() {
        catalogueService.create(entity);

        ByteData data = new ByteData();
        byte[] d = {1, 0, 1, 0};
        data.setData(d);
        for (int i = 0; i < 10; ++i) {
            ImageEntity img = new ImageEntity();
            img.setName("Img");
            img.setCreatedDate(new Date());
            img.setCatalogue(entity);

            img.setThumbnail(data);
            img.setMediumImage(data);
            img.setDownload(data);
            imageService.create(img);
        }
        Assert.assertEquals("Calculate image page count is not what expected.", 4,
                catalogueService.imageCount(entity, 3).intValue());
    }

    @Test
    @Rollback
    public void testUpdate() {
        catalogueService.create(entity);
        String title = "new title";
        entity.setTitle(title);
        catalogueService.update(entity);
        Assert.assertEquals("Entity did not update.", title, catalogueService.load(entity.getId()).getTitle());
    }

    @Test
    @Rollback
    public void testLoadNoFetch() {
        catalogueService.create(entity);
        ByteData data = new ByteData();
        byte[] d = {1, 0, 1, 0};
        data.setData(d);

        ImageEntity img = new ImageEntity();
        img.setName(testTitle);
        img.setCreatedDate(new Date());
        img.setCatalogue(catalogueService.load(entity.getId()));

        img.setThumbnail(data);
        img.setMediumImage(data);
        img.setDownload(data);
        imageService.create(img);

        CatalogueEntity loaded = catalogueService.load(entity.getId());
        Assert.assertEquals(loaded.getImages(), null);
    }

    @Test
    @Rollback
    public void testLoadByPageIncludeDefault() {
        List<CatalogueEntity> temp_list = new ArrayList<>();
        temp_list.add(populateCatalogues(3));

        List<CatalogueEntity> list = catalogueService.loadByPage(4, 1, null, true);
        Assert.assertEquals("Wrong page content.", list, temp_list);
    }

    @Test
    @Rollback
    public void testLoadByPageExcludeDefault() {
        List<CatalogueEntity> temp_list = new ArrayList<>();
        temp_list.add(populateCatalogues(3));
        List<CatalogueEntity> list = catalogueService.loadByPage(3, 1, null, false);
        Assert.assertEquals("Expected and actual page content doesn't match.", temp_list, list);
    }

    @Test
    @Rollback
    public void testRemoveNonExistentEntity() {
        entity.setId((long) 999);
        Assert.assertFalse(catalogueService.remove(entity));
    }

    @Test
    @Rollback
    public void testLoadByIdNonExistentEntity() {
        Assert.assertEquals("Entity actually found???", null, catalogueService.load((long) 999));
    }

    @Test
    @Rollback
    public void testLoadNoFetchNonExistendEntity() {
        Assert.assertEquals("Entity actually found???", null, catalogueService.loadNoFetch(testTitle));
    }

    @Test
    @Rollback
    public void testCountWithSearch() {
        Assert.assertEquals("Page count is not what expected.", 1, catalogueService.count(1, defaultTitle, true).intValue());
    }

    @Test
    @Rollback
    public void testLoadByPageWithSearchExcludeDefaultCatalogue() {
        List<CatalogueEntity> temp_list = new ArrayList<>();
        List<CatalogueEntity> list = catalogueService.loadByPage(1, 1, defaultTitle, false);
        Assert.assertEquals("Expected and actual page content doesn't match.", temp_list, list);
    }

    @Test
    @Rollback
    public void testLoadByPageWithSearchIncludeDefaultCatalogue() {
        List<CatalogueEntity> temp_list = new ArrayList<>();
        temp_list.add(defaultCat);
        List<CatalogueEntity> list = catalogueService.loadByPage(1, 1, defaultTitle, true);
        Assert.assertEquals("Expected and actual page content doesn't match.", temp_list, list);
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    private CatalogueEntity populateCatalogues(Integer j) {
        CatalogueEntity cat = new CatalogueEntity();
        for (int i = 1; i <= j; ++i) {
            cat = new CatalogueEntity();
            cat.setTitle(testTitle + i);
            cat.setCreatedDate(new Date());
            catalogueService.create(cat);
        }
        return cat;
    }
}
