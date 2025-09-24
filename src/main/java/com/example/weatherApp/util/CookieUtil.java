package com.example.weatherApp.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

@UtilityClass
public class CookieUtil {
    public static final String COOKIE_SESSION_NAME = "SESSIONID";

    public static Cookie createSessionCookie(String uuid) {
        Cookie cookie = new Cookie(COOKIE_SESSION_NAME, uuid);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        return cookie;
    }
    public static Cookie deleteSessionCookie() {
        Cookie cookie = new Cookie(COOKIE_SESSION_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public Optional<Cookie> getSessionTokenFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(WebUtils.getCookie(request, COOKIE_SESSION_NAME));
    }

}
