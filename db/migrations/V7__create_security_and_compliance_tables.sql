-- V7__create_security_and_compliance_tables.sql
-- Criação das tabelas para 2FA, Consentimento e GDPR

-- Tabela TWO_FACTOR_AUTH
CREATE TABLE IF NOT EXISTS TWO_FACTOR_AUTH (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    is_enabled BOOLEAN DEFAULT FALSE,
    secret_key VARCHAR(255),  -- Armazenado criptografado
    backup_codes LONGTEXT,    -- Armazenado criptografado
    backup_codes_used LONGTEXT,
    method VARCHAR(50) DEFAULT 'TOTP',
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_verified_at TIMESTAMP,
    is_verified BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_enabled (is_enabled)
);

-- Tabela CONSENT_LOG (GDPR)
CREATE TABLE IF NOT EXISTS CONSENT_LOG (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    consent_type VARCHAR(100) NOT NULL,
    is_granted BOOLEAN NOT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    consent_version VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    withdrawn_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_consent_type (consent_type),
    INDEX idx_created_at (created_at),
    UNIQUE KEY unique_consent_per_user (user_id, consent_type)
);

-- Tabela DATA_DELETION_REQUEST (GDPR - Direito ao Esquecimento)
CREATE TABLE IF NOT EXISTS DATA_DELETION_REQUEST (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',  -- PENDING, APPROVED, REJECTED, COMPLETED
    reason TEXT,
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    processed_by BIGINT,
    ip_address VARCHAR(45),
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id) ON DELETE CASCADE,
    FOREIGN KEY (processed_by) REFERENCES USER_ACCOUNT(id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_requested_at (requested_at)
);

-- Índices de performance
CREATE INDEX idx_tfa_is_enabled ON TWO_FACTOR_AUTH(is_enabled);
CREATE INDEX idx_consent_withdrawn ON CONSENT_LOG(withdrawn_at);
CREATE INDEX idx_deletion_status ON DATA_DELETION_REQUEST(status);

