package com.careercompass.backend.controller.benefit;

import com.careercompass.backend.entity.Benefit;
import com.careercompass.backend.repository.BenefitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/benefits")
@CrossOrigin(origins = "*")
public class BenefitController {

    @Autowired
    private BenefitRepository benefitRepository;

    @GetMapping
    public ResponseEntity<Page<Benefit>> getAllBenefits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Benefit> benefits = benefitRepository.findAll(pageable);
        return ResponseEntity.ok(benefits);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Benefit>> getAllBenefitsList() {
        List<Benefit> benefits = benefitRepository.findAll();
        return ResponseEntity.ok(benefits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Benefit> getBenefitById(@PathVariable Long id) {
        Optional<Benefit> benefit = benefitRepository.findById(id);
        return benefit.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Benefit> createBenefit(@RequestBody Benefit benefit) {
        Benefit savedBenefit = benefitRepository.save(benefit);
        return ResponseEntity.ok(savedBenefit);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Benefit> updateBenefit(@PathVariable Long id, @RequestBody Benefit benefitDetails) {
        Optional<Benefit> optionalBenefit = benefitRepository.findById(id);
        if (optionalBenefit.isPresent()) {
            Benefit benefit = optionalBenefit.get();
            benefit.setName(benefitDetails.getName());
            benefit.setCategory(benefitDetails.getCategory());
            benefit.setDescription(benefitDetails.getDescription());
            
            Benefit savedBenefit = benefitRepository.save(benefit);
            return ResponseEntity.ok(savedBenefit);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBenefit(@PathVariable Long id) {
        if (benefitRepository.existsById(id)) {
            benefitRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Benefit API is running!");
    }
}