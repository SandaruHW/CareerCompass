package com.careercompass.backend.dto.response;

import com.careercompass.backend.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
    
    private Long id;
    private String fileName;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private Resume.Status status;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

