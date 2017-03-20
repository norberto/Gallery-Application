package edu.norbertzardin.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserEntity.class)
public abstract class UserEntity_ {

	public static volatile SingularAttribute<UserEntity, String> password;
	public static volatile SetAttribute<UserEntity, UserRole> userRole;
	public static volatile SingularAttribute<UserEntity, Boolean> enabled;
	public static volatile SingularAttribute<UserEntity, String> username;

}

