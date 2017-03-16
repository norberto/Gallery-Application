package edu.norbertzardin.test;

import edu.norbertzardin.entities.ByteData;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.service.CatalogueService;
import edu.norbertzardin.service.ImageService;
import edu.norbertzardin.service.TagService;
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
import java.util.Date;

@Transactional
@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestImageService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CatalogueService catalogueService;

    @Autowired
    private TagService tagService;

    private ImageEntity imageEntity;
    private CatalogueEntity catalogueEntity;
    private TagEntity tagEntity;

    @Before
    public void setUp() {
        imageEntity = new ImageEntity();
        imageEntity.setName("test_img");
        imageEntity.setCreatedDate(new Date());
        byte[] data = {1,1,1,1,1,0,1,1,1,1,1};
        ByteData temp = new ByteData();
        temp.setData(data);
        imageEntity.setThumbnail(temp);
        imageEntity.setMediumImage(temp);
        imageEntity.setDownload(temp);

        tagEntity = new TagEntity();
        tagEntity.setName("testTag");
        tagEntity.setCreatedDate(new Date());

    }

    @Test
    @Rollback
    public void testCreateImage(){
        Assert.assertTrue("Image could not be created.", imageService.create(imageEntity));
    }

    @Test
    @Rollback
    public void testCreateImageViolateNotNull(){
        ImageEntity image = new ImageEntity();
        Assert.assertFalse("Image could be created.", imageService.create(image));
    }


    @Test
    @Rollback
    public void testRemoveImage() {
        imageService.create(imageEntity);
        Assert.assertTrue("Could not delete image.", imageService.remove(imageEntity.getId()));
    }

    @Test
    @Rollback
    public void testRemoveNonExistingImage() {
        Assert.assertFalse("Could not delete image.", imageService.remove((long) 999));
    }

    @Test
    @Rollback
    public void testLoadMediumImage() {
        imageService.create(imageEntity);
        ImageEntity temp = imageService.loadMedium(imageEntity.getId());
        Assert.assertNotNull("Image is null.", temp.getMediumImage());
    }

    @Test
    @Rollback
    public void testLoad() {
        imageService.create(imageEntity);
        entityManager.detach(imageEntity);
        Assert.assertEquals("Loaded object is not the same as local one.",
                imageEntity.getName(), imageService.load(imageEntity.getId()).getName());
    }

    @Test
    @Rollback
    public void testUpdateImage() {
        imageService.create(imageEntity);
        entityManager.detach(imageEntity);
        imageEntity.setName("WOAH.");
        imageService.update(imageEntity);
        Assert.assertEquals("Update failed.", "WOAH.", imageService.load(imageEntity.getId()).getName());

    }
    @Test
    @Rollback
    public void testCountImagesUsingKey() {
        imageService.create(imageEntity);
        entityManager.detach(imageEntity);
        Assert.assertEquals("Wrong image count.", 1, imageService.count(imageEntity.getName(), null).intValue());
    }

    @Test
    @Rollback
    public void testDownload() {
        imageService.create(imageEntity);
        entityManager.detach(imageEntity);
        ByteData data = imageService.download(imageEntity.getId());
        byte[] data2 = imageEntity.getDownload().getData();
        Assert.assertEquals("Data doesn't match.", data2[5], data.getData()[5]);
        Assert.assertEquals("Data doesn't match.", data2[1], data.getData()[1]);
        Assert.assertEquals("Data doesn't match.", data2[0], data.getData()[0]);


    }

    @Test
    @Rollback
    public void testSearchPaging() {
        populateImages(10);
        Assert.assertEquals("Wrong page image count.", 10, imageService.find("test", null, 1, 100).size());
    }



    @Test
    @Rollback
    public void testLoadAllImages() {
        int count = 50;
        populateImages(count);

        Assert.assertEquals("Wrong image count, not as expected :(", count, imageService.loadImages(1, count*2).size());
    }

    public void populateImages(Integer count) {
        for (int i = 0; i < count; i++) {
            ImageEntity im = new ImageEntity();
            im.setName("test" + 1);
            im.setDownload(imageEntity.getDownload());
            im.setCreatedDate(imageEntity.getCreatedDate());
            im.setThumbnail(imageEntity.getThumbnail());
            im.setMediumImage(imageEntity.getMediumImage());
            imageService.create(im);
        }
    }

    @Test
    @Rollback
    public void testLoadCatalogueImages() {
        catalogueEntity = new CatalogueEntity();
        catalogueEntity.setTitle("test catalogue");
        catalogueEntity.setCreatedDate(new Date());
        catalogueService.create(catalogueEntity);
        entityManager.detach(catalogueEntity);
        imageEntity.setCatalogue(catalogueEntity);
        imageService.create(imageEntity);

        Assert.assertEquals("Page content is not what expected.", imageEntity.getName(), imageService.loadCatalogueImages(1,1, catalogueEntity).get(0).getName());
    }
}
