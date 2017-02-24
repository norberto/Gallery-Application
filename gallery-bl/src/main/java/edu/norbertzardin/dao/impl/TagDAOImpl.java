package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.TagDao;
import edu.norbertzardin.entities.ImageEntity;
import edu.norbertzardin.entities.ImageEntity_;
import edu.norbertzardin.entities.TagEntity;
import edu.norbertzardin.entities.TagEntity_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDao {
    @PersistenceContext(name = "imagePersistence")
    private EntityManager entityManager;

    public TagEntity getTagById(int _id){
        TagEntity result = entityManager.find(TagEntity.class, _id);
        return result;
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

    public TagEntity getTagByIdWithFetch(int id) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<TagEntity> cq = cb.createQuery(TagEntity.class);
//        Root<TagEntity> root = cq.from(TagEntity.class);
//        root.fetch(TagEntity_.images, JoinType.LEFT);

//        Predicate id_ = cb.equals(root.get(TagEntity_.id), id);

//        cq.where(id_).select(root);

//        return entityManager.createQuery(cq).getSingleResult();
        TagEntity result = entityManager.find(TagEntity.class, id);
        return result;
    }

    public TagEntity getTagByName(String name) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagEntity> cq = cb.createQuery(TagEntity.class);
        Root<TagEntity> root = cq.from(TagEntity.class);
        root.fetch(TagEntity_.images, JoinType.INNER);
        String nameSearchTerm = (name == null) ? "%" : ("%" + name.toLowerCase() + "%");
        Predicate name_ = cb.like(cb.lower(root.get(TagEntity_.name)), nameSearchTerm);

        cq.where(name_).select(root);

        try{
            return entityManager.createQuery(cq).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
}
