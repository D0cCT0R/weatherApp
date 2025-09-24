package com.example.weatherApp.exceptions;

import lombok.Getter;

@Getter
public class WeatherApiException extends Exception {
    private int statusCode;
    public WeatherApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
