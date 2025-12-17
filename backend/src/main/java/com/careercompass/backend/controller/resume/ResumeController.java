package com.careercompass.backend.controller.resume;

import com.careercompass.backend.dto.response.ResumeResponse;
import com.careercompass.backend.service.auth.JwtService;
import com.careercompass.backend.service.resume.ResumeService;
import com.careercompass.backend.service.user.UserService;
import com.careercompass.backend.entity.User;
import jakarta.servlet.http.HttpServletRequest;
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
@CrossOrigin(origins = "http://localhost:3000")
public class ResumeController {
    
    private final ResumeService resumeService;
    private final JwtService jwtService;
    private final UserService userService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        
        // Get current user from JWT token
        User currentUser = getCurrentUserFromToken(request);
        
        log.info("Received file upload request: fileName={}, size={}, contentType={}, userId={}",
                file.getOriginalFilename(), file.getSize(), file.getContentType(), currentUser.getId());
        
        ResumeResponse response = resumeService.uploadResume(file, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResume(
            @PathVariable Long id,
            HttpServletRequest request) {
        
        User currentUser = getCurrentUserFromToken(request);
        ResumeResponse response = resumeService.getResumeById(id, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getUserResumes(
            HttpServletRequest request) {
        
        User currentUser = getCurrentUserFromToken(request);
        List<ResumeResponse> responses = resumeService.getUserResumes(currentUser.getId());
        return ResponseEntity.ok(responses);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable Long id,
            HttpServletRequest request) {
        
        User currentUser = getCurrentUserFromToken(request);
        resumeService.deleteResume(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
    
    // Helper method to extract current user from JWT token
    private User getCurrentUserFromToken(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            throw new IllegalArgumentException("Authorization token is required");
        }
        
        String email = jwtService.extractUsername(token);
        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    // Extract JWT token from Authorization header
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

