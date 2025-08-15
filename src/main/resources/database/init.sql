-- Create database
CREATE DATABASE vnair_user_db;

-- Connect to the database
\c vnair_user_db;

-- Create users table (will be auto-created by Hibernate, but this is for reference)
-- CREATE TABLE users (
--     id BIGSERIAL PRIMARY KEY,
--     username VARCHAR(50) UNIQUE NOT NULL,
--     email VARCHAR(255) UNIQUE NOT NULL,
--     phone VARCHAR(20) NOT NULL,
--     password_hash VARCHAR(255) NOT NULL,
--     status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Insert sample data
INSERT INTO users (username, email, phone, password_hash, status) VALUES
('admin', 'admin@vnair.com', '+84901234567', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm', 'ACTIVE'),
('user1', 'user1@example.com', '+84901234568', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm', 'ACTIVE'),
('user2', 'user2@example.com', '+84901234569', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm', 'INACTIVE');

-- Note: The password hash above represents "password123" encoded with BCrypt
