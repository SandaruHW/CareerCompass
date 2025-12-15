package com.careercompass.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for user login requests.
 */
@Data
public class LoginRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 320, message = "Email cannot exceed 320 characters")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    // Optional remember me flag
    private boolean rememberMe = false;
}