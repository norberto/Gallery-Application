package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ImageDao;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.ImageEntity_;
import edu.norbertzardin.entities.TagEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ImageDAOImpl implements ImageDao {

    private CriteriaBuilder cb;
    private CriteriaQuery<ImageEntity> cq;
    private Root<ImageEntity> image;

    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    public void save(ImageEntity image) throws PersistenceException {
        entityManager.persist(image);
        entityManager.flush();
    }

    public void remove(Long id) throws IllegalArgumentException {
        entityManager.remove(entityManager.find(ImageEntity.class, id));
    }

    public List<ImageEntity> loadAll(Integer page, Integer pageMax) {
        setUpCriteriaBuilderForImage();
        fetchThumbnail();
        return pageContent(page, pageMax, entityManager.createQuery(cq.select(image)));
    }

    public ImageEntity load(Long id, Boolean thumbnail, Boolean medium, Boolean download, Boolean tags) {
        setUpCriteriaBuilderForImage();

        if (thumbnail) {
            image.fetch(ImageEntity_.thumbnail, JoinType.INNER);
        }
        if (medium) {
            image.fetch(ImageEntity_.mediumImage, JoinType.INNER);
        }
        if (download) {
            image.fetch(ImageEntity_.download, JoinType.INNER);
        }
        if (tags) {
            image.fetch(ImageEntity_.tags, JoinType.LEFT);
        }

        cq.select(image).where(cb.equal(image.get(ImageEntity_.id), id));
        return entityManager.createQuery(cq).getSingleResult();
    }

    public List<ImageEntity> find(String keyword, TagEntity tag, Integer page, Integer pageMax) {
        setUpCriteriaBuilderForImage();
        Expression<List<TagEntity>> tagList = image.get("tags");
        fetchThumbnail();
        Predicate name = cb.like(cb.lower(image.get(ImageEntity_.name)), keyword.toLowerCase() + "%");
        Predicate description = cb.like(cb.lower(image.get(ImageEntity_.description)), keyword.toLowerCase() + "%");
        CriteriaQuery<ImageEntity> select;
        if (tag != null) {
            Predicate key_tag = cb.isMember(tag, tagList);
            select = cq.select(image).where(cb.or(name, description, key_tag));
        } else {
            select = cq.select(image).where(cb.or(name, description));
        }
        return pageContent(page, pageMax, entityManager.createQuery(select));
    }

    public Long count(String searchString, TagEntity searchTag) {
        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<ImageEntity> root = criteria.from(ImageEntity.class);
        Expression<List<TagEntity>> tagList = root.get("tags");
        criteria.select(cb.count(root));
        if (searchString != null) {
            Predicate name = cb.like(cb.lower(root.get(ImageEntity_.name)), searchString.toLowerCase());
            Predicate description = cb.like(cb.lower(root.get(ImageEntity_.description)), searchString.toLowerCase());
            if (searchTag != null) {
                Predicate key_tag = cb.isMember(searchTag, tagList);
                criteria.where(cb.or(name, description, key_tag));
            } else {
                criteria.where(cb.or(name, description));
            }
        }
        return entityManager.createQuery(criteria).getSingleResult();
    }

    // Update Image in database
    public void update(ImageEntity ie) {
        entityManager.merge(ie);
    }

    @Transactional
    public List<ImageEntity> loadFromCatalogue(Integer page, Integer pageMax, CatalogueEntity catalogue) {
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
}
