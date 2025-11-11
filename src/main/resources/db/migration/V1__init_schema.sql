CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    email    TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS lofi_connect_app_key
(
    id         BIGSERIAL PRIMARY KEY,
    app_key    TEXT                     NOT NULL,
    created_by TEXT                     NOT NULL,
    updated_by TEXT                     NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    is_active  BOOLEAN                           DEFAULT TRUE,
    user_id    BIGINT REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS go_high_level_tokens
(
    id               BIGSERIAL PRIMARY KEY,
    app_key_id       BIGINT REFERENCES lofi_connect_app_key (id),
    access_token     TEXT                     NOT NULL,
    token_type       VARCHAR(50)              NOT NULL,
    expires_in       INTEGER                  NOT NULL,
    refresh_token    TEXT                     NOT NULL,
    refresh_token_id TEXT                     NOT NULL,
    scopes           TEXT                     NOT NULL,
    company_id       TEXT                     NOT NULL,
    location_id      TEXT,
    user_id          TEXT                     NOT NULL,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT (CURRENT_TIMESTAMP AT TIME ZONE 'UTC'),
    user_type        TEXT                     NOT NULL,
    is_active        BOOLEAN                  NOT NULL DEFAULT FALSE
);