package com.example.weatherApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherGeocodingDto (
        double lat,
        double lon
){}
