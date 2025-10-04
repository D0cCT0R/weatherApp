package com.example.weatherApp.controller;


import com.example.weatherApp.dto.RegistrationResponseDto;
import com.example.weatherApp.dto.UserRegistrationFormDto;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.DuplicateUserException;
import com.example.weatherApp.service.facade.RegistrationFacadeService;
import com.example.weatherApp.util.CookieUtil;
import jakarta.servlet.http.Cookie;
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

@Controller
@Slf4j
public class RegistrationController {

    private final RegistrationFacadeService registrationFacadeService;


    @Autowired
    public RegistrationController(RegistrationFacadeService registrationFacadeService) {
        this.registrationFacadeService = registrationFacadeService;
    }

    @GetMapping("/sign-up")
    public String showRegistrationForm() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String registerUser(@ModelAttribute @Valid UserRegistrationFormDto registrationDto, BindingResult bindingResult, HttpServletResponse response, Model model) throws DatabaseIsNotAvailableException, DuplicateUserException {
        log.info("Registration request has been received. User login: {}", registrationDto.login());
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getFieldError().getDefaultMessage());
            return "sign-up";
        }
        RegistrationResponseDto dto = registrationFacadeService.registerUser(registrationDto.login(), registrationDto.password());
        Cookie cookie = CookieUtil.createSessionCookie(dto.sessionUUID().toString());
        response.addCookie(cookie);
        log.info("Registration request processed successfully, User Login: {}, UserId: {}", registrationDto.login(), dto.userId());
        return "redirect:/";
    }
}
