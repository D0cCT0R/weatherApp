package com.example.weatherApp.service.facade;


import com.example.weatherApp.dto.RegistrationResponseDto;
import com.example.weatherApp.entity.Session;
import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.DuplicateUserException;
import com.example.weatherApp.service.core.SessionService;
import com.example.weatherApp.service.core.UserService;
import com.example.weatherApp.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegistrationFacadeService {

    private final UserService userService;
    private final SessionService sessionService;

    @Autowired
    public RegistrationFacadeService(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    public RegistrationResponseDto registerUser(String login, String password) throws DuplicateUserException, DatabaseIsNotAvailableException {
        log.info("Starting user registration for login: {}", login);
        String hashPassword = PasswordUtil.hashPassword(password);
        User savedUser = userService.saveUser(login, hashPassword);
        log.debug("Processing registration user. User with login {} succesfully saved", savedUser.getLogin());
        Session savedSession = sessionService.saveSession(savedUser);
        log.info("User registration successfully User: {}", login);
        return new RegistrationResponseDto(savedUser.getId(), savedUser.getLogin(), savedSession.getUuid());
    }
}
