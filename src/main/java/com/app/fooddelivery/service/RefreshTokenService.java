package com.app.fooddelivery.service;

import com.app.fooddelivery.entity.AppUser;
import com.app.fooddelivery.entity.RefreshToken;
import com.app.fooddelivery.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(@Value("${jwt.refresh.expiration}") long refreshTokenDurationMs,
                               RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenDurationMs = refreshTokenDurationMs;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(AppUser user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().isAfter(Instant.now())) {
            return true;
        }
        refreshTokenRepository.delete(refreshToken);
        return false;
    }

    public void deleteByUser(AppUser user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
