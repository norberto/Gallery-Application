package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.entities.ByteData;
import edu.norbertzardin.entities.ByteData_;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.ImageEntity_;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        return pageContent(page, pageMax, entityManager.createQuery(select));
    }

    public List<ImageEntity> getImageList() {
        return entityManager.createQuery("from ImageEntity ", ImageEntity.class).getResultList();
    }

    public ImageEntity getImageById(Long id) {
        return entityManager.find(ImageEntity.class, id);
    }

    public ImageEntity getImageByIdWithFetch(Long id) {
        setUpCriteriaBuilderForImage();
        image.fetch(ImageEntity_.mediumImage, JoinType.INNER);
        image.fetch(ImageEntity_.tags, JoinType.INNER);
        Predicate name_ = cb.equal(image.get(ImageEntity_.id), id);
        return entityManager
                .createQuery(cq.where(name_).select(image))
                .getSingleResult();
    }

    public List<ImageEntity> findImagesByKeys(String keyword, TagEntity tag, Integer page, Integer pageMax) {
        Expression<List<TagEntity>> tagList = image.get("tags");
        setUpCriteriaBuilderForImage();
        fetchThumbnail();
        Predicate name = cb.like(cb.lower(image.get(ImageEntity_.name)), keyword.toLowerCase());
        Predicate description = cb.like(cb.lower(image.get(ImageEntity_.description)), keyword.toLowerCase());
        CriteriaQuery<ImageEntity> select;
        if (tag != null) {
            Predicate key_tag = cb.isMember(tag, tagList);
            select = cq.select(image).where(cb.or(name, description, key_tag));
        } else {
            select = cq.select(image).where(cb.or(name, description));
        }

        return pageContent(page, pageMax, entityManager.createQuery(select));
    }

    public Long getImageCountSearch(String searchString, TagEntity searchTag) {
        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<ImageEntity> root = criteria.from(ImageEntity.class);
        Expression<List<TagEntity>> tagList = root.get("tags");

        Predicate name = cb.like(cb.lower(root.get(ImageEntity_.name)), searchString.toLowerCase());
        Predicate description = cb.like(cb.lower(root.get(ImageEntity_.description)), searchString.toLowerCase());
        if (searchTag != null) {
            Predicate key_tag = cb.isMember(searchTag, tagList);
            criteria.select(cb.count(root)).where(cb.or(name, description, key_tag));
        } else {
            criteria.select(cb.count(root)).where(cb.or(name, description));
        }
        return entityManager.createQuery(criteria).getSingleResult();
    }

    public Long getImageCount() {
        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        criteria.select(cb.count(criteria.from(ImageEntity.class)));
        return entityManager.createQuery(criteria).getSingleResult();
    }

    public ImageEntity getImageByIdFullFetch(Long id) {
        setUpCriteriaBuilderForImage();
        fetchAll();
        Predicate name_ = cb.equal(image.get(ImageEntity_.id), id);
        cq.where(name_).select(image);
        return entityManager.createQuery(cq).getSingleResult();
    }

    // Update Image in database
    public void updateImage(ImageEntity ie) {
        entityManager.merge(ie);
    }

    @Transactional
    public List<ImageEntity> getImageListFromFolderForPage(Integer page, Integer pageMax, CatalogueEntity catalogue) {
        setUpCriteriaBuilderForImage();
        fetchThumbnail();

        Predicate p = cb.equal(image.get(ImageEntity_.catalogue), catalogue);
        CriteriaQuery<ImageEntity> select = cq.select(image).where(p);
        return pageContent(page, pageMax, entityManager.createQuery(select));
    }


    // Set up criteria builder for ImageEntity model
    private void setUpCriteriaBuilderForImage() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(ImageEntity.class);
        image = cq.from(ImageEntity.class);
    }

    // Fetches all contents of an Entity, for viewing an image
    private void fetchAll() {
//        image.fetch(ImageEntity_.thumbnail, JoinType.INNER);
//        image.fetch(ImageEntity_.download, JoinType.INNER);

        image.fetch(ImageEntity_.mediumImage, JoinType.INNER);
        image.fetch(ImageEntity_.tags, JoinType.LEFT);
    }

    public ByteData getDownloadById(Long id) {
        setUpCriteriaBuilderForImage();
        image.fetch(ImageEntity_.download, JoinType.INNER);
        Predicate name_ = cb.equal(image.get(ImageEntity_.id), id);
        cq.where(name_).select(image);
        return entityManager.createQuery(cq).getSingleResult().getDownload();
    }

    private List<ImageEntity> pageContent(Integer page, Integer pageMax, TypedQuery<ImageEntity> typedQuery) {
        typedQuery.setFirstResult((page - 1) * pageMax);
        typedQuery.setMaxResults(pageMax);
        return typedQuery.getResultList();
    }

    // Fetches only thumbnail - for listing images
    private void fetchThumbnail() {
        image.fetch(ImageEntity_.thumbnail, JoinType.INNER);
    }

    @PersistenceContext
    public void setEntityManagerFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
