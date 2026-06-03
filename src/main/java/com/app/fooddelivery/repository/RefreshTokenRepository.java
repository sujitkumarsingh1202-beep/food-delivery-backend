package com.app.fooddelivery.repository;

import com.app.fooddelivery.entity.RefreshToken;
import com.app.fooddelivery.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(AppUser user);
}
