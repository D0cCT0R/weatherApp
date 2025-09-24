package com.example.weatherApp.service.facade;

import com.example.weatherApp.dto.AuthResponseDto;
import com.example.weatherApp.entity.Session;
import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.AuthenticationFailedException;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.service.core.SessionService;
import com.example.weatherApp.service.core.UserService;
import com.example.weatherApp.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthFacadeService {
    private final UserService userService;
    private final SessionService sessionService;


    @Autowired
    public AuthFacadeService(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    public AuthResponseDto authUser(String login, String password) throws DatabaseIsNotAvailableException, AuthenticationFailedException {
        log.info("Starting user auth for login: {}", login);
        User user = userService.getUserByLogin(login).orElseThrow(() -> {
            log.warn("User not found. Login: {}", login);
            return new AuthenticationFailedException("Incorrect login or password");
        });
        if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
            log.warn("Incorrect password user: {}", login);
            throw new AuthenticationFailedException("Incorrect login or password");
        }
        Session session = sessionService.saveSession(user);
        log.info("User auth successfully: {}", login);
        return new AuthResponseDto(user.getId(), user.getLogin(), session.getUuid());
    }

    public void logout(UUID uuid, String login) throws DatabaseIsNotAvailableException {
        log.info("Starting user logout for login: {}", login);
        Optional<Session> session = sessionService.getSessionByUUID(uuid);
        if(session.isPresent()) {
            sessionService.deleteSession(session.get());
        }
        log.info("User logout for login: {} complete successfully", login);
    }
}
