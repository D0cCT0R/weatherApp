package com.example.weatherApp.service.facade;

import com.example.weatherApp.service.core.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SessionCleanupScheduler {
    private final SessionService sessionService;

    public SessionCleanupScheduler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Scheduled(fixedRate = 3600000)
    public void cleanupSessions() {
        try {
            int count = sessionService.deleteExpiredSession();
            log.info("Deleted {} expired sessions", count);
        } catch (Exception e) {
            log.error("Session cleanup failed", e);
        }
    }
}

