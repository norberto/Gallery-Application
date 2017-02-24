package edu.norbertzardin.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TagEntity.class)
public abstract class TagEntity_ {

	public static volatile ListAttribute<TagEntity, ImageEntity> images;
	public static volatile SingularAttribute<TagEntity, Date> createdDate;
	public static volatile SingularAttribute<TagEntity, String> name;
	public static volatile SingularAttribute<TagEntity, Integer> id;

}

