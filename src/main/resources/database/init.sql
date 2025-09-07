-- -- Create database
-- CREATE DATABASE vnair_user_db;

-- -- Connect to the database
-- \c vnair_user_db;

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

-- -- Create indexes for better performance
-- CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
-- CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
-- CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
-- CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- -- Insert sample data
-- INSERT INTO users (username, email, phone, password_hash, status) VALUES
-- ('admin', 'admin@vnair.com', '+84901234567', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm', 'ACTIVE'),
-- ('user1', 'user1@example.com', '+84901234568', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm', 'ACTIVE'),
-- ('user2', 'user2@example.com', '+84901234569', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm', 'INACTIVE');

-- -- Note: The password hash above represents "password123" encoded with BCrypt

-- -- docker exec -it vnair_postgres psql -U postgres -d vnair_user_db


-- Create users table (will be auto-created by Hibernate, but this is for reference)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    company_name VARCHAR(255),
    country_region VARCHAR(255) NOT NULL,
    tax_id VARCHAR(255),
    user_role_type VARCHAR(255) NOT NULL CHECK (user_role_type IN ('FORWARDER', 'CARRIER', 'INDIVIDUAL')),
    agreed_to_terms BOOLEAN NOT NULL DEFAULT FALSE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create aircrafts table
CREATE TABLE IF NOT EXISTS aircrafts (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create cabins table
CREATE TABLE IF NOT EXISTS cabins (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    position VARCHAR(50) NOT NULL CHECK (position IN ('FRONT', 'MIDDLE', 'REAR')),
    seat_count INTEGER NOT NULL CHECK (seat_count > 0),
    description VARCHAR(500),
    aircraft_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (aircraft_id) REFERENCES aircrafts(id) ON DELETE CASCADE
);

-- Create user_roles table (many-to-many relationship with additional fields)
CREATE TABLE IF NOT EXISTS user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    assigned_by_user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_by_user_id) REFERENCES users(id) ON DELETE SET NULL,
    UNIQUE(user_id, role_id)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

CREATE INDEX IF NOT EXISTS idx_roles_name ON roles(name);
CREATE INDEX IF NOT EXISTS idx_roles_is_active ON roles(is_active);

CREATE INDEX IF NOT EXISTS idx_user_roles_user_id ON user_roles(user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role_id ON user_roles(role_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_is_active ON user_roles(is_active);

CREATE INDEX IF NOT EXISTS idx_aircrafts_code ON aircrafts(code);
CREATE INDEX IF NOT EXISTS idx_cabins_aircraft_id ON cabins(aircraft_id);
CREATE INDEX IF NOT EXISTS idx_cabins_position ON cabins(position);

-- Insert sample data with complete fields
INSERT INTO users (username, email, phone, password_hash, full_name, country_region, user_role_type, agreed_to_terms, status) VALUES
('admin', 'admin@vnair.com', '+84901234567', '$2a$10$/pnKnqLov5zSS6TmyyeqeuTUTMOl4giQkRNbVBdHlLUYAkaBgZ5EC', 'System Administrator', 'Vietnam', 'INDIVIDUAL', TRUE, 'ACTIVE'),
('user1', 'user1@example.com', '+84901234568', '$2a$10$/pnKnqLov5zSS6TmyyeqeuTUTMOl4giQkRNbVBdHlLUYAkaBgZ5EC', 'John Doe', 'Vietnam', 'INDIVIDUAL', TRUE, 'ACTIVE'),
('user2', 'user2@example.com', '+84901234569', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm', 'Jane Smith', 'Vietnam', 'INDIVIDUAL', TRUE, 'INACTIVE')
ON CONFLICT (username) DO NOTHING;

-- Insert sample roles
INSERT INTO roles (name, description, is_active) VALUES
('ADMIN', 'Administrator role with full system access', TRUE),
('USER', 'Standard user role with limited access', TRUE),
('MANAGER', 'Manager role with intermediate privileges', TRUE)
ON CONFLICT (name) DO NOTHING;

-- Insert sample aircrafts
INSERT INTO aircrafts (code, name) VALUES
('VN-A350', 'Airbus A350-900'),
('VN-A321', 'Airbus A321-200'),
('VN-B777', 'Boeing 777-200ER')
ON CONFLICT (code) DO NOTHING;

-- Insert sample cabins
INSERT INTO cabins (name, position, seat_count, description, aircraft_id) 
SELECT 'Business Class', 'FRONT', 28, 'Premium business class cabin with lie-flat seats', a.id
FROM aircrafts a WHERE a.code = 'VN-A350';

INSERT INTO cabins (name, position, seat_count, description, aircraft_id) 
SELECT 'Premium Economy', 'MIDDLE', 56, 'Enhanced economy class with extra legroom', a.id
FROM aircrafts a WHERE a.code = 'VN-A350';

INSERT INTO cabins (name, position, seat_count, description, aircraft_id) 
SELECT 'Economy Class', 'REAR', 245, 'Standard economy class cabin', a.id
FROM aircrafts a WHERE a.code = 'VN-A350';

INSERT INTO cabins (name, position, seat_count, description, aircraft_id) 
SELECT 'Business Class', 'FRONT', 16, 'Premium business class cabin', a.id
FROM aircrafts a WHERE a.code = 'VN-A321';

INSERT INTO cabins (name, position, seat_count, description, aircraft_id) 
SELECT 'Economy Class', 'REAR', 174, 'Standard economy class cabin', a.id
FROM aircrafts a WHERE a.code = 'VN-A321';

-- Assign roles to users
-- Note: Using subqueries to handle potential ID variations
INSERT INTO user_roles (user_id, role_id, is_active, assigned_at) 
SELECT u.id, r.id, TRUE, NOW()
FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ADMIN'
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id, is_active, assigned_at) 
SELECT u.id, r.id, TRUE, NOW()
FROM users u, roles r 
WHERE u.username = 'user1' AND r.name = 'USER'
ON CONFLICT (user_id, role_id) DO NOTHING;

INSERT INTO user_roles (user_id, role_id, is_active, assigned_at) 
SELECT u.id, r.id, TRUE, NOW()
FROM users u, roles r 
WHERE u.username = 'user2' AND r.name = 'USER'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- Note: The password hashes above represent:
-- - admin: password123 (hash: $2a$10$/pnKnqLov5zSS6TmyyeqeuTUTMOl4giQkRNbVBdHlLUYAkaBgZ5EC)
-- - user1: password123 (hash: $2a$10$/pnKnqLov5zSS6TmyyeqeuTUTMOl4giQkRNbVBdHlLUYAkaBgZ5EC) 
-- - user2: password123 (hash: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm)

-- You can now login with:
-- - username: admin, password: password123 (has ADMIN role, ACTIVE)
-- - username: user1, password: password123 (has USER role, ACTIVE)
-- - username: user2, password: password123 (has USER role, but account is INACTIVE)

-- Database structure includes all required fields for registration:
-- - full_name: User's full name (REQUIRED)
-- - country_region: User's country/region (REQUIRED) 
-- - user_role_type: FORWARDER, CARRIER, or INDIVIDUAL (REQUIRED)
-- - agreed_to_terms: Must be TRUE for registration (REQUIRED)
-- - company_name: Optional for business users
-- - tax_id: Optional for business users

-- API Endpoints available:
-- POST /api/auth/login - Login existing user
-- POST /api/auth/register - Register new user with full validation
-- GET /api/users/stats - Get user statistics (requires authentication)

-- Aircraft Management:
-- POST /api/aircrafts - Create new aircraft
-- GET /api/aircrafts - Get all aircrafts
-- GET /api/aircrafts/{id} - Get aircraft by ID
-- PUT /api/aircrafts/{id} - Update aircraft
-- DELETE /api/aircrafts/{id} - Delete aircraft

-- Cabin Management:
-- POST /api/cabins - Create new cabin
-- GET /api/cabins - Get all cabins
-- GET /api/cabins/{id} - Get cabin by ID
-- PUT /api/cabins/{id} - Update cabin
-- DELETE /api/cabins/{id} - Delete cabin
-- GET /api/cabins/aircraft/{aircraftId} - Get cabins by aircraft ID

-- Sample Data:
-- - 3 aircrafts: VN-A350, VN-A321, VN-B777
-- - 5 cabins: Business/Premium Economy/Economy for A350, Business/Economy for A321
-- - Cabin positions: FRONT, MIDDLE, REAR
