package edu.norbertzardin.service;

import edu.norbertzardin.dao.UserDao;
import edu.norbertzardin.entities.UserEntity;
import edu.norbertzardin.entities.UserRole;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Integer register(String username, String password) {
        if (username == null || "".equals(username) ||
                password == null || "".equals(password)) {
            return 101;
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        try {
            userDao.create(user);
        } catch (ConstraintViolationException | JpaSystemException e) {
            return 102;
        }
        UserRole ur = new UserRole();
        ur.setRole("ROLE_USER");
        ur.setUser(user);
        userDao.create(ur);

        return 200;
    }


}
