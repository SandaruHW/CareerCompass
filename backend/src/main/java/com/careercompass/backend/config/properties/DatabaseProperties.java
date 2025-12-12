package com.careercompass.backend.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "app.database")
public class DatabaseProperties {
    
    @NotBlank(message = "Database URL is required")
    private String url;
    
    @NotBlank(message = "Database username is required")
    private String username;
    
    @NotBlank(message = "Database password is required")
    private String password;
    
    @NotBlank(message = "Driver class name is required")
    private String driverClassName;
    
    @Min(value = 1, message = "Pool size must be at least 1")
    private Integer poolSize = 10;
    
    private Integer minIdle = 5;
    
    private Integer maxLifetime = 1800000; // 30 minutes
}

