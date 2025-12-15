package com.careercompass.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Job posting entity with denormalized aggregates for performance
@Entity
@Table(
    name = "jobs",
    indexes = {
        @Index(name = "idx_jobs_status_active", columnList = "created_at DESC", unique = false),
        @Index(name = "idx_jobs_status", columnList = "status", unique = false),
        @Index(name = "idx_jobs_location", columnList = "location", unique = false),
        @Index(name = "idx_jobs_location_status", columnList = "location, status, created_at DESC", unique = false),
        @Index(name = "idx_jobs_salary", columnList = "salary_min, salary_max", unique = false),
        @Index(name = "idx_jobs_company_id", columnList = "company_id", unique = false),
        @Index(name = "idx_jobs_posted_by", columnList = "posted_by", unique = false),
        @Index(name = "idx_jobs_created_at", columnList = "created_at DESC", unique = false),
        @Index(name = "idx_jobs_expires_at", columnList = "expires_at", unique = false),
        @Index(name = "idx_jobs_company_status", columnList = "company_id, status", unique = false),
        @Index(name = "idx_jobs_featured", columnList = "is_featured, created_at DESC", unique = false)
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String externalJobId;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private Long postedBy;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LocationType locationType = LocationType.ON_SITE;

    @Column(precision = 10, scale = 2)
    private BigDecimal salaryMin;

    @Column(precision = 10, scale = 2)
    private BigDecimal salaryMax;

    @Column(length = 3)
    @Builder.Default
    private String currency = "USD";

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SalaryFrequency salaryFrequency = SalaryFrequency.ANNUAL;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private SeniorityLevel seniorityLevel;

    @Column(nullable = false)
    @Builder.Default
    private Integer experienceYearsMin = 0;

    private Integer experienceYearsMax;

    // Denormalized aggregates for performance
    @Column(nullable = false)
    @Builder.Default
    private Integer requiredSkillCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer benefitCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer applicationCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatus status = JobStatus.DRAFT;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Visibility visibility = Visibility.PUBLIC;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isFeatured = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime closedAt;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "job_skills",
        joinColumns = @JoinColumn(name = "job_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "job_benefits",
        joinColumns = @JoinColumn(name = "job_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "benefit_id", referencedColumnName = "id")
    )
    @Builder.Default
    private Set<Benefit> benefits = new HashSet<>();

    // Enums
    public enum JobStatus {
        DRAFT, ACTIVE, INACTIVE, CLOSED, EXPIRED
    }

    public enum LocationType {
        REMOTE, ON_SITE, HYBRID
    }

    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, TEMPORARY, INTERNSHIP
    }

    public enum SeniorityLevel {
        ENTRY, MID, SENIOR, LEAD, EXECUTIVE
    }

    public enum SalaryFrequency {
        ANNUAL, HOURLY, MONTHLY
    }

    public enum Visibility {
        PUBLIC, INTERNAL, PRIVATE
    }

    // Helper methods
    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    public void addBenefit(Benefit benefit) {
        this.benefits.add(benefit);
    }

    public boolean hasSalary() {
        return salaryMin != null && salaryMax != null;
    }

    public void publish() {
        this.status = JobStatus.ACTIVE;
        this.publishedAt = LocalDateTime.now();
    }

    public void close() {
        this.status = JobStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }

    public boolean isAcceptingApplications() {
        return status == JobStatus.ACTIVE && 
               (expiresAt == null || expiresAt.isAfter(LocalDateTime.now()));
    }
}
