package edu.norbertzardin.service;


import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

@Service("tagService")
public class TagService {
    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public boolean create(TagEntity te) {
        try {
            tagDao.create(te);
            return true;
        } catch (JpaSystemException | PersistenceException e) {
            return false;
        }
    }
    // Return: true - created, false - already has or updated a tag.
    public Boolean create(String tag_name, ImageEntity ie) {
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
        TagEntity te = load(tag_name, true);
        if(te == null) {
            te = new TagEntity();
            te.setName(tag_name);
            te.addImage(ie);
            te.setCreatedDate(new Date());
            tagDao.create(te);
            return true;
        } else {
            te.addImage(ie);
            update(te);
            return false;
        }
    }

    public TagEntity load(String name, Boolean fetch) {
        if (name != null && !name.isEmpty()) {
            try {
                return tagDao.load(name, fetch);
            } catch (NoResultException e){
                return null;
            }
        }
        return null;
    }

    public TagEntity load(Long id) {
        return tagDao.load(id);
    }


    public List<TagEntity> loadAll() {
        return tagDao.loadAll();
    }

    public void update(TagEntity tag) {
        tagDao.update(tag);
    }

    public void remove(TagEntity tag) {
        tagDao.remove(tag);
    }
}
