package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.entities.TagEntity_;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDao {

    private EntityManager entityManager;

    private CriteriaBuilder cb;
    private CriteriaQuery<TagEntity> cq;
    private Root<TagEntity> tag;

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    public void setEntityManagerFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public TagEntity load(Long id) throws NoResultException {
        return entityManager.find(TagEntity.class, id);
    }

    @Transactional
    public void create(TagEntity tag) throws PersistenceException, JpaSystemException {
        entityManager.persist(tag);
        entityManager.flush();
    }

    @Transactional
    public void update(TagEntity tag) {
        entityManager.merge(tag);
    }

    @Transactional
    public List<TagEntity> loadAll() {
        setUpCriteriaBuilderForTag();
        return entityManager.createQuery(cq.select(tag)).getResultList();
    }

    @Transactional
    public TagEntity load(String name, Boolean fetch) throws NoResultException {
        setUpCriteriaBuilderForTag();
        if (fetch) {
            tag.fetch(TagEntity_.images, JoinType.INNER);
        }
        Predicate name_ = cb.equal(cb.lower(tag.get(TagEntity_.name)), name.toLowerCase());
        return entityManager.createQuery(cq.select(tag).where(name_)).getSingleResult();
    }

    private void setUpCriteriaBuilderForTag() {
        cb = entityManager.getCriteriaBuilder();
        cq = cb.createQuery(TagEntity.class);
        tag = cq.from(TagEntity.class);
    }

    @Transactional
    public void remove(TagEntity tag) {
        entityManager.remove(entityManager.find(TagEntity.class, tag.getId()));
    }
}
