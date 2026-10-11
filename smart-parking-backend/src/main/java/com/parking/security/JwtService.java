package com.parking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // =========================
    // SIGNING KEY
    // =========================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // =========================
    // GENERATE JWT
    // =========================

    public String generateToken(
            String username,
            String role) {

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    // =========================
    // EXTRACT USERNAME
    // =========================

    public String extractUsername(
            String token) {

        return getClaims(token)
                .getSubject();
    }

    // =========================
    // EXTRACT ROLE
    // =========================

    public String extractRole(
            String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    // =========================
    // VALIDATE TOKEN
    // =========================

    public boolean isTokenValid(
            String token,
            String username) {

        String extractedUsername =
                extractUsername(token);

        return extractedUsername.equals(username)
                && !isTokenExpired(token);
    }

    // =========================
    // CHECK EXPIRATION
    // =========================

    private boolean isTokenExpired(
            String token) {

        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // =========================
    // GET CLAIMS
    // =========================

    private Claims getClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
