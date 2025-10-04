package com.example.weatherApp.dto;



import java.util.UUID;


public record AuthResponseDto(
        Long userId,
        String userLogin,
        UUID sessionUUID
) {}

