package com.achivhub.backend.domain.services.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    @Value("${achievementhub.jwt.secretKey}")
    private String secretKey;

    @Value("${achievementhub.jwt.validity}")
    private long validity;

    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(TimeUnit.DAYS.toMillis(validity))))
                .signWith(generateKey())
                .compact();
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        if(jwt.isBlank() || userDetails == null) {
            return false;
        }

        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt) && isSignatureValid(jwt);
    }

    public boolean isTokenExpired(String jwt) {
        Date expirationDate = getClaims(jwt).getExpiration();
        return expirationDate.before(Date.from(Instant.now()));
    }

    private SecretKey generateKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private boolean isSignatureValid(String jwt) {
        try {
            Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(jwt);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    If u want to generate with extra claims.

    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nameOfClaim", "valueOfClaim");

        Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();


    }
    */
}
