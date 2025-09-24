package com.example.weatherApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherDto(
     String name,
     Main main,
     Coord coord
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Main (int temp) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Coord (double lat, double lon) {}
}
