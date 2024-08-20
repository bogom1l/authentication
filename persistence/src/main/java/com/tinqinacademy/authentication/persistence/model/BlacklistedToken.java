package com.tinqinacademy.authentication.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
