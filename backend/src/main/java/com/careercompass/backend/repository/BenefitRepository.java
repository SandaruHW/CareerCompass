package com.careercompass.backend.repository;

import com.careercompass.backend.entity.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    
    Optional<Benefit> findByName(String name);
    
    boolean existsByName(String name);
}