package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.ImageEntity_;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
// TODO Re-do criteria building

@Repository
public class ImageDAOImpl implements ImageDao {

    private CriteriaBuilder cb;
    private CriteriaQuery<ImageEntity> cq;
    private Root<ImageEntity> image;

    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    public void createImage(ImageEntity image) {
        entityManager.persist(image);
    }

    public void deleteImage(ImageEntity ie) {
        entityManager.remove(entityManager.find(ImageEntity.class, ie.getId()));
    }

    public List<ImageEntity> getImageList(Integer page, Integer pageMax) {
        setUpCriteriaBuilderForImage();
        fetchThumbnail();
        CriteriaQuery<ImageEntity> select = cq.select(image);
        TypedQuery<ImageEntity> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((page - 1) * pageMax);
        typedQuery.setMaxResults(pageMax);
         return  typedQuery.getResultList();
    }

    public List<ImageEntity> getImageList() {
        return entityManager.createQuery("from ImageEntity ", ImageEntity.class).getResultList();
    }

    public ImageEntity getImageById(Long id){
        return entityManager.find(ImageEntity.class, id);
    }

    public ImageEntity getImageByIdWithFetch(Long id) {
        setUpCriteriaBuilderForImage();
        image.fetch(ImageEntity_.mediumImage, JoinType.INNER);
        image.fetch(ImageEntity_.tags, JoinType.INNER);

        Predicate name_ = cb.equal(image.get(ImageEntity_.id), id);

        return entityManager
                .createQuery( cq.where(name_).select(image))
                .getSingleResult();
    }

    public List<ImageEntity> findImagesByKeys(String keyword, TagEntity tag) {
        Expression<List<TagEntity>> tagList = image.get("tags");

        setUpCriteriaBuilderForImage();
        fetchThumbnail();
        String key = (keyword == null) ? "%" : (keyword.toLowerCase() + "%");

        Predicate name = cb.like(cb.lower(image.get(ImageEntity_.name)), key);
        Predicate description = cb.like(cb.lower(image.get(ImageEntity_.description)), key);

        if(tag != null) {
            Predicate key_tag = cb.isMember(tag, tagList);
            cq.select(image).where(cb.or(name, description, key_tag));
        } else {
            cq.select(image).where(cb.or(name, description));
        }

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
