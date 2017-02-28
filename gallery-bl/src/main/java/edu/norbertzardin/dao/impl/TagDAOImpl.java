package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.entities.TagEntity_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDao {
    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    public TagEntity getTagById(Long id){
        return entityManager.find(TagEntity.class, id);
    }

    @PersistenceContext
    public void setEntityManagerFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

    @Transactional
    public void createTag(TagEntity tag) {
        entityManager.persist(tag);
    }

    @Transactional
    public void updateTag(TagEntity tag) { entityManager.merge(tag); }

    public List<TagEntity> getTagList() {
        return entityManager.createQuery("from TagEntity ", TagEntity.class).getResultList();
    }

    public TagEntity getTagByIdWithFetch(Long id) {
        return entityManager.find(TagEntity.class, id);
    }

    public TagEntity getTagByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagEntity> cq = cb.createQuery(TagEntity.class);
        Root<TagEntity> root = cq.from(TagEntity.class);
        root.fetch(TagEntity_.images, JoinType.INNER);
        String nameSearchTerm = (name == null) ? "%" : (name.toLowerCase() + "%");
        Predicate name_ = cb.like(cb.lower(root.get(TagEntity_.name)), nameSearchTerm);

        cq.where(name_).select(root);

        try{
            return entityManager.createQuery(cq).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
}
