package com.tinqinacademy.authentication.core.security;

import org.springframework.http.HttpStatus;
import com.tinqinacademy.authentication.api.exceptions.AuthException;
import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

/***
 * This service handles all aspects of JWT token management - generation, validation, and extracting user information from the token.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final UserRepository userRepository;

    @Value("${jwt.secretKey}")
    private String JWT_SECRET_KEY;

    @Value("${jwt.expirationTime}")
    private Integer JWT_EXPIRATION; // 5min

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token) {
        try {
            String id = extractId(token);
            String role = extractRole(token);

            return validateUser(id, role);
        } catch (AuthException ex) {
            log.error("Token validation error: {}", ex.getMessage());
            return false;
        }
    }

    private Boolean validateUser(String id, String role) {
        Optional<User> user = userRepository.findById(UUID.fromString(id));
        return user.isPresent() && role.equals(user.get().getRole().toString());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception ex) {
            throw new AuthException(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    public String generateToken(User user) {
        return generateToken(user, new HashMap<>());
    }

    private String generateToken(User user, Map<String, Object> extraClaims) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

        String token = Jwts.builder()
                .subject(user.getId().toString())
                .claim("role", user.getRole().name())
                .claims(extraClaims)
                .issuedAt(currentDate) //.claim("iat", currentDate)
                .expiration(expirationDate) //.claim("exp", expirationDate)
                .signWith(getKey())
                .compact();

        return token;
    }
}
