package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ByteDataDao;
import edu.norbertzardin.entities.ByteData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ByteDataDAOImpl implements ByteDataDao {

    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    @Override
    public ByteData getByteDataById(Long id) {
        return entityManager.find(ByteData.class, id);
    }

    @Transactional
    public void createByteData(ByteData bd) {
        entityManager.persist(bd);
    }

    public void setEntityManager(EntityManager entityManager) { this.entityManager = entityManager; }

    public EntityManager getEntityManager() { return entityManager; }
}
