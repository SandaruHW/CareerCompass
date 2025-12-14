package com.careercompass.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Job benefit entity - normalized many-to-many with jobs
@Entity
@Table(
    name = "benefits",
    indexes = {
        @Index(name = "idx_benefits_name", columnList = "name", unique = true)
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Constructor for common use
    public Benefit(String name, String category) {
        this.name = name;
        this.category = category;
    }
}
