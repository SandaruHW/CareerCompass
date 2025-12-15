package com.careercompass.backend.service.auth;

import com.careercompass.backend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for JWT token generation and validation.
 */
@Service
@Slf4j
public class JwtService {
    
    @Value("${app.jwt.secret:YourSecretKeyChangeInProductionMinimum256BitsForSecurityPurposesCareerCompass2025}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration-ms:86400000}") // 24 hours in milliseconds
    private long jwtExpirationMs;
    
    @Value("${app.jwt.refresh-expiration-ms:604800000}") // 7 days in milliseconds
    private long jwtRefreshExpirationMs;
    
    // Generate JWT token for authenticated user
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return generateTokenForUser(user);
    }
    
    // Generate JWT token for user
    public String generateTokenForUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        claims.put("authorities", user.getAuthorities());
        
        return createToken(claims, user.getEmail(), jwtExpirationMs);
    }
    
    // Generate refresh token
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("type", "refresh");
        
        return createToken(claims, user.getEmail(), jwtRefreshExpirationMs);
    }
    
    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    // Extract user ID from token
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }
    
    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    // Extract specific claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token parsing failed: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
    
    // Check if token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    // Validate token against user details
    public Boolean validateToken(String token, User userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    // Create JWT token with claims and subject
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }
    
    // Get signing key for JWT
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    // Get token expiration time in seconds
    public long getExpirationTime() {
        return jwtExpirationMs / 1000; // Convert to seconds
    }
}