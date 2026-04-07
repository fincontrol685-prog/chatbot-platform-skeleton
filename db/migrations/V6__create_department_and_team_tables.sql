-- V6__create_department_and_team_tables.sql
-- Criação das tabelas de Departamentos e Equipes para Gerenciamento Profissional

-- Tabela DEPARTMENT
CREATE TABLE IF NOT EXISTS DEPARTMENT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    code VARCHAR(100) UNIQUE,
    parent_department_id BIGINT,
    manager_id BIGINT,
    location VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (parent_department_id) REFERENCES DEPARTMENT(id),
    FOREIGN KEY (manager_id) REFERENCES USER_ACCOUNT(id),
    INDEX idx_parent_id (parent_department_id),
    INDEX idx_manager_id (manager_id),
    INDEX idx_is_active (is_active),
    INDEX idx_created_by (created_by)
);

-- Tabela USER_DEPARTMENT (Many-to-Many)
CREATE TABLE IF NOT EXISTS USER_DEPARTMENT (
    user_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, department_id),
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES DEPARTMENT(id) ON DELETE CASCADE,
    INDEX idx_department_id (department_id)
);

-- Tabela TEAM
CREATE TABLE IF NOT EXISTS TEAM (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(100) UNIQUE,
    department_id BIGINT NOT NULL,
    team_lead_id BIGINT,
    is_active BOOLEAN DEFAULT TRUE,
    max_conversations_per_user INT DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (department_id) REFERENCES DEPARTMENT(id),
    FOREIGN KEY (team_lead_id) REFERENCES USER_ACCOUNT(id),
    INDEX idx_department_id (department_id),
    INDEX idx_team_lead_id (team_lead_id),
    INDEX idx_is_active (is_active),
    INDEX idx_created_by (created_by),
    UNIQUE KEY unique_name_per_dept (name, department_id)
);

-- Tabela TEAM_MEMBER (Many-to-Many)
CREATE TABLE IF NOT EXISTS TEAM_MEMBER (
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (team_id, user_id),
    FOREIGN KEY (team_id) REFERENCES TEAM(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
);

-- Tabela TEAM_ROLE
CREATE TABLE IF NOT EXISTS TEAM_ROLE (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL, -- TEAM_LEAD, MEMBER, MODERATOR
    is_active BOOLEAN DEFAULT TRUE,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by BIGINT,
    FOREIGN KEY (team_id) REFERENCES TEAM(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_by) REFERENCES USER_ACCOUNT(id),
    INDEX idx_team_user (team_id, user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_is_active (is_active),
    UNIQUE KEY unique_user_per_team (team_id, user_id)
);

-- Índices para melhor performance
CREATE INDEX idx_department_is_active ON DEPARTMENT(is_active);
CREATE INDEX idx_team_is_active ON TEAM(is_active);
CREATE INDEX idx_teamrole_is_active ON TEAM_ROLE(is_active);

