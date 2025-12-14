-- V4: Optimized job postings schema with normalization and indexing

-- Companies table
CREATE TABLE IF NOT EXISTS companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    logo_url VARCHAR(500),
    website_url VARCHAR(500),
    industry VARCHAR(100),
    size VARCHAR(20),
    founded_year INT,
    headquarters_location VARCHAR(255),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT company_name_not_empty CHECK (length(trim(name)) > 0),
    CONSTRAINT company_size_valid CHECK (size IN ('STARTUP', 'SMALL', 'MEDIUM', 'LARGE', 'ENTERPRISE'))
);

CREATE INDEX idx_companies_name ON companies(name);
CREATE INDEX idx_companies_industry ON companies(industry);

-- Skills table
CREATE TABLE IF NOT EXISTS skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50),
    description TEXT,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT skill_name_not_empty CHECK (length(trim(name)) > 0)
);

CREATE UNIQUE INDEX idx_skills_name ON skills(name);

-- Benefits table
CREATE TABLE IF NOT EXISTS benefits (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50),
    description TEXT,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT benefit_name_not_empty CHECK (length(trim(name)) > 0)
);



CREATE UNIQUE INDEX idx_benefits_name ON benefits(name);

-- Jobs table (main entity)
DROP TABLE IF EXISTS jobs CASCADE;

CREATE TABLE jobs (
    id BIGSERIAL PRIMARY KEY,
    
    -- Basic information
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    external_job_id VARCHAR(255),
    
    -- Foreign keys
    company_id BIGINT NOT NULL,
    posted_by BIGINT NOT NULL,
    
    -- Location
    location VARCHAR(255) NOT NULL,
    location_type VARCHAR(20) NOT NULL DEFAULT 'ON_SITE', -- REMOTE, ON_SITE, HYBRID
    
    -- Compensation
    salary_min DECIMAL(10, 2),
    salary_max DECIMAL(10, 2),
    currency VARCHAR(3) DEFAULT 'USD',
    salary_frequency VARCHAR(20) DEFAULT 'ANNUAL', -- ANNUAL, HOURLY, MONTHLY
    
    -- Job details
    employment_type VARCHAR(20) NOT NULL, -- FULL_TIME, PART_TIME, CONTRACT, TEMPORARY, INTERNSHIP
    seniority_level VARCHAR(20) NOT NULL, -- ENTRY, MID, SENIOR, LEAD, EXECUTIVE
    experience_years_min INT DEFAULT 0,
    experience_years_max INT,
    
    -- Denormalized aggregates (performance)
    required_skill_count INT DEFAULT 0,
    benefit_count INT DEFAULT 0,
    application_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    
    -- Status & visibility
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT', -- DRAFT, ACTIVE, INACTIVE, CLOSED, EXPIRED
    visibility VARCHAR(20) NOT NULL DEFAULT 'PUBLIC', -- PUBLIC, INTERNAL, PRIVATE
    is_featured BOOLEAN DEFAULT FALSE,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    published_at TIMESTAMP,
    expires_at TIMESTAMP,
    closed_at TIMESTAMP,
    
    -- Constraints
    CONSTRAINT salary_valid CHECK (salary_min IS NULL OR salary_min >= 0),
    CONSTRAINT salary_range_valid CHECK (salary_min IS NULL OR salary_max IS NULL OR salary_max >= salary_min),
    CONSTRAINT experience_valid CHECK (experience_years_min >= 0 AND (experience_years_max IS NULL OR experience_years_max >= experience_years_min)),
    CONSTRAINT status_valid CHECK (status IN ('DRAFT', 'ACTIVE', 'INACTIVE', 'CLOSED', 'EXPIRED')),
    CONSTRAINT visibility_valid CHECK (visibility IN ('PUBLIC', 'INTERNAL', 'PRIVATE')),
    CONSTRAINT location_type_valid CHECK (location_type IN ('REMOTE', 'ON_SITE', 'HYBRID')),
    CONSTRAINT employment_type_valid CHECK (employment_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACT', 'TEMPORARY', 'INTERNSHIP')),
    CONSTRAINT seniority_level_valid CHECK (seniority_level IN ('ENTRY', 'MID', 'SENIOR', 'LEAD', 'EXECUTIVE')),
    CONSTRAINT title_not_empty CHECK (length(trim(title)) > 0),
    
    -- Foreign key constraints
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (posted_by) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Job skills junction table
CREATE TABLE IF NOT EXISTS job_skills (
    job_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency_level VARCHAR(20), -- BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    is_required BOOLEAN DEFAULT TRUE,
    years_experience INT,
    
    PRIMARY KEY (job_id, skill_id),
    
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    
    CONSTRAINT proficiency_valid CHECK (proficiency_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'))
);

CREATE INDEX idx_job_skills_skill_id ON job_skills(skill_id);
CREATE INDEX idx_job_skills_required ON job_skills(job_id, is_required);

-- Job benefits junction table
CREATE TABLE IF NOT EXISTS job_benefits (
    job_id BIGINT NOT NULL,
    benefit_id BIGINT NOT NULL,
    
    PRIMARY KEY (job_id, benefit_id),
    
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (benefit_id) REFERENCES benefits(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE INDEX idx_job_benefits_benefit_id ON job_benefits(benefit_id);

-- Primary indexes for jobs table
CREATE INDEX idx_jobs_status_active ON jobs(created_at DESC) WHERE status = 'ACTIVE'; -- Partial index for active jobs
CREATE INDEX idx_jobs_status ON jobs(status);
CREATE INDEX idx_jobs_location_status ON jobs(location, status, created_at DESC) WHERE status = 'ACTIVE';
CREATE INDEX idx_jobs_location ON jobs(location);
CREATE INDEX idx_jobs_salary ON jobs(salary_min, salary_max) WHERE status = 'ACTIVE' AND salary_min IS NOT NULL;
CREATE INDEX idx_jobs_company_id ON jobs(company_id);
CREATE INDEX idx_jobs_posted_by ON jobs(posted_by);
CREATE INDEX idx_jobs_created_at_brin ON jobs USING BRIN(created_at); -- Time-series index
CREATE INDEX idx_jobs_expires_at ON jobs(expires_at) WHERE expires_at IS NOT NULL AND status = 'ACTIVE';
CREATE INDEX idx_jobs_title_fts ON jobs USING GIN(to_tsvector('english', title)); -- Full-text search
CREATE INDEX idx_jobs_description_fts ON jobs USING GIN(to_tsvector('english', description));
CREATE UNIQUE INDEX idx_jobs_external_id_unique ON jobs(company_id, external_job_id) WHERE external_job_id IS NOT NULL;
CREATE INDEX idx_jobs_company_status ON jobs(company_id, status);
CREATE INDEX idx_jobs_featured ON jobs(created_at DESC) WHERE is_featured = TRUE;

-- Triggers to maintain denormalized columns
CREATE OR REPLACE FUNCTION update_job_skill_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE jobs 
    SET required_skill_count = (
        SELECT COUNT(*) FROM job_skills 
        WHERE job_id = COALESCE(NEW.job_id, OLD.job_id) AND is_required = TRUE
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE id = COALESCE(NEW.job_id, OLD.job_id);
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_job_skill_count ON job_skills;
CREATE TRIGGER trg_job_skill_count
AFTER INSERT OR UPDATE OR DELETE ON job_skills
FOR EACH ROW
EXECUTE FUNCTION update_job_skill_count();

CREATE OR REPLACE FUNCTION update_job_benefit_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE jobs 
    SET benefit_count = (
        SELECT COUNT(*) FROM job_benefits 
        WHERE job_id = COALESCE(NEW.job_id, OLD.job_id)
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE id = COALESCE(NEW.job_id, OLD.job_id);
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_job_benefit_count ON job_benefits;
CREATE TRIGGER trg_job_benefit_count
AFTER INSERT OR UPDATE OR DELETE ON job_benefits
FOR EACH ROW
EXECUTE FUNCTION update_job_benefit_count();

-- Sample data
INSERT INTO companies (name, description, industry, size, website_url, headquarters_location) 
VALUES 
    ('Tech Corp', 'Leading tech company', 'Technology', 'LARGE', 'https://techcorp.com', 'San Francisco, CA'),
    ('StartupXYZ', 'Innovative startup', 'Technology', 'STARTUP', 'https://startupxyz.com', 'Austin, TX')
ON CONFLICT (name) DO NOTHING;

INSERT INTO skills (name, category, description)
VALUES 
    ('Python', 'Programming', 'Python programming language'),
    ('Java', 'Programming', 'Java programming language'),
    ('React', 'Frontend', 'React JavaScript library'),
    ('PostgreSQL', 'Database', 'PostgreSQL relational database'),
    ('AWS', 'Cloud', 'Amazon Web Services')
ON CONFLICT (name) DO NOTHING;

INSERT INTO benefits (name, category, description)
VALUES 
    ('Health Insurance', 'Insurance', 'Comprehensive health coverage'),
    ('401k', 'Retirement', '401k matching program'),
    ('Remote Work', 'Flexibility', 'Work from home options'),
    ('Stock Options', 'Equity', 'Employee stock options')
ON CONFLICT (name) DO NOTHING;
