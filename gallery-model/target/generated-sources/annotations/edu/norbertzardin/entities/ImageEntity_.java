package edu.norbertzardin.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ImageEntity.class)
public abstract class ImageEntity_ {

	public static volatile SingularAttribute<ImageEntity, Date> createdDate;
	public static volatile SingularAttribute<ImageEntity, byte[]> imageData;
	public static volatile SingularAttribute<ImageEntity, String> name;
	public static volatile SingularAttribute<ImageEntity, String> description;
	public static volatile SingularAttribute<ImageEntity, Integer> id;
	public static volatile SingularAttribute<ImageEntity, CatalogueEntity> catalogue;
	public static volatile ListAttribute<ImageEntity, TagEntity> tags;

}

