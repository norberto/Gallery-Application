package edu.norbertzardin.service;

import edu.norbertzardin.dao.UserDao;
import edu.norbertzardin.entities.UserEntity;
import edu.norbertzardin.entities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public Integer register(String username, String password) {
        if (username == null || username.equals("") ||
                password == null || password.equals("")) {
            return 101;
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        userDao.create(user);

        UserRole ur = new UserRole();
        ur.setRole("ROLE_USER");
        ur.setUser(user);
        userDao.create(ur);

        return 200;
    }


}
