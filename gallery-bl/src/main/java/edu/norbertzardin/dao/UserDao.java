package edu.norbertzardin.dao;

import edu.norbertzardin.entities.UserEntity;
import edu.norbertzardin.entities.UserRole;

public interface UserDao{
    UserEntity findByUserName(String username);

    void create(UserEntity user);

    void create(UserRole userRole);
}
