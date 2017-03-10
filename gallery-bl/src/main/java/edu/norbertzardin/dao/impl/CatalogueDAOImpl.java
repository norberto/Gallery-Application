package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.CatalogueDao;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.CatalogueEntity_;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.ImageEntity_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public List<CatalogueEntity> getCatalogueListByPage(Integer cataloguePage, Integer pageCatalogueMax) {
        setUpCriteriaBuilderForCatalogue();
        Predicate predicate = cb.equal(catalogue.get(CatalogueEntity_.title), "Non-categorized");
        CriteriaQuery<CatalogueEntity> select = cq.select(catalogue).where(predicate.not());
        TypedQuery<CatalogueEntity> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((cataloguePage - 1) * pageCatalogueMax);
        typedQuery.setMaxResults(pageCatalogueMax);
        return  typedQuery.getResultList();
    }

    public List<CatalogueEntity> getCatalogueList() {
        return entityManager.createQuery("from CatalogueEntity ", CatalogueEntity.class).getResultList();
    }

    @Transactional
    public void editCatalogue(CatalogueEntity ce) {
        entityManager.merge(ce);
    }

    @Transactional
    public void deleteCatalogue(Long id) { entityManager.remove(entityManager.find(CatalogueEntity.class, id)); }

    @Transactional
    public CatalogueEntity getCatalogueByName(String name) throws NoResultException {
        setUpCriteriaBuilderForCatalogue();
        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.title), name);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    public CatalogueEntity getCatalogueByNameNoFetch(String name) throws NoResultException {
        setUpCriteriaBuilderForCatalogue();
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.title), name);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    public CatalogueEntity getCatalogueById(Long id) {
        setUpCriteriaBuilderForCatalogue();
        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.id), id);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    public CatalogueEntity getCatalogueByIdMediumFetch(Long id) {
        setUpCriteriaBuilderForCatalogue();
        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.mediumImage, JoinType.LEFT);
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.id), id);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    // Set up criteria builder for CatalogueEntity model
    private void setUpCriteriaBuilderForCatalogue() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(CatalogueEntity.class);
        catalogue = cq.from(CatalogueEntity.class);
    }

    public Long getPageCount(CatalogueEntity catalogue) {
        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<ImageEntity> image = criteria.from(ImageEntity.class);
        criteria.select(cb.count(image));
        criteria.where( cb.equal(image.get(ImageEntity_.catalogue), catalogue));
        return entityManager.createQuery(criteria).getSingleResult();
    }

    public List<CatalogueEntity> getCatalogueListByKey(String key) {
        setUpCriteriaBuilderForCatalogue();
        String keyword = (key == null) ? "%" : ( "%" + key.toLowerCase() + "%");
        Predicate name = cb.like(cb.lower(catalogue.get(CatalogueEntity_.title)), keyword);
        cq.select(catalogue).where(cb.or(name));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Long getCatalogueCount() {
        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        criteria.select(cb.count(criteria.from(CatalogueEntity.class)));
        return entityManager.createQuery(criteria).getSingleResult();
    }
}
