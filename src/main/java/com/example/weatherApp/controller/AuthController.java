package com.example.weatherApp.controller;


import com.example.weatherApp.dto.AuthResponseDto;
import com.example.weatherApp.dto.UserAuthFormDto;
import com.example.weatherApp.dto.UserDto;
import com.example.weatherApp.exceptions.AuthenticationFailedException;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.service.facade.AuthFacadeService;
import com.example.weatherApp.util.CookieUtil;
import com.example.weatherApp.validation.UUIDValidator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Optional;
import java.util.UUID;


@Controller
@Slf4j
public class AuthController {

    private final AuthFacadeService authFacadeService;

    @Autowired
    public AuthController(AuthFacadeService authFacadeService) {
        this.authFacadeService = authFacadeService;
    }

    @GetMapping("/sign-in")
    public String showSignInPage() {
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String loginUser(@Valid @ModelAttribute UserAuthFormDto dto, BindingResult bindingResult, Model model, HttpServletResponse response) throws DatabaseIsNotAvailableException, AuthenticationFailedException {
        log.info("Login request has been received. User: {}", dto.login());
        if(bindingResult.hasErrors()) {
            log.warn("Validation failed for user {}: {}", bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("error", bindingResult.getFieldError().getDefaultMessage());
            return "sign-in";
        }
        AuthResponseDto authResponse = authFacadeService.authUser(dto.login(), dto.password());
        Cookie cookie = CookieUtil.createSessionCookie(authResponse.sessionUUID().toString());
        response.addCookie(cookie);
        log.info("Request login processed successfully Login: {}, UserId: {}", authResponse.userLogin(), authResponse.userId());
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(@RequestAttribute("user") UserDto user, HttpServletResponse response, HttpServletRequest request) throws DatabaseIsNotAvailableException {
        log.info("Logout request has been received. UserId: {}", user.id());
        Optional<Cookie> cookie = CookieUtil.getSessionTokenFromRequest(request);
        if (cookie.isPresent()) {
            String uuid = cookie.get().getValue();
            if (!UUIDValidator.isValidUuid(uuid)) {
                log.warn("Security violation: invalid uuid from user: {}", user.id());
                Cookie deleteCookie = CookieUtil.deleteSessionCookie();
                response.addCookie(deleteCookie);
                return "redirect:/";
            }
            authFacadeService.logout(UUID.fromString(uuid), user.login());
        }
        Cookie deleteCookie = CookieUtil.deleteSessionCookie();
        response.addCookie(deleteCookie);
        log.info("Request logout processed successfully UserId: {}", user.id());
        return "redirect:/";
    }
}
