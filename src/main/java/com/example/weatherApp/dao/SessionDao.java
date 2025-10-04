package com.example.weatherApp.dao;


import com.example.weatherApp.entity.Session;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class SessionDao {
    private final SessionFactory sessionFactory;
    @Autowired
    public SessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Session save(Session session) throws DatabaseIsNotAvailableException {
        log.debug("Executing session save for user: {}", session.getUser().getLogin());
        try {
            sessionFactory.getCurrentSession().persist(session);
            sessionFactory.getCurrentSession().flush();
            log.debug("Executing session save for user: {} complete succesfully", session.getUser().getLogin());
            return session;
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }

    public Optional<Session> getSessionByUUID(UUID uuid) throws DatabaseIsNotAvailableException {
        log.debug("Executing session get for uuid: {}", uuid);
        try{
            Optional<Session> session = Optional.ofNullable(sessionFactory.getCurrentSession().get(Session.class, uuid));
            log.debug("Executing session get for uuid: {} complete successfully", uuid);
            return session;
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }

    public void deleteSession(Session session) throws DatabaseIsNotAvailableException {
        String userLogin = session.getUser().getLogin();
        log.debug("Executing session delete for user: {}", userLogin);
        try {
            sessionFactory.getCurrentSession().remove(session);
            sessionFactory.getCurrentSession().flush();
            log.debug("Executing session delete for user: {} complete succesfully", userLogin);
         } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }

    public int deleteExpiredSessions(OffsetDateTime now) {
        return sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM Session s WHERE s.expiresTime < :now")
                .setParameter("now", now)
                .executeUpdate();
    }
}
