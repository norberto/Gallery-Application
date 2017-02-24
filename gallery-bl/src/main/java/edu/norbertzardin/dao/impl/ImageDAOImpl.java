package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.CatalogueEntity_;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.ImageEntity_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
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
    public void deleteImage(ImageEntity ie) {
        entityManager.remove(entityManager.find(ImageEntity.class, ie.getId()));
    }

    @Transactional
    public List<ImageEntity> getImageList(Integer page, int pageMax) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ImageEntity> cq = cb.createQuery(ImageEntity.class);
        Root<ImageEntity> root = cq.from(ImageEntity.class);
        root.fetch(ImageEntity_.thumbnail, JoinType.INNER);
        CriteriaQuery<ImageEntity> select = cq.select(root);
        TypedQuery<ImageEntity> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((page - 1) * pageMax);
        typedQuery.setMaxResults(pageMax);
         return  typedQuery.getResultList();
    }

    @Transactional
    public List<ImageEntity> getImageList() {
        return entityManager.createQuery("from ImageEntity ", ImageEntity.class).getResultList();
    }

    @Transactional
    public ImageEntity getImageById(int id){
        return entityManager.find(ImageEntity.class, id);
    }

    @Transactional
    public ImageEntity getImageByIdWithFetch(int id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ImageEntity> cq = cb.createQuery(ImageEntity.class);
        Root<ImageEntity> root = cq.from(ImageEntity.class);
        root.fetch(ImageEntity_.mediumImage, JoinType.INNER);
        root.fetch(ImageEntity_.tags, JoinType.INNER);
        Predicate name_ = cb.equal(root.get(ImageEntity_.id), id);
        cq.where(name_).select(root);
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    public List<ImageEntity> findImagesByName(String name) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ImageEntity> cq = cb.createQuery(ImageEntity.class);
        Root<ImageEntity> root = cq.from(ImageEntity.class);

        String nameSearchTerm = (name == null) ? "%" : ("%" + name.toLowerCase() + "%");
        Predicate name_ = cb.like(cb.lower(root.get(ImageEntity_.name)), nameSearchTerm);
        Predicate description_ = cb.like(cb.lower(root.get(ImageEntity_.description)), nameSearchTerm);
        Predicate OR  = cb.or(name_, description_);

        cq.where(OR).select(root);

        return entityManager.createQuery(cq).getResultList();
    }

    public int getPageCount(int pageMax) {
        int imageCount = entityManager
                .createQuery("from ImageEntity ", ImageEntity.class)
                .getResultList()
                .size();
        int pageCount = imageCount / pageMax;
        if(pageCount * pageMax < imageCount) {
            return pageCount + 1;
        } else {
            return pageCount;
        }
    }

    @PersistenceContext
    public void setEntityManagerFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

}
