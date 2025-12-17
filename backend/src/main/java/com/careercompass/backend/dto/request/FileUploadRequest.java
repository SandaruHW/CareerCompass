package com.careercompass.backend.dto.request;

import org.springframework.web.multipart.MultipartFile;

/**
 * DTO for file upload request - for validation and documentation
 */
public class FileUploadRequest {
    
    private MultipartFile file;
    
    public FileUploadRequest() {}
    
    public FileUploadRequest(MultipartFile file) {
        this.file = file;
    }
    
    public MultipartFile getFile() {
        return file;
    }
    
    public void setFile(MultipartFile file) {
        this.file = file;
    }
    
    /**
     * Validates the file upload request
     */
    public boolean isValid() {
        return file != null && !file.isEmpty();
    }
    
    /**
     * Gets the original filename
     */
    public String getOriginalFilename() {
        return file != null ? file.getOriginalFilename() : null;
    }
    
    /**
     * Gets the file size
     */
    public long getSize() {
        return file != null ? file.getSize() : 0;
    }
    
    /**
     * Gets the content type
     */
    public String getContentType() {
        return file != null ? file.getContentType() : null;
    }
}