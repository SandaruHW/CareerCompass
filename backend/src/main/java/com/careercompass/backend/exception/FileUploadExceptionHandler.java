package com.careercompass.backend.exception;

import com.careercompass.backend.dto.response.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

/**
 * Global exception handler for file upload related errors
 */
@ControllerAdvice
@Slf4j
public class FileUploadExceptionHandler {
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<FileUploadResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex) {
        log.error("File upload size exceeded: {}", ex.getMessage());
        
        FileUploadResponse response = FileUploadResponse.error(
            "File size exceeds maximum limit of 10MB"
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<FileUploadResponse> handleMultipartException(
            MultipartException ex) {
        log.error("Multipart file error: {}", ex.getMessage());
        
        FileUploadResponse response = FileUploadResponse.error(
            "Invalid file upload: " + ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FileUploadResponse> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        log.error("File validation error: {}", ex.getMessage());
        
        FileUploadResponse response = FileUploadResponse.error(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FileUploadResponse> handleFileUploadRuntimeException(
            RuntimeException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("Failed to save file")) {
            log.error("File save error: {}", ex.getMessage());
            
            FileUploadResponse response = FileUploadResponse.error(
                "Failed to save file. Please try again."
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        
        // Re-throw if not file-related
        throw ex;
    }
}