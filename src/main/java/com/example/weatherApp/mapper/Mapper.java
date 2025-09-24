package com.example.weatherApp.mapper;


import com.example.weatherApp.dto.UserDto;
import com.example.weatherApp.entity.User;

public class Mapper {
    public static UserDto convertUserToDto(User user) {
        return new UserDto(user.getId(), user.getLogin());
    }
}
