package com.careercompass.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


// Configuration for JPA auditing with Spring Security integration.
 
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {

    // Provides current user for JPA auditing
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }

    // Spring Security integration for auditing
    public static class SpringSecurityAuditorAware implements AuditorAware<Long> {

        @Override
        public Optional<Long> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || 
                !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.empty();
            }

            // If the principal is a UserDetails (our User entity implements this)
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                
                // If it's our User entity, we can get the ID directly
                if (userDetails instanceof com.careercompass.backend.entity.User) {
                    com.careercompass.backend.entity.User user = 
                        (com.careercompass.backend.entity.User) userDetails;
                    return Optional.ofNullable(user.getId());
                }
                
                // For other UserDetails implementations, try to parse ID from username
                try {
                    return Optional.of(Long.parseLong(userDetails.getUsername()));
                } catch (NumberFormatException e) {
                    // If username is not a number, we can't determine user ID
                    return Optional.empty();
                }
            }

            // Fallback: try to parse authentication name as user ID
            try {
                return Optional.of(Long.parseLong(authentication.getName()));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
    }
}