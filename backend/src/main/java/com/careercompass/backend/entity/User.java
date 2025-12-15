package com.careercompass.backend.entity;

import com.careercompass.backend.entity.base.BaseAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * User entity with authentication, authorization, auditing and soft delete features.
 */
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_role", columnList = "role"),
        @Index(name = "idx_users_enabled_locked", columnList = "enabled, account_locked"),
        @Index(name = "idx_users_created_at", columnList = "created_at"),
        @Index(name = "idx_users_updated_at", columnList = "updated_at"),
        @Index(name = "idx_users_deleted_at", columnList = "deleted_at"),
        @Index(name = "idx_users_last_login", columnList = "last_login_at"),
        @Index(name = "idx_users_password_reset", columnList = "password_reset_token")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_username", columnNames = "username")
    }
)
// Soft delete: sets deleted_at instead of actual deletion
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ? AND version = ?")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "passwordResetToken"})
public class User extends BaseAuditEntity implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // === AUTHENTICATION FIELDS ===
    
    // Primary authentication identifier
    @Column(name = "email", nullable = false, unique = true, length = 320)
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    /**
     * Optional username for display purposes
     * Can be used as alternative identifier
     */
    @Column(name = "username", unique = true, length = 50)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
    private String username;
    
    /**
     * Hashed password using BCrypt
     * Never store plain text passwords!
     * Minimum length enforced at validation layer
     */
    @Column(name = "password_hash", nullable = false, length = 100) // BCrypt produces 60 chars, but allow extra space
    @NotBlank(message = "Password is required")
    private String password;
    
    // === PERSONAL INFORMATION ===
    
    @Column(name = "first_name", nullable = false, length = 100)
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;
    
    @Column(name = "phone_number", length = 20)
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
    private String phoneNumber;
    
    // === AUTHORIZATION FIELDS ===
    
    // Primary role for RBAC
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;
    
    // Additional permissions beyond primary role
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;
    
    // === ACCOUNT STATUS FIELDS ===
    
    // Account enabled status
    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;
    
    // Email verification status
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;
    
    // Account lock status
    @Column(name = "account_locked", nullable = false)
    @Builder.Default
    private Boolean accountLocked = false;
    
    // Failed login attempt counter
    @Column(name = "failed_login_attempts", nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;
    
    // Account lock timestamp
    @Column(name = "locked_at")
    private LocalDateTime lockedAt;
    
    // === PASSWORD MANAGEMENT ===
    
    // Password reset token
    @Column(name = "password_reset_token", length = 255)
    private String passwordResetToken;
    
    // Password reset token expiration
    @Column(name = "password_reset_expires_at")
    private LocalDateTime passwordResetExpiresAt;
    
    // Last password change timestamp
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;
    
    // === SESSION MANAGEMENT ===
    
    // Last login timestamp
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    // Last login IP address
    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;
    
    // === SOFT DELETE FIELDS ===
    
    // Soft delete timestamp
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    // User who performed deletion
    @Column(name = "deleted_by")
    private Long deletedBy;
    
    // Reason for deletion
    @Column(name = "deletion_reason", length = 500)
    private String deletionReason;
    
    // === ENUMS ===
    
    // System roles
    public enum Role {
        USER,           // Regular user
        RECRUITER,      // Can post jobs
        ADMIN,          // Full system access
        SUPER_ADMIN     // System administration
    }
    
    // Fine-grained permissions
    public enum Authority {
        // User management
        READ_USERS,
        WRITE_USERS,
        DELETE_USERS,
        
        // Job management
        READ_JOBS,
        WRITE_JOBS,
        DELETE_JOBS,
        PUBLISH_JOBS,
        
        // Resume management
        READ_RESUMES,
        WRITE_RESUMES,
        DELETE_RESUMES,
        
        // System administration
        SYSTEM_CONFIG,
        VIEW_ANALYTICS,
        MODERATE_CONTENT
    }
    
    // === SPRING SECURITY USERDETAILS IMPLEMENTATION ===
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        
        // Add additional authorities if they exist
        if (authorities != null) {
            authorities.forEach(authority -> 
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.name()))
            );
        }
        
        return grantedAuthorities;
    }
    
    @Override
    public String getUsername() {
        // Use email as the username for Spring Security
        return email;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement account expiration logic if needed
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        // Could implement password aging here
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled && !isDeleted();
    }
    
    // === HELPER METHODS ===
    
    // Check if user is soft deleted
    public boolean isDeleted() {
        return deletedAt != null;
    }
    
    // Get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // Check if user has specific authority
    public boolean hasAuthority(Authority authority) {
        return authorities != null && authorities.contains(authority);
    }
    
    // Check if password reset token is valid
    public boolean isPasswordResetTokenValid(String token) {
        return passwordResetToken != null 
            && passwordResetToken.equals(token)
            && passwordResetExpiresAt != null
            && passwordResetExpiresAt.isAfter(LocalDateTime.now());
    }
    
    // Reset failed login attempts after successful login
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.lockedAt = null;
    }
    
    // Increment failed attempts and lock if needed
    public void incrementFailedLoginAttempts(int maxAttempts) {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= maxAttempts) {
            this.accountLocked = true;
            this.lockedAt = LocalDateTime.now();
        }
    }
    
    // Soft delete the user
    public void softDelete(Long deletedBy, String reason) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.deletionReason = reason;
        this.enabled = false;
    }
    
    // Restore from soft delete
    public void restore() {
        this.deletedAt = null;
        this.deletedBy = null;
        this.deletionReason = null;
        this.enabled = true;
    }
}


