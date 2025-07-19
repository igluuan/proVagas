-- Initialization script for proVagas database
-- This script creates the basic structure and initial data

-- Create database if not exists (handled by Docker environment variables)
-- CREATE DATABASE IF NOT EXISTS vagas_db;

-- Set timezone
SET timezone = 'America/Sao_Paulo';

-- Enable extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Insert default roles if not exists
-- Note: Application will create roles automatically, but we can ensure they exist
INSERT INTO roles (name) VALUES ('USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('COMPANY') ON CONFLICT (name) DO NOTHING;

-- Create default admin user (password: admin123)
-- Note: This should be changed in production
INSERT INTO users (id, name, email, password, created_at, is_active) 
VALUES (
    uuid_generate_v4(),
    'Administrator',
    'admin@provagas.com',
    '$2a$10$9ZQjG5z1QG5QG5QG5QG5Qu.5QG5QG5QG5QG5QG5QG5QG5QG5QG5QG5',
    NOW(),
    true
) ON CONFLICT (email) DO NOTHING;

-- Grant admin role to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.email = 'admin@provagas.com' 
AND r.name = 'ADMIN'
ON CONFLICT DO NOTHING;