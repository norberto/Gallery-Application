package edu.norbertzardin.service;


import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tagService")
public class TagService {
    @Autowired
    private TagDao tagDao;

    public void createTag(TagEntity ce) {
        tagDao.createTag(ce);
    }

//    public void deleteTag(TagEntity ce) {
//        tagDao.deleteTag(ce.getId());
//    }

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
}
