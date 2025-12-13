package com.careercompass.backend.controller.resume;

import com.careercompass.backend.dto.response.ResumeResponse;
import com.careercompass.backend.service.resume.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@Slf4j
public class ResumeController {
    
    private final ResumeService resumeService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) {
        
        log.info("Received file upload request: fileName={}, size={}, contentType={}, userId={}",
                file.getOriginalFilename(), file.getSize(), file.getContentType(), userId);
        
        ResumeResponse response = resumeService.uploadResume(file, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResume(
            @PathVariable Long id,
            @RequestParam("userId") Long userId) {
        ResumeResponse response = resumeService.getResumeById(id, userId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getUserResumes(
            @RequestParam("userId") Long userId) {
        List<ResumeResponse> responses = resumeService.getUserResumes(userId);
        return ResponseEntity.ok(responses);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable Long id,
            @RequestParam("userId") Long userId) {
        resumeService.deleteResume(id, userId);
        return ResponseEntity.noContent().build();
    }
}

