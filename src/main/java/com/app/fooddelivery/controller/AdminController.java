package com.app.fooddelivery.controller;

import com.app.fooddelivery.entity.AppUser;
import com.app.fooddelivery.entity.Role;
import com.app.fooddelivery.repository.RoleRepository;
import com.app.fooddelivery.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Admin endpoint is available");
    }

    @PutMapping("/assign-role/{userId}")
    public ResponseEntity<String> assignAdminRole(@PathVariable Long userId) {
        return userRepository.findById(userId).map(user -> {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));
            user.getRoles().add(adminRole);
            userRepository.save(user);
            return ResponseEntity.ok("Admin role assigned successfully");
        }).orElseGet(() -> ResponseEntity.badRequest().body("User not found"));
    }
}
