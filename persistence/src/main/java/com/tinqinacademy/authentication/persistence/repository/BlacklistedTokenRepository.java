package com.tinqinacademy.authentication.persistence.repository;

import com.tinqinacademy.authentication.persistence.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, UUID> {
    Boolean existsByToken(String token);
}
