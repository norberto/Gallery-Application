package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.CatalogueDao;
import edu.norbertzardin.entities.CatalogueEntity;
import edu.norbertzardin.entities.ImageEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CatalogueDAOImpl implements CatalogueDao {

    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

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
}
