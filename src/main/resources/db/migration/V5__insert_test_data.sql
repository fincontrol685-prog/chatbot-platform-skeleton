-- Flyway migration: V5__insert_test_data.sql
-- Insert test data for authentication and initial setup

-- Note: Roles are already inserted in V1__create_core_tables.sql
-- Do NOT insert roles again here to avoid duplicates

-- Insert test user with password 'admin' (BCrypt hash)
-- Password: admin123
-- Hash generated with BCrypt
INSERT INTO USER_ACCOUNT (USERNAME, EMAIL, PASSWORD_HASH, ENABLED)
VALUES ('admin', 'admin@chatbot.local', '$2a$10$slYQmyNdGzin7olVN3OQq.7/GG4qNvUTqJlzUqCVHEjLvqLvV2Tja', 1);

-- Assign ADMIN role to admin user
INSERT INTO USER_ROLE (USER_ID, ROLE_ID)
SELECT u.ID, r.ID FROM USER_ACCOUNT u, ROLE r
WHERE u.USERNAME = 'admin' AND r.NAME = 'ADMIN';

-- Insert another test user
INSERT INTO USER_ACCOUNT (USERNAME, EMAIL, PASSWORD_HASH, ENABLED)
VALUES ('user', 'user@chatbot.local', '$2a$10$slYQmyNdGzin7olVN3OQq.7/GG4qNvUTqJlzUqCVHEjLvqLvV2Tja', 1);

-- Assign USER role to user
INSERT INTO USER_ROLE (USER_ID, ROLE_ID)
SELECT u.ID, r.ID FROM USER_ACCOUNT u, ROLE r
WHERE u.USERNAME = 'user' AND r.NAME = 'USUARIO';

