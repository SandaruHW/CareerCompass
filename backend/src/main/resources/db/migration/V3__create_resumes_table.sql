-- Versioned Migration: V3__create_resumes_table.sql
-- Description: Create resumes table for uploaded CV documents
-- Author: CareerCompass Team
-- Date: 2024-01-15

CREATE TABLE IF NOT EXISTS resumes (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'UPLOADED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT resumes_status_check CHECK (status IN ('UPLOADED', 'PROCESSING', 'ANALYZED', 'FAILED'))
);

-- Create indexes
CREATE INDEX idx_resumes_user_id ON resumes(user_id);
CREATE INDEX idx_resumes_status ON resumes(status);
CREATE INDEX idx_resumes_created_at ON resumes(created_at);
CREATE INDEX idx_resumes_file_name ON resumes(file_name);

-- Add comments
COMMENT ON TABLE resumes IS 'Uploaded resume/CV documents for CareerCompass platform';
COMMENT ON COLUMN resumes.file_name IS 'Unique generated file name stored on disk';
COMMENT ON COLUMN resumes.original_file_name IS 'Original file name from user upload';
COMMENT ON COLUMN resumes.file_path IS 'Full path to the stored file';
COMMENT ON COLUMN resumes.content_type IS 'MIME type of the file (e.g., application/pdf)';
COMMENT ON COLUMN resumes.file_size IS 'File size in bytes';
COMMENT ON COLUMN resumes.status IS 'Processing status: UPLOADED, PROCESSING, ANALYZED, FAILED';

