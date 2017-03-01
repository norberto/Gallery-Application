package edu.norbertzardin.service;


import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Date;
import java.util.List;

@Service("tagService")
public class TagService {
    @Autowired
    private TagDao tagDao;

    public void createTag(TagEntity ce) {
        tagDao.createTag(ce);
    }

    public void createTag(String tag_name, ImageEntity ie) {
        TagEntity tag_ = getTagByName(tag_name);
        if(tag_ == null){
            TagEntity te = new TagEntity();
            te.setName(tag_name);
            te.addImage(ie);
            te.setCreatedDate(new Date());
            createTag(te);
        } else {
            tag_.addImage(ie);
            updateTag(tag_);
        }
    }

    public TagEntity getTagByName(String name) { return tagDao.getTagByName(name); }

    public TagEntity getTagById(Long id) { return tagDao.getTagById(id); }

    public List<TagEntity> getTagList() {
        return tagDao.getTagList();
    }

    public void setTagDao(TagDao tagDao){
        this.tagDao = tagDao;
    }

    public TagDao getTagDao(){
        return tagDao;
    }

    public void updateTag(TagEntity tag) {
        tagDao.updateTag(tag);
    }

    public void removeTag(TagEntity tag) { tagDao.remove(tag);}
}
