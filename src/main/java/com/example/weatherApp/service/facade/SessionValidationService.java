package com.example.weatherApp.service.facade;

import com.example.weatherApp.dto.UserDto;
import com.example.weatherApp.entity.Session;
import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.mapper.Mapper;
import com.example.weatherApp.service.core.SessionService;
import com.example.weatherApp.validation.UUIDValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SessionValidationService {
    private final SessionService sessionService;

    public SessionValidationService(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    public Optional<UserDto> sessionValidator(String uuid) throws DatabaseIsNotAvailableException {
        if (!UUIDValidator.isValidUuid(uuid)) {
            return Optional.empty();
        }
        UUID sessionId = UUID.fromString(uuid);
        Optional<Session> session = sessionService.getSessionByUUID(sessionId);
        if (session.isEmpty()) {
            return Optional.empty();
        }
        Session currentSession = session.get();
        if (isSessionExpired(currentSession)) {
            sessionService.deleteSession(currentSession);
        }
        User user = currentSession.getUser();
        return Optional.of(Mapper.convertUserToDto(user));
    }

    public boolean isSessionExpired(Session session) {
        return session.getExpiresTime().isBefore(OffsetDateTime.now());
    }
}
