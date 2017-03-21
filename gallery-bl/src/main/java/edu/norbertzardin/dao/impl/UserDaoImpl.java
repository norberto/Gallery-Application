package edu.norbertzardin.dao.impl;
import java.util.List;

import edu.norbertzardin.dao.UserDao;
import edu.norbertzardin.entities.TagEntity_;
import edu.norbertzardin.entities.UserEntity;
import edu.norbertzardin.entities.UserEntity_;
import edu.norbertzardin.entities.UserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public UserEntity findByUserName(String username) {
        return entityManager.find(UserEntity.class, username);
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