package com.example.weatherApp.controller;


import com.example.weatherApp.dto.UserDto;
import com.example.weatherApp.dto.WeatherDto;
import com.example.weatherApp.service.facade.UserWeatherFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.List;


@Controller
@Slf4j
public class HomeController {

    private final UserWeatherFacadeService userWeatherFacadeService;

    @Autowired
    public HomeController(UserWeatherFacadeService userWeatherFacadeService) {
        this.userWeatherFacadeService = userWeatherFacadeService;
    }

    @GetMapping("/")
    public String showHomePage(@RequestAttribute(name = "user", required = false) UserDto user, Model model) throws Exception {
        if (user != null) {
            List<WeatherDto> weatherList = userWeatherFacadeService.getUserLocationsWithWeather(user.id());
            if(!weatherList.isEmpty()) {
                model.addAttribute("locations", weatherList);
            }
            model.addAttribute("login", user.login());
        }
        return "home";
    }
}
