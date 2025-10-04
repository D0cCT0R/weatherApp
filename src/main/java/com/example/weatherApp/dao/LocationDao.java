package com.example.weatherApp.dao;


import com.example.weatherApp.entity.Location;
import com.example.weatherApp.exceptions.DatabaseIsNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Slf4j
public class LocationDao {

    private final SessionFactory sessionFactory;

    public LocationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public List<Location> getUserLocations(Long userId) throws DatabaseIsNotAvailableException {
        try {
            log.debug("Executing get all User locations by user userId {}", userId);
            return sessionFactory.getCurrentSession().createQuery("from Location where user.id=:userId", Location.class).setParameter("userId", userId).getResultList();
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }

    public void saveLocation(Location location) throws DatabaseIsNotAvailableException {
        try {
            log.debug("Executing save Location. Lat {}, Lon {}, User userId {}", location.getLatitude(), location.getLongitude(), location.getUser().getId());
            sessionFactory.getCurrentSession().persist(location);
            sessionFactory.getCurrentSession().flush();
            log.debug("Executing save Location complete succesfully. Location userId: {}", location.getId());
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }

    public void deleteLocation(Location location) throws DatabaseIsNotAvailableException {
        try {
            log.debug("Executing delete Location. Id: {}", location.getId());
            sessionFactory.getCurrentSession().remove(location);
            sessionFactory.getCurrentSession().flush();
            log.debug("Executing delete Location complete succesfully");
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }

    public Optional<Location> getUserLocation(Long userId, double lat, double lon) throws DatabaseIsNotAvailableException {
        try {
            log.debug("Executing get all User locations. User userId: {}", userId);
            return Optional.ofNullable(sessionFactory.getCurrentSession().createQuery("from Location where user.id = :userId and latitude = :lat and longitude = :lon", Location.class).setParameter("userId", userId)
                    .setParameter("lat", lat)
                    .setParameter("lon", lon)
                    .setMaxResults(1)
                    .uniqueResult());
        } catch (HibernateException e) {
            log.error("DB error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Database is not available");
        } catch (Exception e) {
            log.error("Unknown error: {}", e.getMessage(), e);
            throw new DatabaseIsNotAvailableException("Unknown error");
        }
    }
}
