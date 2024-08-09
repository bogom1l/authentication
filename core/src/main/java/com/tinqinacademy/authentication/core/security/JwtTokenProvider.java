package com.tinqinacademy.authentication.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationTime}")
    private Integer tokenExpiration;

    private SecretKey decodedKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(decodedKey())
                    .build()
                    .parse(token);
            return true;
        } catch (IllegalArgumentException | MalformedJwtException |
                 UnsupportedJwtException | ExpiredJwtException e) {
            return false;
        }
    }

    public String getUsername(String token){
        Claims claims = Jwts.parser()
                .verifyWith(decodedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public String createToken(Authentication authentication) {
        Date now = new Date();
        Date expirityDate = new Date(now.getTime() + tokenExpiration);

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User has no roles assigned"));

        String token = Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .claim("roles", role) // todo ? ''role''
                .claim("iat", now.getTime() / 1000)
                .claim("exp", expirityDate.getTime() / 1000)
                .expiration(expirityDate)
                .signWith(decodedKey())
                .compact();

        return token;
    }

}
