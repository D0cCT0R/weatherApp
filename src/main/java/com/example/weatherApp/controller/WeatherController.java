package com.example.weatherApp.controller;


import com.example.weatherApp.dto.*;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.SecurityException;
import com.example.weatherApp.exceptions.WeatherApiException;
import com.example.weatherApp.service.facade.SearchFacadeService;
import com.example.weatherApp.service.facade.UserWeatherFacadeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/weather")
@Slf4j
public class WeatherController {
    private final SearchFacadeService searchFacadeService;

    private final UserWeatherFacadeService userWeatherFacadeService;

    @Autowired
    public WeatherController(SearchFacadeService searchFacadeService, UserWeatherFacadeService userWeatherFacadeService) {
        this.searchFacadeService = searchFacadeService;
        this.userWeatherFacadeService = userWeatherFacadeService;
    }

    @GetMapping("/search")
    public String searchWeather(@Valid @ModelAttribute SearchDto searchDto,
                                BindingResult bindingResult,
                                @RequestAttribute(name = "user", required = false) UserDto user,
                                Model model) throws WeatherApiException {
        log.info("Search request has been received query: {}, userId: {}", searchDto.query(), user.id());
        if (bindingResult.hasErrors()) {
            model.addAttribute("searchError", bindingResult.getFieldError().getDefaultMessage());
            return "home";
        }
        List<WeatherDto> weatherList = searchFacadeService.searchWeatherByQuery(searchDto.query());
        model.addAttribute("weatherList", weatherList);
        model.addAttribute("user", user);
        log.info("Search request processed successfully query: {}, userId: {}", searchDto.query(), user.id());
        return "search-results";
    }

    @PostMapping("/favorites")
    public String addFavoriteLocation(@Valid @ModelAttribute AddLocationDto addLocationDto,
                                      BindingResult bindingResult,
                                      @RequestAttribute("user") UserDto userDto) throws DatabaseIsNotAvailableException, SecurityException {
        log.info("Add favorite request has been received userId: {}", userDto.id());
        if (bindingResult.hasErrors()) {
            log.warn("Invalid form data from user {}: {}", userDto.id(), bindingResult.getFieldErrors());
            return "redirect:/";
        }
        userWeatherFacadeService.addUserLocation(userDto.id(), addLocationDto);
        log.info("Add favorite request processed successfully userId: {}", userDto.id());
        return "redirect:/";
    }

    @PostMapping("/delete-favorite")
    public String deleteFavoriteLocation(@Valid @ModelAttribute DeleteLocationDto dto, BindingResult bindingResult, @RequestAttribute("user") UserDto userDto) throws SecurityException, DatabaseIsNotAvailableException {
        log.info("Delete favorite request has been received userId: {}", userDto.id());
        if (bindingResult.hasErrors()) {
            log.warn("Invalid form data from user {}: {}", userDto.id(), bindingResult.getFieldErrors());
            return "redirect:/";
        }
        userWeatherFacadeService.deleteUserLocation(userDto.id(), dto.lat(), dto.lon());
        log.info("Delete favorite request processed successfully userId: {}", userDto.id());
        return "redirect:/";
    }

}
