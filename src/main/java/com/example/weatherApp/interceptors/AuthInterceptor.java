package com.example.weatherApp.interceptors;

import com.example.weatherApp.dto.UserDto;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.service.facade.SessionValidationService;
import com.example.weatherApp.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionValidationService sessionValidationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws DatabaseIsNotAvailableException {
        Optional<Cookie> userCookie = CookieUtil.getSessionTokenFromRequest(request);
        if(userCookie.isPresent()) {
            String cookieUuid = userCookie.get().getValue();
            log.debug("Processing session validation for UUID: {}", cookieUuid);
            Optional<UserDto> userDto = sessionValidationService.sessionValidator(cookieUuid);
            if(userDto.isPresent()) {
                request.setAttribute("user", userDto.get());
                log.debug("User authenticated: {}", userDto.get().login());
            } else {
                log.debug("Invalid session, deleting cookie: {}", cookieUuid);
                response.addCookie(CookieUtil.deleteSessionCookie());
            }
        }
        return true;
    }
}

