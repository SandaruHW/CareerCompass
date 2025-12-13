package com.careercompass.backend.util;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileValidationUtil {
    
    // Allowed MIME types for resumes
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    
    // Allowed file extensions
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
        ".pdf", ".doc", ".docx"
    );
    
    // Maximum file size: 10MB (from AppProperties)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB in bytes
    
    /**
     * Validates file content type
     */
    public static boolean isValidContentType(String contentType) {
        return contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase());
    }
    
    /**
     * Validates file extension
     */
    public static boolean isValidExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        String lowerFileName = fileName.toLowerCase();
        return ALLOWED_EXTENSIONS.stream()
                .anyMatch(lowerFileName::endsWith);
    }
    
    /**
     * Validates file size
     */
    public static boolean isValidSize(long fileSize) {
        return fileSize > 0 && fileSize <= MAX_FILE_SIZE;
    }
    
    /**
     * Validates file name for path traversal attacks
     */
    public static boolean isSafeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        
        // Check for path traversal patterns
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            return false;
        }
        
        // Check for null bytes
        if (fileName.contains("\0")) {
            return false;
        }
        
        // Check for reserved characters (Windows)
        String[] reservedChars = { "<", ">", ":", "\"", "|", "?", "*" };
        for (String reserved : reservedChars) {
            if (fileName.contains(reserved)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Sanitizes file name to prevent path traversal
     */
    public static String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return "resume";
        }
        
        // Remove path separators and parent directory references
        String sanitized = fileName
                .replaceAll("\\.\\.", "")
                .replaceAll("/", "_")
                .replaceAll("\\\\", "_")
                .replaceAll("\0", "");
        
        // Remove reserved characters
        sanitized = sanitized.replaceAll("[<>:\"|?*]", "_");
        
        // Trim whitespace
        sanitized = sanitized.trim();
        
        // If empty after sanitization, use default name
        if (sanitized.isEmpty()) {
            sanitized = "resume";
        }
        
        return sanitized;
    }
    
    /**
     * Gets file extension from filename
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex).toLowerCase();
        }
        return "";
    }
    
    /**
     * Validates MultipartFile
     */
    public static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required and cannot be empty");
        }
        
        // Validate file name
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("File name is required");
        }
        
        if (!isSafeFileName(originalFileName)) {
            throw new IllegalArgumentException("Invalid file name: contains unsafe characters");
        }
        
        // Validate file extension
        if (!isValidExtension(originalFileName)) {
            throw new IllegalArgumentException(
                "Invalid file type. Allowed types: PDF, DOC, DOCX"
            );
        }
        
        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !isValidContentType(contentType)) {
            throw new IllegalArgumentException(
                "Invalid content type. Allowed types: application/pdf, application/msword, " +
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );
        }
        
        // Validate file size
        long fileSize = file.getSize();
        if (!isValidSize(fileSize)) {
            throw new IllegalArgumentException(
                String.format("File size exceeds maximum limit of %d MB", MAX_FILE_SIZE / (1024 * 1024))
            );
        }
    }
    
    /**
     * Resolves safe file path preventing path traversal
     */
    public static Path resolveSafePath(Path baseDir, String fileName) {
        // Sanitize the filename
        String sanitized = sanitizeFileName(fileName);
        
        // Resolve path
        Path resolved = baseDir.resolve(sanitized);
        
        // Normalize to remove any remaining path traversal attempts
        Path normalized = resolved.normalize();
        
        // Ensure the resolved path is within the base directory
        if (!normalized.startsWith(baseDir.normalize())) {
            throw new IllegalArgumentException("Path traversal detected");
        }
        
        return normalized;
    }
}

