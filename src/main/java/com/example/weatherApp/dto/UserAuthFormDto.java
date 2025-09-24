package com.example.weatherApp.dto;


import jakarta.validation.constraints.NotBlank;

public record UserAuthFormDto(
        @NotBlank(message = "Please enter login")
        String login,
        @NotBlank(message = "Please enter password")
        String password
) {
}
