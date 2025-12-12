package com.careercompass.backend.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    
    @NotBlank(message = "JWT secret is required")
    private String secret;
    
    @Min(value = 1, message = "Expiration time must be positive")
    private Long expirationMs = 86400000L; // 24 hours in milliseconds
    
    @Min(value = 1, message = "Refresh expiration time must be positive")
    private Long refreshExpirationMs = 604800000L; // 7 days in milliseconds
    
    private String issuer = "careercompass";
}

