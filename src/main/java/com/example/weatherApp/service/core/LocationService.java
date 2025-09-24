package com.example.weatherApp.service.core;

import com.example.weatherApp.dao.LocationDao;
import com.example.weatherApp.entity.Location;
import com.example.weatherApp.entity.User;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import com.example.weatherApp.exceptions.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LocationService {

    private final LocationDao locationDao;

    @Autowired
    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Transactional(readOnly = true)
    public List<Location> getAllUserLocations(Long userId) throws DatabaseIsNotAvailableException {
        return locationDao.getUserLocations(userId);
    }

    @Transactional
    public void addLocation(User user, String name, double lat, double lon) throws DatabaseIsNotAvailableException {
        Location location = Location.builder()
                .name(name)
                .user(user)
                .latitude(lat)
                .longitude(lon)
                .build();
        locationDao.saveLocation(location);
    }
    @Transactional(readOnly = true)
    public Optional<Location> getLocation(Long userId, double lat, double lon) throws DatabaseIsNotAvailableException {
        return locationDao.getUserLocation(userId, lat, lon);
    }

    @Transactional
    public void deleteLocation(Location location) throws DatabaseIsNotAvailableException {
        locationDao.deleteLocation(location);
    }


}
