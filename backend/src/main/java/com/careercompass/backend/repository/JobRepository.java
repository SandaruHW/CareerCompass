package com.careercompass.backend.repository;

import com.careercompass.backend.entity.Job;
import com.careercompass.backend.entity.Job.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JobRepository - Data Access Layer for Job entity.
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' ORDER BY j.createdAt DESC")
    Page<Job> findActiveJobs(Pageable pageable);

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    Page<Job> findByCompanyId(Long companyId, Pageable pageable);

    Page<Job> findByPostedBy(Long userId, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.location = :location AND j.status = 'ACTIVE' " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findByLocation(@Param("location") String location, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.location = :location AND j.employmentType = :type " +
           "AND j.status = 'ACTIVE' ORDER BY j.createdAt DESC")
    Page<Job> findByLocationAndEmploymentType(
        @Param("location") String location,
        @Param("type") Job.EmploymentType type,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.locationType = 'REMOTE' AND j.status = 'ACTIVE' " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findRemoteJobs(Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' " +
           "AND j.salaryMin IS NOT NULL AND j.salaryMax IS NOT NULL " +
           "AND j.salaryMin >= :minSalary AND j.salaryMax <= :maxSalary " +
           "ORDER BY j.salaryMax DESC")
    Page<Job> findBySalaryRange(
        @Param("minSalary") BigDecimal minSalary,
        @Param("maxSalary") BigDecimal maxSalary,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' " +
           "AND j.salaryMin >= :minSalary " +
           "ORDER BY j.salaryMax DESC")
    Page<Job> findHighPayingJobs(
        @Param("minSalary") BigDecimal minSalary,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.seniorityLevel = :level AND j.status = 'ACTIVE' " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findBySeniorityLevel(
        @Param("level") Job.SeniorityLevel level,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' " +
           "AND j.experienceYearsMin <= :yearsExperience " +
           "AND (j.experienceYearsMax IS NULL OR j.experienceYearsMax >= :yearsExperience) " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findByExperienceLevel(
        @Param("yearsExperience") Integer yearsExperience,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' " +
           "AND j.employmentType = :type " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findByEmploymentType(
        @Param("type") Job.EmploymentType type,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' " +
           "AND j.isFeatured = true " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findFeaturedJobs(Pageable pageable);

    @Query("SELECT j FROM Job j LEFT JOIN FETCH j.skills s " +
           "WHERE s.name IN :skillNames AND j.status = 'ACTIVE' " +
           "GROUP BY j.id " +
           "ORDER BY COUNT(s.id) DESC")
    Page<Job> findBySkills(
        @Param("skillNames") List<String> skillNames,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j " +
           "WHERE j.status = 'ACTIVE' " +
           "AND (LOWER(j.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findByTitleOrDescriptionContaining(
        @Param("searchTerm") String searchTerm,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' " +
           "AND j.expiresAt IS NOT NULL " +
           "AND j.expiresAt <= :date " +
           "ORDER BY j.expiresAt ASC")
    List<Job> findExpiringJobs(@Param("date") LocalDateTime date);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' " +
           "AND j.createdAt >= :date " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findRecentJobs(
        @Param("date") LocalDateTime date,
        Pageable pageable
    );

    @Query("SELECT j FROM Job j " +
           "WHERE j.companyId = :companyId " +
           "AND j.status = :status " +
           "ORDER BY j.createdAt DESC")
    Page<Job> findByCompanyIdAndStatus(
        @Param("companyId") Long companyId,
        @Param("status") JobStatus status,
        Pageable pageable
    );

    @Query("SELECT j.id as jobId, j.title, j.viewCount " +
           "FROM Job j " +
           "WHERE j.status = 'ACTIVE' " +
           "ORDER BY j.viewCount DESC")
    Page<Object[]> findPopularJobs(Pageable pageable);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.companyId = :companyId AND j.status = 'ACTIVE'")
    Long countActiveJobsByCompany(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.postedBy = :userId")
    Long countJobsByPostedBy(@Param("userId") Long userId);

    Optional<Job> findByIdAndStatus(Long id, JobStatus status);

    List<Job> findTop10ByStatusOrderByCreatedAtDesc(JobStatus status);
}
