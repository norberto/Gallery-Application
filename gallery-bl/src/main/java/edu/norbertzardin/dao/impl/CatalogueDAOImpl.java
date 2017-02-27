package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.CatalogueDao;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.CatalogueEntity_;
import edu.norbertzardin.entities.ImageEntity_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class CatalogueDAOImpl implements CatalogueDao {

    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<CatalogueEntity> cq;
    private Root<CatalogueEntity> root;

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
        root = cq.from(CatalogueEntity.class);
        root.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);

        Predicate name_ = cb.equal(root.get(CatalogueEntity_.title), name);
        cq.where(name_).select(root);
        return entityManager.createQuery(cq).getSingleResult();
    }

    public CatalogueEntity getCatalogueById(Long id) {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(CatalogueEntity.class);
        root = cq.from(CatalogueEntity.class);

        root.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.thumbnail, JoinType.LEFT);
        Predicate name_ = cb.equal(root.get(CatalogueEntity_.id), id);
        cq.where(name_).select(root);
        return entityManager.createQuery(cq).getSingleResult();
    }

    public CatalogueEntity getCatalogueByIdMediumFetch(Long id) {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(CatalogueEntity.class);
        root = cq.from(CatalogueEntity.class);

        root.fetch(CatalogueEntity_.images, JoinType.LEFT).fetch(ImageEntity_.mediumImage, JoinType.LEFT);
        Predicate name_ = cb.equal(root.get(CatalogueEntity_.id), id);
        cq.where(name_).select(root);
        return entityManager.createQuery(cq).getSingleResult();
    }


}
