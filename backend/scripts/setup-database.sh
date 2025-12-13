#!/bin/bash

# Database Setup Script for CareerCompass
# This script creates the database and user for development

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== CareerCompass Database Setup ===${NC}\n"

# Default values
DB_NAME="${DB_NAME:-careercompass}"
DB_USER="${DB_USER:-postgres}"
DB_PASSWORD="${DB_PASSWORD:-108247}"
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"

echo "Database Configuration:"
echo "  Host: $DB_HOST"
echo "  Port: $DB_PORT"
echo "  Database: $DB_NAME"
echo "  User: $DB_USER"
echo ""

# Check if PostgreSQL is running
echo -e "${BLUE}Checking PostgreSQL connection...${NC}"
if ! psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c '\q' 2>/dev/null; then
    echo -e "${RED}Error: Cannot connect to PostgreSQL${NC}"
    echo "Please ensure PostgreSQL is running and credentials are correct"
    exit 1
fi
echo -e "${GREEN}✓ PostgreSQL connection successful${NC}\n"

# Create database if it doesn't exist
echo -e "${BLUE}Creating database '$DB_NAME'...${NC}"
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres <<EOF
SELECT 'CREATE DATABASE $DB_NAME'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_NAME')\gexec
EOF
echo -e "${GREEN}✓ Database '$DB_NAME' ready${NC}\n"

# Create user if it doesn't exist (optional)
if [ "$DB_USER" != "postgres" ]; then
    echo -e "${BLUE}Creating user '$DB_USER'...${NC}"
    psql -h "$DB_HOST" -p "$DB_PORT" -U postgres -d postgres <<EOF
DO \$\$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = '$DB_USER') THEN
        CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';
    END IF;
END
\$\$;
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
EOF
    echo -e "${GREEN}✓ User '$DB_USER' ready${NC}\n"
fi

# Grant privileges
echo -e "${BLUE}Granting privileges...${NC}"
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" <<EOF
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
\c $DB_NAME
GRANT ALL ON SCHEMA public TO $DB_USER;
EOF
echo -e "${GREEN}✓ Privileges granted${NC}\n"

echo -e "${GREEN}=== Database Setup Complete ===${NC}\n"
echo "You can now run the Spring Boot application:"
echo "  cd backend"
echo "  ./mvnw spring-boot:run"
echo ""
echo "Flyway will automatically apply migrations on startup."

