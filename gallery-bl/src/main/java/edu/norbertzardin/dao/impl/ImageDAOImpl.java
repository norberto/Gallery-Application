package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.entities.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class ImageDAOImpl implements ImageDao {

    private CriteriaBuilder cb;
    private CriteriaQuery<ImageEntity> cq;
    private Root<ImageEntity> image;

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
    public List<ImageEntity> getImageList(Integer page, Integer pageMax) {
        setUpCriteriaBuilderForImage();
        fetchThumbnail();
        CriteriaQuery<ImageEntity> select = cq.select(image);
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
    public ImageEntity getImageById(Long id){
        return entityManager.find(ImageEntity.class, id);
    }

    @Transactional
    public ImageEntity getImageByIdWithFetch(Long id) {
        setUpCriteriaBuilderForImage();
        image.fetch(ImageEntity_.mediumImage, JoinType.INNER);
        image.fetch(ImageEntity_.tags, JoinType.INNER);

        Predicate name_ = cb.equal(image.get(ImageEntity_.id), id);

        return entityManager
                .createQuery( cq.where(name_).select(image))
                .getSingleResult();
    }

    @Transactional
    public List<ImageEntity> findImagesByKey(String keyword) {
        setUpCriteriaBuilderForImage();
        fetchThumbnail();

        ListJoin<ImageEntity, TagEntity> join = image.join(ImageEntity_.tags, JoinType.INNER);
        String key = (keyword == null) ? "%" : ("%" + keyword.toLowerCase() + "%");

        Predicate name = cb.like(cb.lower(image.get(ImageEntity_.name)), key);
        Predicate description = cb.like(cb.lower(image.get(ImageEntity_.description)), key);
        Predicate tags = cb.like(join.get(TagEntity_.name), key);
        cq.select(image).where(cb.or(name, description, tags));

        return entityManager.createQuery(cq).getResultList();
    }

    public Integer getPageCount(Integer pageMax) {
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

    public ImageEntity getImageByIdFullFetch(Long id) {
        setUpCriteriaBuilderForImage();
        fetchAll();

        Predicate name_ = cb.equal(image.get(ImageEntity_.id), id);
        cq.where(name_).select(image);
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    public void updateImage(ImageEntity ie) {
        entityManager.merge(ie);
    }

    @PersistenceContext
    public void setEntityManagerFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

    private void setUpCriteriaBuilderForImage() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(ImageEntity.class);
        image = cq.from(ImageEntity.class);
    }

    private void fetchAll() {
        image.fetch(ImageEntity_.thumbnail, JoinType.INNER);
        image.fetch(ImageEntity_.mediumImage, JoinType.INNER);
        image.fetch(ImageEntity_.download, JoinType.INNER);
        image.fetch(ImageEntity_.tags, JoinType.LEFT);
    }

    private void fetchThumbnail() {
        image.fetch(ImageEntity_.thumbnail, JoinType.INNER);
    }
}
