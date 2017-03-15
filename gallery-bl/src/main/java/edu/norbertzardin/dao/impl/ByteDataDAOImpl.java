package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.ByteDataDao;
import edu.norbertzardin.entities.ByteData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@Repository
public class ByteDataDAOImpl implements ByteDataDao {

    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    public ByteData load(Long id) {
        return entityManager.find(ByteData.class, id);
    }

    public void save(ByteData bd) {
        entityManager.persist(bd);
    }
}
