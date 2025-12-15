package com.careercompass.backend.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity providing auditing fields for automatic timestamp and user tracking.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseAuditEntity {
    
    // Automatically set on creation
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Automatically updated on modification
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // User who created this entity
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;
    
    // User who last modified this entity
    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;
    
    // Version for optimistic locking
    @Version
    @Column(name = "version")
    private Long version = 0L;
}
