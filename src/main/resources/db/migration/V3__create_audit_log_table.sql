-- V3__create_audit_log_table.sql
-- Creates audit log table for tracking all user actions and changes

CREATE TABLE AUDIT_LOG (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    old_value LONGTEXT,
    new_value LONGTEXT,
    ip_address VARCHAR(45),
    status VARCHAR(20) NOT NULL DEFAULT 'SUCCESS',
    error_message LONGTEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id),
    INDEX idx_audit_user_id (user_id),
    INDEX idx_audit_created_at (created_at),
    INDEX idx_audit_action (action)
);

