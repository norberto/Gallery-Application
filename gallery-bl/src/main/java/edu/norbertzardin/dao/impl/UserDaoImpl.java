package edu.norbertzardin.dao.impl;
import java.util.List;

import edu.norbertzardin.dao.UserDao;
import edu.norbertzardin.entities.*;
import org.h2.engine.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public UserEntity findByUserName(String username) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);
        Root<UserEntity> user = cq.from(UserEntity.class);
        user.fetch(UserEntity_.userRole, JoinType.LEFT);
        Predicate name_ = cb.equal(user.get(UserEntity_.username), username);
        cq.where(name_).select(user);
        List<UserEntity> list =  entityManager.createQuery(cq).getResultList();
        if(list.size() == 0){
            return null;
        }
        return list.get(0);
//        return entityManager.find(UserEntity.class, username);
    }

    @Override
    @Transactional
    public void create(UserEntity user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    @Transactional
    public void create(UserRole userRole) {
        entityManager.persist(userRole);
        entityManager.flush();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}