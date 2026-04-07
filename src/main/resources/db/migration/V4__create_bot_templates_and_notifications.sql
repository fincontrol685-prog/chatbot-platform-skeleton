-- V4__create_bot_templates_and_notifications.sql
-- Creates bot templates and notification tables for enhanced platform features

CREATE TABLE BOT_TEMPLATE (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description LONGTEXT,
    config LONGTEXT NOT NULL,
    category VARCHAR(100),
    icon_url VARCHAR(500),
    created_by BIGINT NOT NULL,
    is_public BOOLEAN DEFAULT FALSE,
    usage_count BIGINT DEFAULT 0,
    rating DOUBLE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES USER_ACCOUNT(id),
    INDEX idx_template_created_by (created_by),
    INDEX idx_template_created_at (created_at),
    INDEX idx_template_category (category)
);

CREATE TABLE NOTIFICATION (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message LONGTEXT,
    related_entity_type VARCHAR(50),
    related_entity_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    read_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id),
    INDEX idx_notification_user_id (user_id),
    INDEX idx_notification_read (is_read),
    INDEX idx_notification_created_at (created_at),
    INDEX idx_notification_type (type)
);

