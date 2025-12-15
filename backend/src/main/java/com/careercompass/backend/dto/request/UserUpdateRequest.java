package com.careercompass.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Request DTO for updating user profile information
 * All fields are optional for partial updates
 */
@Data
public class UserUpdateRequest {
    
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]*$", message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
    private String username;
    
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]*$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    private String firstName;
    
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'-]*$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    private String lastName;
    
    @Pattern(
        regexp = "^\\+?[1-9]\\d{1,14}$",
        message = "Phone number must be valid (international format, 1-15 digits)"
    )
    private String phoneNumber;
}
