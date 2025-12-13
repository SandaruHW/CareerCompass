package com.careercompass.backend.repository;

import com.careercompass.backend.entity.Resume;
import com.careercompass.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    
    List<Resume> findByUser(User user);
    
    List<Resume> findByUserId(Long userId);
    
    Optional<Resume> findByIdAndUserId(Long id, Long userId);
    
    boolean existsByFileNameAndUserId(String fileName, Long userId);
}

