-- V14: Extended subscription lifecycle
-- Adds tables and columns for: checkout intents, subscription audit logs,
-- refund requests, and lifecycle tracking fields on tenant_subscriptions.

-- 1. Lifecycle tracking columns on tenant_subscriptions
ALTER TABLE tenant_subscriptions
    ADD COLUMN IF NOT EXISTS grace_period_starts_at TIMESTAMP WITH TIME ZONE,
    ADD COLUMN IF NOT EXISTS read_only_starts_at    TIMESTAMP WITH TIME ZONE,
    ADD COLUMN IF NOT EXISTS suspended_at           TIMESTAMP WITH TIME ZONE,
    ADD COLUMN IF NOT EXISTS cancelled_at           TIMESTAMP WITH TIME ZONE;

-- 2. checkout_intents: tracks user checkout attempts from open to completion or expiry
CREATE TABLE IF NOT EXISTS checkout_intents
(
    id                    BIGSERIAL PRIMARY KEY,
    user_id               BIGINT      NOT NULL,
    plan_id               BIGINT      NOT NULL REFERENCES subscription_plans (id),
    paddle_transaction_id VARCHAR(100),
    status                VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    expires_at            TIMESTAMP WITH TIME ZONE NOT NULL,
    reminder_sent_at      TIMESTAMP WITH TIME ZONE,
    created_at            TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at            TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_checkout_intents_user_id    ON checkout_intents (user_id);
CREATE INDEX IF NOT EXISTS idx_checkout_intents_status     ON checkout_intents (status);
CREATE INDEX IF NOT EXISTS idx_checkout_intents_expires_at ON checkout_intents (expires_at);

-- 3. subscription_audit_logs: immutable audit trail for all subscription lifecycle events
CREATE TABLE IF NOT EXISTS subscription_audit_logs
(
    id                     BIGSERIAL PRIMARY KEY,
    tenant_subscription_id BIGINT       REFERENCES tenant_subscriptions (id),
    actor_type             VARCHAR(20)  NOT NULL,   -- USER | ADMIN | SYSTEM | PADDLE
    actor_id               VARCHAR(100),
    event_type             VARCHAR(100) NOT NULL,
    old_value              TEXT,
    new_value              TEXT,
    paddle_event_id        VARCHAR(100),
    ip_address             VARCHAR(50),
    created_at             TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_audit_logs_subscription_id ON subscription_audit_logs (tenant_subscription_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_event_type      ON subscription_audit_logs (event_type);
CREATE INDEX IF NOT EXISTS idx_audit_logs_created_at      ON subscription_audit_logs (created_at);

-- 4. refund_requests: user-initiated refund requests pending admin review
CREATE TABLE IF NOT EXISTS refund_requests
(
    id                     BIGSERIAL PRIMARY KEY,
    tenant_subscription_id BIGINT      NOT NULL REFERENCES tenant_subscriptions (id),
    user_id                BIGINT      NOT NULL,
    reason                 TEXT        NOT NULL,
    status                 VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    admin_notes            TEXT,
    reviewed_by            BIGINT,
    reviewed_at            TIMESTAMP WITH TIME ZONE,
    created_at             TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at             TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_refund_requests_user_id ON refund_requests (user_id);
CREATE INDEX IF NOT EXISTS idx_refund_requests_status  ON refund_requests (status);
