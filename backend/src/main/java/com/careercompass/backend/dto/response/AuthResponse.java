package com.careercompass.backend.dto.response;

import com.careercompass.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for authentication responses containing user info and token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String token;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn; // Token expiration in seconds
    
    // User information
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private User.Role role;
    private boolean emailVerified;
    private LocalDateTime lastLoginAt;
}