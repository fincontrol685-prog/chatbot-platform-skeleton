-- V2__create_conversation_tables.sql
-- Creates conversation and message tracking tables for chatbot platform

CREATE TABLE CONVERSATION (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bot_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    message_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    closed_at TIMESTAMP NULL,
    metadata VARCHAR(2000),
    FOREIGN KEY (bot_id) REFERENCES BOT(id),
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id),
    INDEX idx_conversation_bot_id (bot_id),
    INDEX idx_conversation_user_id (user_id),
    INDEX idx_conversation_created_at (created_at)
);

CREATE TABLE CONVERSATION_MESSAGE (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    message_type VARCHAR(50) NOT NULL,
    content LONGTEXT NOT NULL,
    sentiment_score DOUBLE,
    intent VARCHAR(255),
    confidence DOUBLE,
    response_time_ms BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata VARCHAR(2000),
    is_flagged BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (conversation_id) REFERENCES CONVERSATION(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES USER_ACCOUNT(id),
    INDEX idx_message_conversation_id (conversation_id),
    INDEX idx_message_sender_id (sender_id),
    INDEX idx_message_created_at (created_at)
);

