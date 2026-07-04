-- V16: API usage logs
-- Tracks every API call made through an app key:
-- which platform (e.g. GHL), endpoint, HTTP method, response status, latency, and analytics metadata.

CREATE TABLE IF NOT EXISTS api_usage_logs
(
    id                   BIGSERIAL PRIMARY KEY,

    -- Core identity
    app_key_id           BIGINT REFERENCES lofi_connect_app_keys (id) NOT NULL,
    platform             VARCHAR(50)                                  NOT NULL CHECK (platform IN ('GHL')),
    request_id           VARCHAR(100),                                                        -- correlation ID for tracing

    -- Request
    http_method          VARCHAR(10)                                  NOT NULL,
    endpoint             VARCHAR(500)                                 NOT NULL,               -- raw path with actual IDs
    endpoint_pattern     VARCHAR(500),                                                        -- normalised path e.g. /contacts/{id}
    ip_address           INET,                                                                -- caller IP for abuse detection
    user_agent           VARCHAR(255),                                                        -- SDK / browser / curl
    request_size_bytes   INT,                                                                 -- payload size in bytes

    -- Response
    response_status      INT,
    response_size_bytes  INT,                                                                 -- response body size in bytes
    response_time_ms     BIGINT,                                                              -- total round-trip ms
    is_error             BOOLEAN                                      NOT NULL DEFAULT FALSE, -- true when response_status >= 400
    error_code           VARCHAR(100),                                                        -- platform-specific error code
    rate_limit_remaining INT,                                                                 -- X-RateLimit-Remaining from platform

    requested_at         TIMESTAMP WITH TIME ZONE                     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- AuditableEntity columns
    created_by           BIGINT,
    created_at           TIMESTAMP WITH TIME ZONE                              DEFAULT CURRENT_TIMESTAMP,
    updated_by           BIGINT,
    updated_at           TIMESTAMP WITH TIME ZONE                              DEFAULT CURRENT_TIMESTAMP,
    version              BIGINT                                                DEFAULT 0,
    is_active            BOOLEAN                                               DEFAULT TRUE NOT NULL,
    is_deleted           BOOLEAN                                               DEFAULT FALSE NOT NULL,
    deleted_by           BIGINT,
    deleted_at           TIMESTAMP WITH TIME ZONE
);

-- Lookups
CREATE INDEX IF NOT EXISTS idx_api_usage_app_key_id ON api_usage_logs (app_key_id);
CREATE INDEX IF NOT EXISTS idx_api_usage_platform ON api_usage_logs (platform);
CREATE INDEX IF NOT EXISTS idx_api_usage_requested_at ON api_usage_logs (requested_at);
-- Analytics composites
CREATE INDEX IF NOT EXISTS idx_api_usage_app_key_at ON api_usage_logs (app_key_id, requested_at DESC);
CREATE INDEX IF NOT EXISTS idx_api_usage_endpoint_pattern ON api_usage_logs (endpoint_pattern, requested_at DESC);
CREATE INDEX IF NOT EXISTS idx_api_usage_is_error ON api_usage_logs (is_error, requested_at DESC);
CREATE INDEX IF NOT EXISTS idx_api_usage_ip ON api_usage_logs (ip_address);
