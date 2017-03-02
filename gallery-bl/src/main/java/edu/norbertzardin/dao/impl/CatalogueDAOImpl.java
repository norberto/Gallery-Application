package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.CatalogueDao;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.CatalogueEntity_;
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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CatalogueDAOImpl implements CatalogueDao {

    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<CatalogueEntity> cq;
    private Root<CatalogueEntity> catalogue;

    @Transactional
    public void createCatalogue(CatalogueEntity ce) {
        entityManager.persist(ce);
    }

    public List<CatalogueEntity> getCatalogueList() {
        List<CatalogueEntity> result = entityManager.createQuery("from CatalogueEntity ", CatalogueEntity.class).getResultList();
        return result;
    }
    @Transactional
    public void editCatalogue(CatalogueEntity ce) {
        entityManager.merge(ce);
    }

    @Transactional
    public void deleteCatalogue(Long id) { entityManager.remove(entityManager.find(CatalogueEntity.class, id)); }

    @Transactional
    public CatalogueEntity getCatalogueByName(String name) {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(CatalogueEntity.class);
        catalogue = cq.from(CatalogueEntity.class);
        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);

        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.title), name);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    public CatalogueEntity getCatalogueByNameNoFetch(String name) {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(CatalogueEntity.class);
        catalogue = cq.from(CatalogueEntity.class);
//        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);

        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.title), name);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    public CatalogueEntity getCatalogueById(Long id) {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(CatalogueEntity.class);
        catalogue = cq.from(CatalogueEntity.class);

        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.id), id);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    public CatalogueEntity getCatalogueByIdMediumFetch(Long id) {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(CatalogueEntity.class);
        catalogue = cq.from(CatalogueEntity.class);

        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.mediumImage, JoinType.LEFT);
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.id), id);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    // Set up criteria builder for CatalogueEntity model
    private void setUpCriteriaBuilderForImage() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(CatalogueEntity.class);
        catalogue = cq.from(CatalogueEntity.class);
    }

    public Long getPageCount(Integer pageMax) {
        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        criteria.select(cb.count(criteria.from(CatalogueEntity.class)));
        Long imageCount = entityManager.createQuery(criteria).getSingleResult();

        Long pageCount = imageCount / pageMax;
        if(pageCount * pageMax < imageCount) {
            return pageCount + 1;
        } else {
            return pageCount;
        }
    }
}
