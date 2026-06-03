package com.app.fooddelivery.controller;

import com.app.fooddelivery.dto.request.LoginRequest;
import com.app.fooddelivery.dto.request.SignupRequest;
import com.app.fooddelivery.dto.response.AuthResponse;
import com.app.fooddelivery.entity.AppUser;
import com.app.fooddelivery.entity.RefreshToken;
import com.app.fooddelivery.exception.BadRequestException;
import com.app.fooddelivery.security.JwtUtil;
import com.app.fooddelivery.service.AuthService;
import com.app.fooddelivery.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        AppUser registeredUser = authService.register(user);
        String accessToken = jwtUtil.generateToken(registeredUser.getUsername(), registeredUser.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toList()));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(registeredUser);
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken(), "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return authService.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> {
                    String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toList()));
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
                    return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken.getToken(), "Login successful"));
                })
                .orElseGet(() -> ResponseEntity.status(401).body(new AuthResponse(null, null, "Invalid credentials")));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .filter(refreshTokenService::verifyExpiration)
                .map(token -> {
                    List<String> roles = token.getUser().getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toList());
                    String accessToken = jwtUtil.generateToken(token.getUser().getUsername(), roles);
                    return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken, "Token refreshed"));
                })
                .orElseGet(() -> ResponseEntity.status(403).body(new AuthResponse(null, null, "Invalid or expired refresh token")));
    }
}
