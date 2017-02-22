package edu.norbertzardin.dao.impl;

import edu.norbertzardin.dao.MessageDao;
import edu.norbertzardin.entities.MessageEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.*;
import java.util.List;

@Repository
public class MessageDAOImpl implements MessageDao {

//    @PersistenceContext(name = "messagePersistence")
    private EntityManager entityManager;

    public void createMessage(MessageEntity message) {
        entityManager.persist(message);
    }

    public List<MessageEntity> getMessageList() {
        List<MessageEntity> result = entityManager.createQuery("from MessageEntity ", MessageEntity.class).getResultList();

        return result;
    }
    @PersistenceContext
    public void setEntityManagerFactory(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }
}