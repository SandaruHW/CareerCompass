package com.careercompass.backend.dto.response;

import com.careercompass.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * UserResponse DTO excluding sensitive information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    
    // Authorization
    private User.Role role;
    
    // Account status
    private Boolean enabled;
    private Boolean emailVerified;
    private Boolean accountLocked;
    
    // Security info (non-sensitive)
    private LocalDateTime lastLoginAt;
    private Integer failedLoginAttempts;
    private LocalDateTime passwordChangedAt;
    
    // Audit info
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Basic user info for public APIs
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Basic {
        private Long id;
        private String email;
        private String fullName;
        private User.Role role;
        private Boolean enabled;
        private LocalDateTime createdAt;
    }
    
    // Detailed user info for admin interfaces
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detailed {
        private Long id;
        private String email;
        private String username;
        private String firstName;
        private String lastName;
        private String fullName;
        private String phoneNumber;
        private User.Role role;
        private Boolean enabled;
        private Boolean emailVerified;
        private Boolean accountLocked;
        private LocalDateTime lastLoginAt;
        private String lastLoginIp;
        private Integer failedLoginAttempts;
        private LocalDateTime lockedAt;
        private LocalDateTime passwordChangedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long createdBy;
        private Long updatedBy;
        private Long version;
    }
}

