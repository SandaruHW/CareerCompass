package com.careercompass.backend.controller.job;

import com.careercompass.backend.entity.Job;
import com.careercompass.backend.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    // Get all jobs with pagination
    @GetMapping
    public ResponseEntity<Page<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobRepository.findAll(pageable);
        return ResponseEntity.ok(jobs);
    }

    // Get active jobs
    @GetMapping("/active")
    public ResponseEntity<Page<Job>> getActiveJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobRepository.findActiveJobs(pageable);
        return ResponseEntity.ok(jobs);
    }

    // Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    // Create new job
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job savedJob = jobRepository.save(job);
        return ResponseEntity.ok(savedJob);
    }

    // Update job
    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        Optional<Job> optionalJob = jobRepository.findById(id);
        if (optionalJob.isPresent()) {
            Job job = optionalJob.get();
            job.setTitle(jobDetails.getTitle());
            job.setDescription(jobDetails.getDescription());
            job.setLocation(jobDetails.getLocation());
            job.setSalaryMin(jobDetails.getSalaryMin());
            job.setSalaryMax(jobDetails.getSalaryMax());
            job.setEmploymentType(jobDetails.getEmploymentType());
            job.setSeniorityLevel(jobDetails.getSeniorityLevel());
            job.setStatus(jobDetails.getStatus());
            
            Job savedJob = jobRepository.save(job);
            return ResponseEntity.ok(savedJob);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete job
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Search jobs by location
    @GetMapping("/location/{location}")
    public ResponseEntity<Page<Job>> getJobsByLocation(
            @PathVariable String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobRepository.findByLocation(location, pageable);
        return ResponseEntity.ok(jobs);
    }

    // Search jobs by salary range
    @GetMapping("/salary")
    public ResponseEntity<Page<Job>> getJobsBySalaryRange(
            @RequestParam BigDecimal minSalary,
            @RequestParam BigDecimal maxSalary,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobRepository.findBySalaryRange(minSalary, maxSalary, pageable);
        return ResponseEntity.ok(jobs);
    }

    // Get remote jobs
    @GetMapping("/remote")
    public ResponseEntity<Page<Job>> getRemoteJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobRepository.findRemoteJobs(pageable);
        return ResponseEntity.ok(jobs);
    }

    // Get featured jobs
    @GetMapping("/featured")
    public ResponseEntity<Page<Job>> getFeaturedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobRepository.findFeaturedJobs(pageable);
        return ResponseEntity.ok(jobs);
    }

    // Get jobs by company
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<Job>> getJobsByCompany(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobRepository.findByCompanyId(companyId, pageable);
        return ResponseEntity.ok(jobs);
    }

    // Search jobs by title or description
    @GetMapping("/search")
    public ResponseEntity<Page<Job>> searchJobs(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobRepository.findByTitleOrDescriptionContaining(query, pageable);
        return ResponseEntity.ok(jobs);
    }

    // Get count of active jobs by company
    @GetMapping("/count/company/{companyId}")
    public ResponseEntity<Long> getJobCountByCompany(@PathVariable Long companyId) {
        Long count = jobRepository.countActiveJobsByCompany(companyId);
        return ResponseEntity.ok(count);
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Job API is running!");
    }
}