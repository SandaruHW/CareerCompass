package com.careercompass.backend.config;

import com.careercompass.backend.config.properties.AppProperties;
import com.careercompass.backend.config.properties.DatabaseProperties;
import com.careercompass.backend.config.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    AppProperties.class,
    DatabaseProperties.class,
    JwtProperties.class
})
public class PropertyConfig {
    // This class enables @ConfigurationProperties beans
}

