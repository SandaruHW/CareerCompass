package com.careercompass.backend.service.resume;

import com.careercompass.backend.config.properties.AppProperties;
import com.careercompass.backend.dto.response.ResumeResponse;
import com.careercompass.backend.entity.Resume;
import com.careercompass.backend.entity.User;
import com.careercompass.backend.exception.ResourceNotFoundException;
import com.careercompass.backend.repository.ResumeRepository;
import com.careercompass.backend.repository.UserRepository;
import com.careercompass.backend.util.FileValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ResumeService {
    
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    
    public ResumeResponse uploadResume(MultipartFile file, Long userId) {
        // Validate file
        FileValidationUtil.validateFile(file);
        
        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Generate unique file name to prevent conflicts
        String originalFileName = file.getOriginalFilename();
        String fileExtension = FileValidationUtil.getFileExtension(originalFileName);
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        
        // Resolve safe file path
        Path uploadDir = Paths.get(appProperties.getFileUpload().getUploadDir());
        try {
            // Create upload directory if it doesn't exist
            Files.createDirectories(uploadDir);
            
            // Resolve safe path (prevents path traversal)
            Path filePath = FileValidationUtil.resolveSafePath(uploadDir, uniqueFileName);
            
            // Save file to disk
            Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
            );
            
            log.info("File saved successfully: {}", filePath);
            
            // Create resume entity
            Resume resume = new Resume();
            resume.setFileName(uniqueFileName);
            resume.setOriginalFileName(FileValidationUtil.sanitizeFileName(originalFileName));
            resume.setFilePath(filePath.toString());
            resume.setContentType(file.getContentType());
            resume.setFileSize(file.getSize());
            resume.setUser(user);
            resume.setStatus(Resume.Status.UPLOADED);
            
            Resume savedResume = resumeRepository.save(resume);
            
            log.info("Resume saved to database with id: {}", savedResume.getId());
            
            return mapToResponse(savedResume);
            
        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }
    
    @Transactional(readOnly = true)
    public ResumeResponse getResumeById(Long id, Long userId) {
        Resume resume = resumeRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with id: " + id));
        return mapToResponse(resume);
    }
    
    @Transactional(readOnly = true)
    public List<ResumeResponse> getUserResumes(Long userId) {
        return resumeRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public void deleteResume(Long id, Long userId) {
        Resume resume = resumeRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with id: " + id));
        
        // Delete file from disk
        try {
            Path filePath = Paths.get(resume.getFilePath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted: {}", filePath);
            }
        } catch (IOException e) {
            log.error("Error deleting file: {}", e.getMessage(), e);
            // Continue with database deletion even if file deletion fails
        }
        
        // Delete from database
        resumeRepository.delete(resume);
        log.info("Resume deleted from database with id: {}", id);
    }
    
    private ResumeResponse mapToResponse(Resume resume) {
        return new ResumeResponse(
                resume.getId(),
                resume.getFileName(),
                resume.getOriginalFileName(),
                resume.getContentType(),
                resume.getFileSize(),
                resume.getStatus(),
                resume.getUser().getId(),
                resume.getCreatedAt(),
                resume.getUpdatedAt()
        );
    }
}

