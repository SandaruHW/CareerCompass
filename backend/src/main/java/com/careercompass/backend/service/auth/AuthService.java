package com.careercompass.backend.service.auth;

import com.careercompass.backend.dto.request.LoginRequest;
import com.careercompass.backend.dto.request.UserCreateRequest;
import com.careercompass.backend.dto.response.AuthResponse;
import com.careercompass.backend.dto.response.UserResponse;
import com.careercompass.backend.entity.User;
import com.careercompass.backend.exception.ResourceNotFoundException;
import com.careercompass.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for authentication operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    // User registration
    public AuthResponse register(UserCreateRequest request) {
        log.debug("Registering new user with email: {}", request.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        
        if (request.getUsername() != null && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        
        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(User.Role.USER) // Default role
                .enabled(true)
                .emailVerified(false)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .build();
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());
        
        // Generate tokens
        String token = jwtService.generateTokenForUser(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        
        return buildAuthResponse(savedUser, token, refreshToken);
    }
    
    // User login
    public AuthResponse login(LoginRequest request, String ipAddress) {
        log.debug("Attempting login for email: {}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found. Please check your email or sign up."));
        
        // Check if account is locked
        if (user.getAccountLocked()) {
            log.warn("Login attempted for locked account: {}", request.getEmail());
            throw new BadCredentialsException("Your account has been locked due to too many failed login attempts. Please contact support.");
        }
        
        // Check if account is enabled
        if (!user.getEnabled()) {
            throw new BadCredentialsException("Your account has been disabled. Please contact support.");
        }
        
        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Record failed login attempt
            user.incrementFailedLoginAttempts(5); // Max 5 attempts
            userRepository.save(user);
            log.warn("Failed login attempt for email: {}", request.getEmail());
            throw new BadCredentialsException("Incorrect password. Please try again.");
        }
        
        // Successful login - reset failed attempts and update last login
        user.resetFailedLoginAttempts();
        user.setLastLoginAt(LocalDateTime.now());
        user.setLastLoginIp(ipAddress);
        userRepository.save(user);
        
        log.info("User logged in successfully: {}", user.getEmail());
        
        // Generate tokens
        String token = jwtService.generateTokenForUser(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        return buildAuthResponse(user, token, refreshToken);
    }
    
    // Get current user
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token is required");
        }
        
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        return mapToUserResponse(user);
    }
    
    // Refresh token
    public AuthResponse refreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token is required");
        }
        
        try {
            String email = jwtService.extractUsername(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
            if (!jwtService.validateToken(refreshToken, user)) {
                throw new BadCredentialsException("Invalid refresh token");
            }
            
            // Generate new tokens
            String newToken = jwtService.generateTokenForUser(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
            
            return buildAuthResponse(user, newToken, newRefreshToken);
            
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            throw new BadCredentialsException("Invalid refresh token");
        }
    }
    
    // Logout (token invalidation could be implemented with a blacklist)
    public void logout(String token) {
        // In a production system, you might want to blacklist the token
        // For now, we just log the logout
        if (token != null) {
            try {
                String email = jwtService.extractUsername(token);
                log.info("User logged out: {}", email);
            } catch (Exception e) {
                log.debug("Invalid token during logout: {}", e.getMessage());
            }
        }
    }
    
    // Build authentication response
    private AuthResponse buildAuthResponse(User user, String token, String refreshToken) {
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .role(user.getRole())
                .emailVerified(user.getEmailVerified())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
    
    // Map User to UserResponse
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
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