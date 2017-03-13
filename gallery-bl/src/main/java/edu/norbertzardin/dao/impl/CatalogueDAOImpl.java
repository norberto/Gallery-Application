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
import javax.persistence.criteria.Expression;
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
    public void save(CatalogueEntity ce) {
        entityManager.persist(ce);
    }



    @Transactional
    public void update(CatalogueEntity ce) {
        entityManager.merge(ce);
    }

    @Transactional
    public void remove(Long id) { entityManager.remove(entityManager.find(CatalogueEntity.class, id)); }

    @Transactional
    public CatalogueEntity load(String name) throws NoResultException {
        setUpCriteriaBuilderForCatalogue();
        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.title), name);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    public CatalogueEntity load(Long id) {
        setUpCriteriaBuilderForCatalogue();
        catalogue.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.id), id);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Transactional
    public CatalogueEntity loadNoFetch(String name) throws NoResultException {
        setUpCriteriaBuilderForCatalogue();
        Predicate name_ = cb.equal(catalogue.get(CatalogueEntity_.title), name);
        cq.where(name_).select(catalogue);
        return entityManager.createQuery(cq).getSingleResult();
    }



    public CatalogueEntity loadMediumFetch(Long id) {
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

    public Long imageCount(CatalogueEntity catalogue) {
        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<ImageEntity> image = criteria.from(ImageEntity.class);
        criteria.select(cb.count(image));
        criteria.where( cb.equal(image.get(ImageEntity_.catalogue), catalogue));
        return entityManager.createQuery(criteria).getSingleResult();
    }

    public List<CatalogueEntity> loadByKey(String key) {
        setUpCriteriaBuilderForCatalogue();
        String keyword = (key == null) ? "%" : ( "%" + key.toLowerCase() + "%");
        Predicate name = cb.like(cb.lower(catalogue.get(CatalogueEntity_.title)), keyword);
        cq.select(catalogue).where(cb.or(name));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Long count(String searchString) {
        String[] escapedChars = {
                "%", "_", "!", "~", ";", "-", "[", "]", 
                "{", "}", "?", "&", ",", "|", "*"
        };

        cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        criteria.select(cb.count(criteria.from(CatalogueEntity.class)));
        
        if(searchString != null && !searchString.equals("")) {
            for(String c : escapedChars) {
                searchString = searchString.replace(c, "\\" + c);
            }

            criteria.where(cb.like(cb.lower(catalogue.get(CatalogueEntity_.title)), searchString.toLowerCase() + "%", '\\'));
        }
        return entityManager.createQuery(criteria).getSingleResult();

    }

    public List<CatalogueEntity> loadByPage(Integer cataloguePage, Integer pageCatalogueMax, String searchString, Boolean includeDefault) {
        setUpCriteriaBuilderForCatalogue();
        TypedQuery<CatalogueEntity> typedQuery;
        CriteriaQuery<CatalogueEntity> select;
        if(searchString != null && !searchString.equals("")) {
            searchString = searchString.replace("%", "\\%").toLowerCase() + "%";
            Predicate search = cb.like(cb.lower(catalogue.get(CatalogueEntity_.title)), searchString, '\\');
            if(!includeDefault) {
                Predicate predicate = cb.equal(catalogue.get(CatalogueEntity_.title), "Non-categorized");
                select = cq.select(catalogue).where(cb.and(predicate.not(), search));
            } else {
                select = cq.select(catalogue).where(search);
            }
        } else {
            select = cq.select(catalogue);
        }

        typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((cataloguePage - 1) * pageCatalogueMax);
        typedQuery.setMaxResults(pageCatalogueMax);
        return  typedQuery.getResultList();
    }
}
