package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.ImageEntity_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.zul.Messagebox;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        List<ImageEntity> result;
//        result = entityManager.createQuery("from ImageEntity ", ImageEntity.class).getResultList();
        result = entityManager.

        return result;
    }

    public ImageEntity getImageById(int id){
        return entityManager.find(ImageEntity.class, id);
    }

    public List<ImageEntity> findImagesByName(String name) {
        String nameSearchTerm = (name == null) ? "%" : ("%" + name.toLowerCase() + "%");

        CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery(ImageEntity.class);
        Root root = criteriaQuery.from(ImageEntity.class);

        Predicate predicate = entityManager.getCriteriaBuilder()
                .like(entityManager.getCriteriaBuilder().lower(root.<String>get(ImageEntity_.name)), nameSearchTerm);

        criteriaQuery.where(predicate).select(root);

        TypedQuery typedQuery = entityManager.createQuery(criteriaQuery);

        return typedQuery.getResultList();

    }

    @PersistenceContext
    public void setEntityManagerFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

}
