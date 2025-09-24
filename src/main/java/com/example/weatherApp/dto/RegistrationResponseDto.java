package com.example.weatherApp.dto;

import java.util.UUID;

public record RegistrationResponseDto (
        Long userId,
        String userLogin,
        UUID sessionUUID
) {}
