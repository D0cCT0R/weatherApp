package com.example.weatherApp.interceptors;


import com.example.weatherApp.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import java.io.IOException;


@Component
public class PublicPagesInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Cookie cookie = WebUtils.getCookie(request, CookieUtil.COOKIE_SESSION_NAME);
        return cookie == null;
    }
}
