package com.mdd.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key SECRET_KEY;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            this.SECRET_KEY = Keys.hmacShaKeyFor(decodedKey);
            System.out.println("Successfully loaded secret key.");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT secret key: Ensure it is Base64 encoded and at least 32 bytes long.");
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            System.out.println("Failed to parse JWT: " + e.getMessage());
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
        System.out.println("Generated JWT: " + token);
        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String emailFromToken = extractUsername(token);

            if (!emailFromToken.equals(userDetails.getUsername())) {
                System.out.println("JWT validation failed: email mismatch. Token email: " + emailFromToken + ", Expected email: " + userDetails.getUsername());
                return false;
            }

            if (isTokenExpired(token)) {
                System.out.println("JWT validation failed: token expired.");
                return false;
            }

            return true;

        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}
