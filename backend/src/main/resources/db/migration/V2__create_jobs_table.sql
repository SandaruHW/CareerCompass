-- Versioned Migration: V2__create_jobs_table.sql
-- Description: Create jobs table for job postings
-- Author: CareerCompass Team
-- Date: 2024-01-15

CREATE TABLE IF NOT EXISTS jobs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    company VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    salary_min DECIMAL(10, 2),
    salary_max DECIMAL(10, 2),
    employment_type VARCHAR(50),
    posted_by BIGINT REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT jobs_status_check CHECK (status IN ('ACTIVE', 'INACTIVE', 'CLOSED'))
);

-- Create indexes
CREATE INDEX idx_jobs_company ON jobs(company);
CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_jobs_status ON jobs(status);
CREATE INDEX idx_jobs_posted_by ON jobs(posted_by);
CREATE INDEX idx_jobs_created_at ON jobs(created_at);

-- Add comments
COMMENT ON TABLE jobs IS 'Job postings in the CareerCompass platform';
COMMENT ON COLUMN jobs.posted_by IS 'Foreign key to users table (admin/recruiter who posted)';
COMMENT ON COLUMN jobs.status IS 'Job status: ACTIVE, INACTIVE, or CLOSED';

