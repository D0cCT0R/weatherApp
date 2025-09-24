package com.example.weatherApp.service.core;

import com.example.weatherApp.dto.WeatherDto;
import com.example.weatherApp.dto.WeatherGeocodingDto;
import com.example.weatherApp.exceptions.WeatherApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
@Slf4j
public class WeatherService {

    private final RestClient restClient;
    @Value("${weather.api.key}")
    private String apiKey;
    @Value("${weather.api.units}")
    private String units;
    private final int searchLimit = 4;
    @Autowired
    public WeatherService(RestClient restClient) {
        this.restClient = restClient;
    }

    public WeatherDto getWeatherByCity(double lat, double lon) throws WeatherApiException {
        log.debug("Weather request by coordinates: lat={}, lon={}", lat, lon);
        try {
            WeatherDto result = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("data/2.5/weather")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", apiKey)
                            .queryParam("units", units)
                            .build())
                    .retrieve()
                    .body(WeatherDto.class);
            log.debug("Successfully received weather data for coordinates: lat={}, lon={}", lat, lon);
            return result;
        } catch (HttpClientErrorException e) {
            log.warn("Weather API client error: {}", e.getStatusCode());
            throw new WeatherApiException("Weather service error: invalid parameters", e.getStatusCode().value());
        } catch (Exception e) {
            log.error("Weather service error for coordinates: lat={}, lon={}", lat, lon, e);
            throw new WeatherApiException("Weather service temporary unavailable", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public List<WeatherGeocodingDto> searchLocations(String query) throws WeatherApiException {
        log.debug("Search locations by request: {}", query);
        try {
            List<WeatherGeocodingDto> result = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("geo/1.0/direct")
                            .queryParam("q", query)
                            .queryParam("limit", searchLimit)
                            .queryParam("appid", apiKey)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<WeatherGeocodingDto>>() {
                    });
            assert result != null;
            log.info("Found {} locations for request: {}", result.size(), query);
            return result;
        } catch (HttpClientErrorException e) {
            log.warn("Weather API client error: {}", e.getStatusCode());
            throw new WeatherApiException("Weather service error: invalid parameters", e.getStatusCode().value());
        } catch (Exception e) {
            log.error("Weather service error for query: {}", query);
            throw new WeatherApiException("Weather service temporary unavailable", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

}
