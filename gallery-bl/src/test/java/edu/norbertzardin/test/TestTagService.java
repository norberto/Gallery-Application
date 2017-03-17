package edu.norbertzardin.test;

import edu.norbertzardin.entities.ByteData;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestTagService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TagService tagService;

    @Autowired
    private ImageService imageService;

    private TagEntity tagEntity;

    @Before
    public void setUp() {
        tagEntity = new TagEntity();
        tagEntity.setName("test");
        tagEntity.setCreatedDate(new Date());
    }

    @Test
    @Rollback
    public void testCreate() {
        Assert.assertTrue("Tag could not be saved to database.", tagService.create(tagEntity));
    }


    @Test
    @Rollback
    public void testLoadById() {
        Assert.assertTrue("Tag could not be created.", tagService.create(tagEntity));
        Assert.assertEquals("Actual and expected are not equal",
                tagEntity.getName(),
                tagService.load(tagEntity.getId()).getName());
    }

    @Test
    @Rollback
    public void testLoadByName() {
        Assert.assertTrue("Could not create entity", tagService.create(tagEntity));
        TagEntity temp = tagService.load(tagEntity.getName(), false);
        Assert.assertNotNull("Could not find any entity.", temp);
        Assert.assertEquals("Actual and expected are not equal", tagEntity.getName(), temp.getName());
    }

    @Test
    @Rollback
    public void testLoadByNameWithNullString() {
        Assert.assertNull("Could not find any entity.", tagService.load(null, false));
    }

    @Test
    @Rollback
    public void testLoadByNameNonExistingEntity() {
        Assert.assertNull("Could not find any entity.", tagService.load("doesn't exist", false));
    }

    @Test
    @Rollback
    public void testLoadTagList() {
        List<TagEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TagEntity tag = new TagEntity();
            tag.setCreatedDate(new Date());
            tag.setName("tag" + i);
            tagService.create(tag);
            list.add(tag);
        }
        Assert.assertEquals("Lists don't match.", list, tagService.loadAll());
    }

    @Test
    @Rollback
    public void testUpdate() {
        Assert.assertTrue("Tag could not be created.", tagService.create(tagEntity));
        TagEntity tagEntity2 = tagService.load(tagEntity.getId());
        tagEntity2.setName("Wobble.");
        tagService.update(tagEntity2);
        Assert.assertEquals("Tag could not be updated.", tagEntity.getName(), tagService.load(tagEntity2.getId()).getName());

    }

    @Test
    @Rollback
    public void testRemove() {
        Assert.assertTrue("Tag could not be created.", tagService.create(tagEntity));
        tagService.remove(tagEntity);
        Assert.assertNull("Tag could not be removed.", tagService.load(tagEntity.getId()));
    }

    @Test
    @Rollback
    public void testCreateTagForImage() {
        String testTag = "tag1";
        ImageEntity img = getImage();
        imageService.create(img);
        tagService.create(testTag, img);
        Assert.assertEquals("Tag was not created.", testTag, tagService.load(testTag, true).getName());
    }

    @Test
    @Rollback
    public void testAddDuplicateTagForImage() {
        String testTag = "test";
        ImageEntity img = getImage();
        imageService.create(img);
        tagService.create(testTag, img);
        Boolean result = tagService.create(testTag, img);
        Assert.assertFalse("Duplicate tag was created.", result);
    }

    @Test
    @Rollback
    public void testCreateDuplicateTag() {
        tagService.create(tagEntity);
        TagEntity tag = new TagEntity();
        tag.setCreatedDate(new Date());
        tag.setName("test");
        Assert.assertFalse("Tag got created, but shouldn't be.", tagService.create(tag));
    }

    @Test
    @Rollback
    public void testAddTagThatImageAlreadyContains() {
        tagService.create(tagEntity);
        ImageEntity img = getImage();
        imageService.create(img);
        entityManager.detach(img);
        img = imageService.load(img.getId());
        tagEntity.addImage(img);
        tagService.update(tagEntity);

        Assert.assertFalse("Tag has been added, although it already contains it.",
                tagService.create(tagEntity.getName(), img));
    }

    public ImageEntity getImage() {
        ImageEntity img = new ImageEntity();
        img.setName("test_img");
        img.setCreatedDate(new Date());
        byte[] data = {1};
        ByteData temp = new ByteData();
        temp.setData(data);
        img.setThumbnail(temp);
        img.setMediumImage(temp);
        img.setDownload(temp);
        return img;
    }
}
