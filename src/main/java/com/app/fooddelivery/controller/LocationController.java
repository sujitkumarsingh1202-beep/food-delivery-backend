package com.app.fooddelivery.controller;

import com.app.fooddelivery.dto.request.LocationRequest;
import com.app.fooddelivery.dto.response.LocationResponse;
import com.app.fooddelivery.entity.Location;
import com.app.fooddelivery.service.LocationService;
import com.app.fooddelivery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;
    private final UserService userService;

    public LocationController(LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<LocationResponse> saveLocation(@Valid @RequestBody LocationRequest request, Authentication authentication) {
        Long userId = userService.getUserByUsername(authentication.getName()).getId();
        Location location = locationService.saveLocation(userId, request);
        return ResponseEntity.ok(new LocationResponse(location.getId(), location.getUserId(), location.getLatitude(), location.getLongitude(), location.getAddress()));
    }

    @PutMapping
    public ResponseEntity<LocationResponse> updateLocation(@Valid @RequestBody LocationRequest request, Authentication authentication) {
        Long userId = userService.getUserByUsername(authentication.getName()).getId();
        Location location = locationService.saveLocation(userId, request); // same logic handles update
        return ResponseEntity.ok(new LocationResponse(location.getId(), location.getUserId(), location.getLatitude(), location.getLongitude(), location.getAddress()));
    }

    @GetMapping("/me")
    public ResponseEntity<LocationResponse> getMyLocation(Authentication authentication) {
        Long userId = userService.getUserByUsername(authentication.getName()).getId();
        return locationService.getLocationByUserId(userId)
                .map(location -> ResponseEntity.ok(new LocationResponse(location.getId(), location.getUserId(), location.getLatitude(), location.getLongitude(), location.getAddress())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
