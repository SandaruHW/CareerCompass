package com.careercompass.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Technical/professional skill entity - normalized many-to-many with jobs
@Entity
@Table(
    name = "skills",
    indexes = {
        @Index(name = "idx_skills_name", columnList = "name", unique = true)
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

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
    public Skill(String name, String category) {
        this.name = name;
        this.category = category;
    }
}
