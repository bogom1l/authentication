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

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserRepository userRepository;

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.expirationTime}")
    private Integer jwtExpiration; // 5min

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String jwt) {
        String id;
        String role;

        try {
            id = extractId(jwt);
            role = extractRole(jwt);
        } catch (AuthenticationException ex) {
            return false;
        }

        Optional<User> user = userRepository.findById(UUID.fromString(id));

        return user.isPresent() && user.get().getRole().toString().equals(role);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    public Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    public String extractId(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public String extractRole(String jwt) {
        return extractClaim(jwt, claims -> claims.get("role", String.class));
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid JWT.", HttpStatus.UNAUTHORIZED);
        }
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    private String generateToken(Map<String, Object> extraClaims, User user) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpiration);

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
