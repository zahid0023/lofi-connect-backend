CREATE TABLE IF NOT EXISTS countries
(
    id          bigserial PRIMARY KEY,

    code
                varchar(10)              NOT NULL UNIQUE,
    iso3_code   varchar(10),
    phone_code  varchar(10),

    name        varchar(255)             NOT NULL,
    description text,
    sort_order  int                      NOT NULL DEFAULT 0,

    created_by  bigint                   NOT NULL,
    created_at  timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  bigint                   NOT NULL,
    updated_at  timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version     bigint                   NOT NULL DEFAULT 0,
    is_active   boolean                  NOT NULL DEFAULT true,
    is_deleted  boolean                  NOT NULL DEFAULT false,
    deleted_by  bigint,
    deleted_at  timestamp with time zone
);