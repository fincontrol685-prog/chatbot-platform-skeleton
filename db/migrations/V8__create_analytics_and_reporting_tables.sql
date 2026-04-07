-- V8__create_analytics_and_reporting_tables.sql
-- Criação das tabelas para Analytics Avançado e Relatórios Customizados

-- Tabela ANALYTICS_METRIC
CREATE TABLE IF NOT EXISTS ANALYTICS_METRIC (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    metric_type VARCHAR(100) NOT NULL,
    metric_value DOUBLE,
    bot_id BIGINT,
    department_id BIGINT,
    team_id BIGINT,
    user_id BIGINT,
    period_date DATE,
    period_hour INT,
    dimension_key VARCHAR(100),
    dimension_value VARCHAR(255),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bot_id) REFERENCES BOT(id),
    FOREIGN KEY (department_id) REFERENCES DEPARTMENT(id),
    FOREIGN KEY (team_id) REFERENCES TEAM(id),
    FOREIGN KEY (user_id) REFERENCES USER_ACCOUNT(id),
    INDEX idx_metric_type (metric_type),
    INDEX idx_bot_id_date (bot_id, period_date),
    INDEX idx_department_id_date (department_id, period_date),
    INDEX idx_team_id_date (team_id, period_date),
    INDEX idx_period_date (period_date),
    INDEX idx_recorded_at (recorded_at)
);

-- Tabela CUSTOM_REPORT
CREATE TABLE IF NOT EXISTS CUSTOM_REPORT (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_by BIGINT,
    user_account_id BIGINT NOT NULL,
    is_public BOOLEAN DEFAULT FALSE,
    report_definition LONGTEXT,
    metric_types VARCHAR(500),
    filters LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES USER_ACCOUNT(id),
    FOREIGN KEY (user_account_id) REFERENCES USER_ACCOUNT(id) ON DELETE CASCADE,
    INDEX idx_created_by (created_by),
    INDEX idx_user_account_id (user_account_id),
    INDEX idx_is_public (is_public),
    INDEX idx_updated_at (updated_at)
);

-- Índices de performance para buscas comuns
CREATE INDEX idx_analytics_metric_type_date ON ANALYTICS_METRIC(metric_type, period_date);
CREATE INDEX idx_custom_report_user_public ON CUSTOM_REPORT(user_account_id, is_public);

