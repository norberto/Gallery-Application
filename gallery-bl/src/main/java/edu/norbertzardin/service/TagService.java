package edu.norbertzardin.service;


import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("tagService")
public class TagService {
    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public void createTag(TagEntity ce) {
        tagDao.createTag(ce);
    }

    public Boolean createTag(String tag_name, ImageEntity ie) {
        // Before creating or updating tags - check if an image already has one
        List<TagEntity> tags = ie.getTags();
        if (tags != null) {
            for (TagEntity t : tags) {
                if (t.getName().equals(tag_name)) {
                    return false;
                }
            }
        }
        // If tag does not exist yet - create it
        try {
            TagEntity te = new TagEntity();
            te.setName(tag_name);
            te.addImage(ie);
            te.setCreatedDate(new Date());
            createTag(te);
        } catch(JpaSystemException e) { // if tag exists assign it to an image
            TagEntity tag_ = getTagByName(tag_name);
            tag_.addImage(ie);
            updateTag(tag_);
        }
        return true;
    }

    public TagEntity getTagByName(String name) {
        if (name != null && !name.isEmpty()) {
            return tagDao.getTagByName(name);
        } else {
            return null;
        }
    }

    public TagEntity getTagById(Long id) {
        return tagDao.getTagById(id);
    }

    public List<TagEntity> getTagList() {
        return tagDao.getTagList();
    }

    public void updateTag(TagEntity tag) {
        tagDao.updateTag(tag);
    }

    public void removeTag(TagEntity tag) {
        tagDao.remove(tag);
    }
}
