package com.example.weatherApp.service.core;

import com.example.weatherApp.dao.UserDao;
import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.DuplicateUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public User saveUser(String login, String password) throws DuplicateUserException, DatabaseIsNotAvailableException {
        User user = User.builder()
                .login(login)
                .password(password)
                .build();
        return userDao.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByLogin(String login) throws DatabaseIsNotAvailableException {
        return userDao.getByLogin(login);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) throws DatabaseIsNotAvailableException {
        return userDao.getById(id);
    }
}
