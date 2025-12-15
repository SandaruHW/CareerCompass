package com.careercompass.backend.config;

import com.careercompass.backend.config.properties.AppProperties;
import com.careercompass.backend.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final AppProperties appProperties;
    @Lazy
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/hello").permitAll()
                .requestMatchers("/api/config-examples/**").permitAll()
                // Protected endpoints
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/resumes/**").authenticated()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Add JWT authentication filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Using @ConfigurationProperties instead of hardcoded values
        configuration.setAllowedOrigins(Arrays.asList(appProperties.getCors().getAllowedOrigins()));
        configuration.setAllowedMethods(Arrays.asList(appProperties.getCors().getAllowedMethods()));
        configuration.setAllowedHeaders(Arrays.asList(appProperties.getCors().getAllowedHeaders()));
        configuration.setAllowCredentials(appProperties.getCors().getAllowCredentials());
        configuration.setMaxAge(Duration.ofSeconds(appProperties.getCors().getMaxAge()));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

