package com.example.weatherApp.dao;


import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.DuplicateUserException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public User save(User user) throws DuplicateUserException, DatabaseIsNotAvailableException {
        try {
            log.debug("Executing save User {} ", user.getLogin());
            sessionFactory.getCurrentSession().persist(user);
            sessionFactory.getCurrentSession().flush();
            log.debug("Executing User save complete succesfully. User userId {}", user.getId());
            return user;
        } catch (ConstraintViolationException e) {
            log.warn("Duplicate user attempt. Login: {}, Constraint: {}",
                    user.getLogin(), e.getConstraintName(), e);
            throw new DuplicateUserException("User login already exists");
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }

    public Optional<User> getByLogin(String login) throws DatabaseIsNotAvailableException {
        try {
            log.debug("Executing find User by login {}", login);
            return Optional.ofNullable(sessionFactory.getCurrentSession()
                    .createQuery("from User where login = :login", User.class).setParameter("login", login).uniqueResult());
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }

    public User getById(Long userId) throws DatabaseIsNotAvailableException {
        try {
            log.debug("Executing find User by userId {}", userId);
            return sessionFactory.getCurrentSession()
                    .get(User.class, userId);
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }
}
