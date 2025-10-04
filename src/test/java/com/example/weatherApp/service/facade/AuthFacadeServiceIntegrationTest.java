package com.example.weatherApp.service.facade;


import com.example.weatherApp.config.root.persistence.TestDataConfig;
import com.example.weatherApp.dao.SessionDao;
import com.example.weatherApp.dao.UserDao;
import com.example.weatherApp.dto.AuthResponseDto;
import com.example.weatherApp.entity.Session;
import com.example.weatherApp.exceptions.AuthenticationFailedException;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.DuplicateUserException;
import com.example.weatherApp.service.core.SessionService;
import com.example.weatherApp.service.core.UserService;
import com.example.weatherApp.util.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestDataConfig.class, AuthFacadeService.class, UserService.class, SessionService.class, UserDao.class, SessionDao.class})
@ActiveProfiles("test")
public class AuthFacadeServiceIntegrationTest {
    @Autowired
    private AuthFacadeService authFacadeService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;
    private final String login = "login@Gmail.com";
    private final String secondLogin = "log@Gmail.com";
    private final String password = PasswordUtil.hashPassword("123");


    @Test
    void tryToAuthUserLoginNotFound() {
        assertThrows(AuthenticationFailedException.class, () -> {
            authFacadeService.authUser(login, password);
        });
    }

    @Test
    void tryToAuthUserPasswordIncorrect() throws DatabaseIsNotAvailableException, DuplicateUserException {
        String userInput = "1234";
        userService.saveUser(login, password);
        assertThrows(AuthenticationFailedException.class, () -> {
            authFacadeService.authUser(login, userInput);
        });
    }

    @Test
    void userAuthSuccesfully() throws DatabaseIsNotAvailableException, DuplicateUserException, AuthenticationFailedException {
        String userInput = "123";
        userService.saveUser(secondLogin, password);
        AuthResponseDto authResponseDto = authFacadeService.authUser(secondLogin, userInput);
        Optional<Session> session = sessionService.getSessionByUUID(authResponseDto.sessionUUID());
        assertTrue(session.isPresent());
        assertNotNull(authResponseDto);
        assertEquals(session.get().getUser().getId(), authResponseDto.userId());
        assertEquals(session.get().getUser().getLogin(), authResponseDto.userLogin());
        assertEquals(session.get().getUuid(), authResponseDto.sessionUUID());
    }
}
