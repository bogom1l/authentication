package com.tinqinacademy.authentication.core.security;

import com.tinqinacademy.authentication.api.exceptions.AuthenticationException;
import com.tinqinacademy.authentication.persistence.model.User;
import com.tinqinacademy.authentication.persistence.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

/***
 * This service handles all aspects of JWT token management - generation, validation, and extracting user information from the token.
 */
@Service
@RequiredArgsConstructor
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

    public boolean validateToken(String token) {
        String id;
        String role;

        try {
            id = extractId(token);
            role = extractRole(token);
            // if token is expired, it will catch it in the extractAllClaims(which is called when we call extractId)
        } catch (AuthenticationException ex) {
            return false;
        }

        Optional<User> user = userRepository.findById(UUID.fromString(id));

        return user.isPresent() && user.get().getRole().toString().equals(role);
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
        } catch (Exception e) {
            throw new AuthenticationException("Invalid JWT", HttpStatus.UNAUTHORIZED);
        }
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    private String generateToken(Map<String, Object> extraClaims, User user) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

        String token = Jwts.builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .issuedAt(currentDate)
                .claim("role", user.getRole().name())
                .claim("iat", currentDate)
                .claim("exp", expirationDate)
                .expiration(expirationDate)
                .signWith(getKey())
                .compact();

        return token;
    }
}
