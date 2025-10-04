package com.example.weatherApp.service.facade;


import com.example.weatherApp.dto.AddLocationDto;
import com.example.weatherApp.dto.WeatherDto;
import com.example.weatherApp.entity.Location;
import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.SecurityException;
import com.example.weatherApp.service.core.LocationService;
import com.example.weatherApp.service.core.UserService;
import com.example.weatherApp.service.core.WeatherService;
import com.example.weatherApp.validation.CoordinatesValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserWeatherFacadeService {

    private final LocationService locationService;
    private final WeatherService weatherService;
    private final UserService userService;

    @Autowired
    public UserWeatherFacadeService(LocationService locationService, WeatherService weatherService, UserService userService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.userService = userService;
    }


    public List<WeatherDto> getUserLocationsWithWeather(Long userId) throws Exception {
        List<Location> userLocations = locationService.getAllUserLocations(userId);
        if (!userLocations.isEmpty()) {
            log.info("Starting search weather for user: {}", userId);
            ArrayList<WeatherDto> userWeatherList = new ArrayList<>();
            for (Location loc : userLocations) {
                userWeatherList.add(weatherService.getWeatherByCity(loc.getLatitude(), loc.getLongitude()));
            }
            log.info("Search weather for user {} complete succesfully", userId);
            return userWeatherList;
        }
        return new ArrayList<>();
    }

    public void addUserLocation(Long userId, AddLocationDto addLocationDto) throws DatabaseIsNotAvailableException, SecurityException {
        try {
            log.info("Starting add user location. UserId {}, lat {}, lon {}", userId, addLocationDto.lat(), addLocationDto.lon());
            double lat = Double.parseDouble(addLocationDto.lat());
            double lon = Double.parseDouble(addLocationDto.lon());
            String locationName = addLocationDto.name();
            if (!CoordinatesValidator.isValidCoordinates(lat, lon)) {
                log.warn("Security violation(validator): invalid coordinates from user {}: {}, {}",
                        userId, addLocationDto.lat(), addLocationDto.lon());
                throw new SecurityException("Invalid coordinates");
            }
            User user = userService.getUserById(userId);
            locationService.addLocation(user, locationName, lat, lon);
            log.info("Add user location complete succesfully. UserId {}, lat {}, lon {}", userId, addLocationDto.lat(), addLocationDto.lon());
        } catch (NumberFormatException e) {
            log.warn("Security violation(number format): invalid coordinates from user {}: {}, {}",
                    userId, addLocationDto.lat(), addLocationDto.lon());
            throw new SecurityException("Invalid coordinate format");
        }
    }

    public void deleteUserLocation(Long userId, String lat, String lon) throws DatabaseIsNotAvailableException, SecurityException {
        try {
            log.info("Starting delete user location. UserId {}, lat {}, lon {}", userId, lat, lon);
            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);
            if (!CoordinatesValidator.isValidCoordinates(latitude, longitude)) {
                log.warn("Security violation(validator): invalid coordinates from user {}: {}, {}",
                        userId, latitude, longitude);
                throw new SecurityException("Invalid coordinates");
            }
            Location location = locationService.getLocation(userId, latitude, longitude).orElseThrow(() -> {
                log.warn("Security violation: user {} tried to delete non-existent location at {}, {}",
                        userId, latitude, longitude);
                return new SecurityException("Invalid coordinates");
            });
            locationService.deleteLocation(location);
            log.info("User {} deleted location: lat {} lon {}", userId, lat, lon);
        } catch (NumberFormatException e) {
            log.warn("Security violation(number format): invalid coordinates from user {}: {}, {}",
                    userId, lat, lon);
            throw new SecurityException("Invalid coordinates");
        }
    }
}
