package com.example.weatherApp.dto;

import jakarta.validation.constraints.NotBlank;

public record SearchDto (
        @NotBlank(message = "Please enter the city name") String query
){}
