package com.example.weatherApp.validation;



public class CoordinatesValidator {
    public static boolean isValidCoordinates(Double lat, Double lon) {
        return lat != null && lon != null &&
                lat >= -90 && lat <= 90 &&
                lon >= -180 && lon <= 180;
    }
}

