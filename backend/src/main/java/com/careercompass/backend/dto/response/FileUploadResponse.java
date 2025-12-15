package com.careercompass.backend.dto.response;

/**
 * Generic file upload response DTO
 */
public class FileUploadResponse {
    
    private Long id;
    private String fileName;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String status;
    private String uploadedAt;
    private String message;
    
    public FileUploadResponse() {}
    
    public FileUploadResponse(Long id, String fileName, String originalFileName, 
                            String contentType, Long fileSize, String status, 
                            String uploadedAt, String message) {
        this.id = id;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.status = status;
        this.uploadedAt = uploadedAt;
        this.message = message;
    }
    
    // Static factory method for success response
    public static FileUploadResponse success(Long id, String fileName, String originalFileName, 
                                           String contentType, Long fileSize, String uploadedAt) {
        return new FileUploadResponse(id, fileName, originalFileName, contentType, 
                                    fileSize, "SUCCESS", uploadedAt, "File uploaded successfully");
    }
    
    // Static factory method for error response
    public static FileUploadResponse error(String message) {
        return new FileUploadResponse(null, null, null, null, null, 
                                    "ERROR", null, message);
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getOriginalFileName() { return originalFileName; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
    
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}