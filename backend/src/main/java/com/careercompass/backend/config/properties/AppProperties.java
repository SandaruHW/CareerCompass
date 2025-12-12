package com.careercompass.backend.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    @NotBlank(message = "Application name is required")
    private String name = "CareerCompass Backend";
    
    @NotBlank(message = "Application version is required")
    private String version = "1.0.0";
    
    private String environment;
    
    private Cors cors = new Cors();
    
    private FileUpload fileUpload = new FileUpload();
    
    @Data
    public static class Cors {
        private String[] allowedOrigins = {"http://localhost:3000"};
        private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
        private String[] allowedHeaders = {"*"};
        private Boolean allowCredentials = true;
        private Integer maxAge = 3600;
    }
    
    @Data
    public static class FileUpload {
        @Min(value = 1, message = "Max file size must be positive")
        private Long maxFileSize = 10485760L; // 10MB in bytes
        
        private String[] allowedTypes = {"application/pdf", "application/msword", 
                                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
        
        private String uploadDir = "./uploads";
    }
}

