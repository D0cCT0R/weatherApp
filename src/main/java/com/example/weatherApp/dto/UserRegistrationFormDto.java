package com.example.weatherApp.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationFormDto(
        @NotBlank(message = "Please enter login")
        String login,
        @NotBlank(message = "Please enter password")
        @Min(value = 3, message = "The minimum password length must be 3 characters")
        String password
) {
}

