package com.careercompass.backend.dto.response;

import com.careercompass.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private User.Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

