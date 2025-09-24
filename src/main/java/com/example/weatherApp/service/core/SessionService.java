package com.example.weatherApp.service.core;


import com.example.weatherApp.dao.SessionDao;
import com.example.weatherApp.entity.Session;
import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SessionService {
    private final SessionDao sessionDao;
    private static final int SESSION_DURATION_HOURS = 1;

    @Autowired
    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Transactional
    public Session saveSession(User user) throws DatabaseIsNotAvailableException {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresTime(OffsetDateTime.now().plusHours(SESSION_DURATION_HOURS));
        return sessionDao.save(session);
    }

    @Transactional
    public Optional<Session> getSessionByUUID(UUID uuid) throws DatabaseIsNotAvailableException {
        return sessionDao.getSessionByUUID(uuid);
    }

    @Transactional
    public int deleteExpiredSession() {
        return sessionDao.deleteExpiredSessions(OffsetDateTime.now());
    }

    @Transactional
    public void deleteSession(Session session) throws DatabaseIsNotAvailableException {
        sessionDao.deleteSession(session);
    }
}
