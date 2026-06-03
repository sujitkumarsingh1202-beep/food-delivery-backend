package com.app.fooddelivery.service;

import com.app.fooddelivery.dto.request.LocationRequest;
import com.app.fooddelivery.entity.AppUser;
import com.app.fooddelivery.entity.Location;
import com.app.fooddelivery.exception.ResourceNotFoundException;
import com.app.fooddelivery.repository.LocationRepository;
import com.app.fooddelivery.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    public LocationService(LocationRepository locationRepository, UserRepository userRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
    }

    public Location saveLocation(Long userId, LocationRequest request) {
        Location location = locationRepository.findByUser_Id(userId).orElse(new Location());
        
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        location.setUser(user);
        
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setAddress(request.getAddress());
        return locationRepository.save(location);
    }

    public Optional<Location> getLocationByUserId(Long userId) {
        return locationRepository.findByUser_Id(userId);
    }
}