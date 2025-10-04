package com.example.weatherApp.dto;

import jakarta.validation.constraints.NotBlank;

public record DeleteLocationDto (
        @NotBlank String name,
        @NotBlank String lat,
        @NotBlank String lon
) {
}

