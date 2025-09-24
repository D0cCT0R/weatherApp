package com.example.weatherApp.service.facade;

import com.example.weatherApp.dto.AuthResponseDto;
import com.example.weatherApp.entity.Session;
import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.AuthenticationFailedException;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.service.core.SessionService;
import com.example.weatherApp.service.core.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthFacadeServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private SessionService sessionService;
    @InjectMocks
    private AuthFacadeService authFacadeService;
    private final String userLogin = "Login";
    private final String userPassword = "$2a$10$qtsDMfUyB1vaPzST4fctQuJq/1hGDtB6mvVFsVF1Q/4Rn7LaPyjxi";

    @Test
    void AuthFailedLoginNotFound() throws DatabaseIsNotAvailableException {
        when(userService.getUserByLogin(userLogin)).thenReturn(Optional.empty());
        assertThrows(AuthenticationFailedException.class, () -> authFacadeService.authUser(userLogin, userPassword));
        verify(userService).getUserByLogin(userLogin);
    }

    @Test
    void AuthFailedIncorrectPassword() throws DatabaseIsNotAvailableException {
        String userInput = "12";
        when(userService.getUserByLogin(userLogin)).thenReturn(Optional.of(new User(1L, userLogin, userPassword)));
        assertThrows(AuthenticationFailedException.class, () -> authFacadeService.authUser(userLogin, userInput));
        verify(userService).getUserByLogin(userLogin);
    }

    @Test
    void AuthSuccess() throws DatabaseIsNotAvailableException, AuthenticationFailedException {
        String userInput = "123";
        User user = new User(1L, userLogin, userPassword);
        UUID sessionUUID = UUID.randomUUID();
        when(userService.getUserByLogin(userLogin)).thenReturn(Optional.of(user));
        when(sessionService.saveSession(user)).thenReturn(new Session(sessionUUID, user, OffsetDateTime.now()));
        AuthResponseDto authResponseDto = authFacadeService.authUser(userLogin, userInput);
        assertNotNull(authResponseDto);
        assertEquals(1L, authResponseDto.userId());
        assertEquals(userLogin, authResponseDto.userLogin());
        assertEquals(sessionUUID, authResponseDto.sessionUUID());
    }
}
