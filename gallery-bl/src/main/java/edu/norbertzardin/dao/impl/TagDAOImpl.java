package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDao {
    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    public ImageEntity getImageById(int _id){
        ImageEntity result = entityManager.find(ImageEntity.class, _id);
        return result;
    }

    @PersistenceContext
    public void setEntityManagerFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

    @Transactional
    public void createTag(TagEntity tag) {
        entityManager.persist(tag);
    }

    public List<TagEntity> getTagList() {
        return entityManager.createQuery("from TagEntity ", TagEntity.class).getResultList();
    }

    public TagEntity getTagById(int id) {
        return entityManager.find(TagEntity.class, id);
    }

    public void pair(ImageEntity image, TagEntity tag) {

    }
}
