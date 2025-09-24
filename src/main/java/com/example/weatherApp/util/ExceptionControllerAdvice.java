package com.example.weatherApp.util;

import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.DuplicateUserException;
import com.example.weatherApp.exceptions.AuthenticationFailedException;
import com.example.weatherApp.exceptions.WeatherApiException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(DuplicateUserException.class)
    public String handleDuplicateUser(DuplicateUserException e, Model model, HttpServletResponse response) {
        model.addAttribute("error",e.getMessage());
        response.setStatus(HttpStatus.CONFLICT.value());
        return "sign-up";
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public String handleNotFoundUser(AuthenticationFailedException ex, Model model, HttpServletResponse response){
        model.addAttribute("error", ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "sign-in";
    }

    @ExceptionHandler(WeatherApiException.class)
    public String handleWeatherApiException(WeatherApiException ex, Model model, HttpServletResponse response) {
        model.addAttribute("error", ex.getMessage());
        response.setStatus(ex.getStatusCode());
        return "error-page";
    }

    @ExceptionHandler(SecurityException.class)
    public String handleSecurityException(SecurityException ex, Model model, HttpServletResponse response) {
        model.addAttribute("error", ex.getMessage());
        response.setStatus(HttpStatus.CONFLICT.value());
        return "error-page";
    }

    @ExceptionHandler(DatabaseIsNotAvailableException.class)
    public String handleDatabaseException(DatabaseIsNotAvailableException e, Model model, HttpServletResponse response) {
        model.addAttribute("error", e.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error-page";
    }
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex, Model model, HttpServletResponse response) {
        model.addAttribute("error", "Session error. Please login again");
        return "error-page";
    }

}
