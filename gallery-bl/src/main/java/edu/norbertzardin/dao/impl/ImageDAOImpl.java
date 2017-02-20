package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.entities.ImageEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.zul.Messagebox;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ImageDAOImpl implements ImageDao {

    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    @Transactional
    public void createImage(ImageEntity image) {
        entityManager.persist(image);
    }

    @Transactional
    public void deleteImageById(int id) {
        entityManager.remove(entityManager.find(ImageEntity.class, id));
    }

    public List<ImageEntity> getImageList() {
        List<ImageEntity> result = entityManager.createQuery("from ImageEntity ", ImageEntity.class).getResultList();

        return result;
    }

    public ImageEntity getImageById(int id){
        ImageEntity result = entityManager.find(ImageEntity.class, id);
        return result;
    }

    @PersistenceContext
    public void setEntityManagerFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

}
