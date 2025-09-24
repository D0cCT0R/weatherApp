package com.example.weatherApp.service.facade;


import com.example.weatherApp.dto.WeatherDto;
import com.example.weatherApp.dto.WeatherGeocodingDto;
import com.example.weatherApp.exceptions.WeatherApiException;
import com.example.weatherApp.service.core.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SearchFacadeService {

    private final WeatherService weatherService;

    @Autowired
    public SearchFacadeService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public List<WeatherDto> searchWeatherByQuery(String query) throws WeatherApiException {
        List<WeatherGeocodingDto> cityList = weatherService.searchLocations(query);
        List<WeatherDto> weatherList = new ArrayList<>();
        log.debug("Found {} cities for your request: {}", cityList.size(), query);
        for (WeatherGeocodingDto weatherGeocodingDto : cityList) {
            try {
                WeatherDto weatherCity = weatherService.getWeatherByCity(weatherGeocodingDto.lat(), weatherGeocodingDto.lon());
                weatherList.add(weatherCity);
                log.debug("Added weather for the city: {}, weather: {} C", weatherCity.name(), weatherCity.main().temp());
            } catch (Exception e) {
                log.warn("Unable to get weather for coordinates: {}, {}",
                        weatherGeocodingDto.lat(), weatherGeocodingDto.lon(), e);
            }
        }
        log.info("Request processed successfully: {}, {} results found", query, weatherList.size());
        return weatherList;
    }
}
