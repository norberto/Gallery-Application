package edu.norbertzardin.dao.impl;
import java.util.List;

import edu.norbertzardin.dao.UserDao;
import edu.norbertzardin.entities.UserEntity;
import edu.norbertzardin.entities.UserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public UserEntity findByUserName(String username) {

        List<UserEntity> users;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);
        Root<UserEntity> user = cq.from(UserEntity.class);

        users = entityManager.createQuery(cq.select(user)).getResultList();

        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }

    }

    @Override
    public void create(UserEntity user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    public void create(UserRole userRole) {
        entityManager.persist(userRole);
        entityManager.flush();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}