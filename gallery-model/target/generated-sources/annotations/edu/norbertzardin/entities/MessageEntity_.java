package edu.norbertzardin.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MessageEntity.class)
public abstract class MessageEntity_ {

	public static volatile SingularAttribute<MessageEntity, Date> createdDate;
	public static volatile SingularAttribute<MessageEntity, Integer> id;
	public static volatile SingularAttribute<MessageEntity, String> message;

}

