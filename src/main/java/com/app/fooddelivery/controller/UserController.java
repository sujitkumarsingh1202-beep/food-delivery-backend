package com.app.fooddelivery.controller;

import com.app.fooddelivery.dto.response.UserResponse;
import com.app.fooddelivery.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
                        user.getRoles().stream().map(role -> role.getName()).toList()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        var user = userService.getUserById(id);
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
                user.getRoles().stream().map(role -> role.getName()).toList()));
    }
}
