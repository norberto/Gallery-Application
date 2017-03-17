package edu.norbertzardin.dao;

import edu.norbertzardin.entities.UserEntity;

public interface UserDao {
    UserEntity findByUserName(String username);
}
