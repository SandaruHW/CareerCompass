package com.careercompass.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Company entity - normalized out of jobs table for reusability
@Entity
@Table(
    name = "companies",
    indexes = {
        @Index(name = "idx_companies_name", columnList = "name", unique = true),
        @Index(name = "idx_companies_industry", columnList = "industry")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String logoUrl;

    @Column(length = 500)
    private String websiteUrl;

    @Column(length = 100)
    private String industry;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CompanySize size;

    private Integer foundedYear;

    @Column(length = 255)
    private String headquartersLocation;

    @Column(nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(nullable = false)
    private java.time.LocalDateTime updatedAt;

    // Company size enum
    public enum CompanySize {
        STARTUP,    // < 50 employees
        SMALL,      // 50 - 500 employees
        MEDIUM,     // 500 - 5,000 employees
        LARGE,      // 5,000 - 50,000 employees
        ENTERPRISE  // > 50,000 employees
    }

    // Helper methods
    public boolean isProfileComplete() {
        return description != null && !description.isEmpty() &&
               logoUrl != null && !logoUrl.isEmpty() &&
               websiteUrl != null && !websiteUrl.isEmpty();
    }
}
