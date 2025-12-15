package com.careercompass.backend.service.user;

import com.careercompass.backend.dto.request.UserCreateRequest;
import com.careercompass.backend.dto.response.UserResponse;
import com.careercompass.backend.entity.User;
import com.careercompass.backend.exception.ResourceNotFoundException;
import com.careercompass.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * UserService providing user management, authentication and security operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int PASSWORD_RESET_TOKEN_VALIDITY_HOURS = 24;
    
    // Create new user account
    public UserResponse createUser(UserCreateRequest request) {
        log.debug("Creating new user with email: {}", request.getEmail());
        
        // Validate unique constraints
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email already exists: " + request.getEmail());
        }
        
        // Create user entity using builder pattern
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Hash the password
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(User.Role.USER) // Default role
                .enabled(true)
                .emailVerified(false) // Require email verification
                .accountLocked(false)
                .failedLoginAttempts(0)
                .passwordChangedAt(LocalDateTime.now())
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("Created new user with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());
        
        return mapToResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = findUserByIdOrThrow(id);
        return mapToResponse(user);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToResponse);
    }
    
    // Spring Security UserDetailsService implementation
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        log.debug("Found user: {} with roles: {}", user.getEmail(), user.getRole());
        return user; // User implements UserDetails
    }
    
    // Change user password with current password verification
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        log.debug("Changing password for user ID: {}", userId);
        
        User user = findUserByIdOrThrow(userId);
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        
        // Clear any existing reset token
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);
        
        userRepository.save(user);
        log.info("Password changed successfully for user ID: {}", userId);
    }
    
    // Initiate password reset process
    public String initiatePasswordReset(String email) {
        log.debug("Initiating password reset for email: {}", email);
        
        // Check if user exists (throws exception if not found)
        userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Generate secure reset token
        String resetToken = generateSecureToken();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(PASSWORD_RESET_TOKEN_VALIDITY_HOURS);
        
        // Set reset token
        userRepository.setPasswordResetToken(email, resetToken, expiresAt);
        
        log.info("Password reset token generated for user: {}", email);
        return resetToken;
    }
    
    // Complete password reset using token
    public void resetPassword(String token, String newPassword) {
        log.debug("Resetting password with token");
        
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));
        
        // Validate token
        if (!user.isPasswordResetTokenValid(token)) {
            throw new IllegalArgumentException("Reset token is expired or invalid");
        }
        
        // Update password
        userRepository.updatePassword(user.getId(), passwordEncoder.encode(newPassword), LocalDateTime.now());
        
        log.info("Password reset successfully for user: {}", user.getEmail());
    }
    
    // Record failed login attempt
    public void recordFailedLoginAttempt(String email, String ipAddress) {
        log.debug("Recording failed login attempt for email: {}", email);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.incrementFailedLoginAttempts(MAX_FAILED_ATTEMPTS);
            userRepository.save(user);
            
            if (user.getAccountLocked()) {
                log.warn("Account locked due to failed login attempts for email: {}", email);
            }
        }
    }
    
    // Record successful login
    public void recordSuccessfulLogin(String email, String ipAddress) {
        log.debug("Recording successful login for email: {}", email);
        
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.resetFailedLoginAttempts();
            userRepository.updateLastLogin(user.getId(), LocalDateTime.now(), ipAddress);
            
            log.info("Successful login recorded for user: {}", email);
        }
    }
    
    // Verify user email
    public void verifyEmail(String email) {
        log.debug("Verifying email: {}", email);
        
        int updated = userRepository.verifyEmail(email);
        if (updated > 0) {
            log.info("Email verified for user: {}", email);
        } else {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
    }
    
    // === UTILITY METHODS ===
    
    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    private String generateSecureToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .emailVerified(user.getEmailVerified())
                .accountLocked(user.getAccountLocked())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

