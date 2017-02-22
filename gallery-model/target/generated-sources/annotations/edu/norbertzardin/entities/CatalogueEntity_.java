package edu.norbertzardin.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CatalogueEntity.class)
public abstract class CatalogueEntity_ {

	public static volatile ListAttribute<CatalogueEntity, ImageEntity> images;
	public static volatile SingularAttribute<CatalogueEntity, Date> createdDate;
	public static volatile SingularAttribute<CatalogueEntity, Integer> id;
	public static volatile SingularAttribute<CatalogueEntity, String> title;

}

