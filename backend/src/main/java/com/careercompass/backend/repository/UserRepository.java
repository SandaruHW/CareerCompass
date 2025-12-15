package com.careercompass.backend.repository;

import com.careercompass.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity with authentication, authorization and audit queries.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // === BASIC AUTHENTICATION QUERIES ===
    
    // Find user by email for authentication
    Optional<User> findByEmail(String email);
    
    // Find user by username
    Optional<User> findByUsername(String username);
    
    // Check if email exists for validation
    boolean existsByEmail(String email);
    
    // Check if username exists for validation
    boolean existsByUsername(String username);
    
    // === SOFT DELETE QUERIES ===
    
    // Find all deleted users
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NOT NULL")
    List<User> findAllDeleted();
    
    // Find deleted users within time range
    @Query("SELECT u FROM User u WHERE u.deletedAt BETWEEN :startDate AND :endDate")
    List<User> findDeletedBetween(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);
    
    // Count deleted users
    @Query("SELECT COUNT(u) FROM User u WHERE u.deletedAt IS NOT NULL")
    long countDeleted();
    
    // Find user by ID including deleted ones
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdIncludingDeleted(@Param("id") Long id);
    
    // === ROLE AND AUTHORIZATION QUERIES ===
    
    // Find users by role
    List<User> findByRole(User.Role role);
    
    // Find users by role with pagination
    Page<User> findByRole(User.Role role, Pageable pageable);
    
    // Find users with specific authority
    @Query("SELECT u FROM User u JOIN u.authorities a WHERE a = :authority")
    List<User> findByAuthority(@Param("authority") User.Authority authority);
    
    // Count users by role
    long countByRole(User.Role role);
    
    // === ACCOUNT STATUS QUERIES ===
    
    // Find enabled users
    List<User> findByEnabledTrue();
    
    // Find disabled users
    List<User> findByEnabledFalse();
    
    // Find locked accounts
    List<User> findByAccountLockedTrue();
    
    // Find users with unverified emails
    List<User> findByEmailVerifiedFalse();
    
    // Find recently locked accounts
    List<User> findByAccountLockedTrueAndLockedAtAfter(LocalDateTime date);
    
    // === SECURITY QUERIES ===
    
    // Find user by password reset token
    Optional<User> findByPasswordResetToken(String token);
    
    // Find users with high failed login attempts
    List<User> findByFailedLoginAttemptsGreaterThanEqual(Integer attempts);
    
    // Find users who haven't logged in recently
    @Query("SELECT u FROM User u WHERE u.lastLoginAt < :date OR u.lastLoginAt IS NULL")
    List<User> findUsersNotLoggedInSince(@Param("date") LocalDateTime date);
    
    // === SEARCH AND FILTERING ===
    
    // Search users by name or email
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Find users created within date range
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find recently registered users
    List<User> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);
    
    // === UPDATE OPERATIONS ===
    
    // Update last login information
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime, u.lastLoginIp = :ipAddress " +
           "WHERE u.id = :userId")
    int updateLastLogin(@Param("userId") Long userId, 
                       @Param("loginTime") LocalDateTime loginTime,
                       @Param("ipAddress") String ipAddress);
    
    // Reset failed login attempts
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = 0, u.lockedAt = NULL " +
           "WHERE u.id = :userId")
    int resetFailedLoginAttempts(@Param("userId") Long userId);
    
    // Lock user account
    @Modifying
    @Query("UPDATE User u SET u.accountLocked = true, u.lockedAt = :lockTime " +
           "WHERE u.id = :userId")
    int lockAccount(@Param("userId") Long userId, @Param("lockTime") LocalDateTime lockTime);
    
    // Update password and reset token fields
    @Modifying
    @Query("UPDATE User u SET u.password = :password, " +
           "u.passwordChangedAt = :changeTime, " +
           "u.passwordResetToken = NULL, " +
           "u.passwordResetExpiresAt = NULL " +
           "WHERE u.id = :userId")
    int updatePassword(@Param("userId") Long userId, 
                      @Param("password") String hashedPassword,
                      @Param("changeTime") LocalDateTime changeTime);
    
    // Set password reset token
    @Modifying
    @Query("UPDATE User u SET u.passwordResetToken = :token, " +
           "u.passwordResetExpiresAt = :expiresAt " +
           "WHERE u.email = :email")
    int setPasswordResetToken(@Param("email") String email,
                             @Param("token") String token,
                             @Param("expiresAt") LocalDateTime expiresAt);
    
    // Verify email address
    @Modifying
    @Query("UPDATE User u SET u.emailVerified = true WHERE u.email = :email")
    int verifyEmail(@Param("email") String email);
    
    // === ANALYTICS QUERIES ===
    
    // Get user registration statistics
    @Query("SELECT DATE(u.createdAt) as date, COUNT(u) as count " +
           "FROM User u " +
           "WHERE u.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(u.createdAt) " +
           "ORDER BY DATE(u.createdAt)")
    List<Object[]> getRegistrationStats(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
    
    // Get login activity statistics
    @Query("SELECT DATE(u.lastLoginAt) as date, COUNT(u) as count " +
           "FROM User u " +
           "WHERE u.lastLoginAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(u.lastLoginAt) " +
           "ORDER BY DATE(u.lastLoginAt)")
    List<Object[]> getLoginStats(@Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate);
}

