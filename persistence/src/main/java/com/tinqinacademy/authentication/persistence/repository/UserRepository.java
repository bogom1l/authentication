package com.tinqinacademy.authentication.persistence.repository;

import com.tinqinacademy.authentication.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
