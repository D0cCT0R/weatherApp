package com.example.weatherApp.dto;




import java.time.OffsetDateTime;
import java.util.UUID;


public record SessionDto (
        UUID uuid,
        UserDto user,
        OffsetDateTime expiresAt
){}

