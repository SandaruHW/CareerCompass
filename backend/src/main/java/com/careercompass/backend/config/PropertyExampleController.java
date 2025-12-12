package com.careercompass.backend.config;

import com.careercompass.backend.config.properties.AppProperties;
import com.careercompass.backend.config.properties.DatabaseProperties;
import com.careercompass.backend.config.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Example controller demonstrating @Value and @ConfigurationProperties usage
 * Remove this in production - it's for learning purposes only
 */
@RestController
@RequestMapping("/api/config-examples")
@RequiredArgsConstructor
public class PropertyExampleController {
    
    // Example 1: @Value for simple properties
    @Value("${app.name:Default App Name}")
    private String appName;
    
    @Value("${app.version:1.0.0}")
    private String appVersion;
    
    @Value("${spring.profiles.active:unknown}")
    private String activeProfile;
    
    @Value("${server.port:8080}")
    private Integer serverPort;
    
    // Example 2: @Value with default values
    @Value("${app.feature.enabled:false}")
    private Boolean featureEnabled;
    
    // Example 3: @Value with environment variable fallback
    @Value("${DB_PASSWORD:defaultPassword}")
    private String dbPasswordFromEnv;
    
    // Example 4: @ConfigurationProperties (type-safe, validated)
    private final AppProperties appProperties;
    private final DatabaseProperties databaseProperties;
    private final JwtProperties jwtProperties;
    
    @GetMapping("/value-examples")
    public Map<String, Object> getValueExamples() {
        Map<String, Object> examples = new HashMap<>();
        examples.put("appName", appName);
        examples.put("appVersion", appVersion);
        examples.put("activeProfile", activeProfile);
        examples.put("serverPort", serverPort);
        examples.put("featureEnabled", featureEnabled);
        examples.put("dbPasswordFromEnv", "***hidden***");
        return examples;
    }
    
    @GetMapping("/configuration-properties")
    public Map<String, Object> getConfigurationProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("app", appProperties);
        props.put("database", Map.of(
            "url", databaseProperties.getUrl(),
            "username", databaseProperties.getUsername(),
            "poolSize", databaseProperties.getPoolSize()
        ));
        props.put("jwt", Map.of(
            "expirationMs", jwtProperties.getExpirationMs(),
            "issuer", jwtProperties.getIssuer()
        ));
        return props;
    }
    
    @GetMapping("/active-profile")
    public Map<String, String> getActiveProfile() {
        Map<String, String> profile = new HashMap<>();
        profile.put("activeProfile", activeProfile);
        profile.put("appName", appProperties.getName());
        profile.put("environment", appProperties.getEnvironment());
        return profile;
    }
}

