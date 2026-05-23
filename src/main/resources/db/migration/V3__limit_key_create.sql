create table if not exists limit_keys
(
    id          bigserial primary key,

    code        varchar(100) not null unique, -- API_KEYS, API_REQUESTS
    name        varchar(150) not null,        -- Human readable name
    description text,

    -- Defines how this limit behaves
    data_type   varchar(50)  not null,        -- INTEGER, BOOLEAN, DECIMAL
    category    varchar(50)  not null,        -- USAGE, FEATURE, RESOURCE

    unit        varchar(50),                  -- COUNT, REQUESTS, KEYS

    is_active   boolean      not null default true,

    created_by  bigint       not null,
    created_at  timestamptz  not null default now(),
    updated_by  bigint       not null,
    updated_at  timestamptz  not null default now(),

    version     bigint       not null default 0,

    is_deleted  boolean      not null default false,
    deleted_by  bigint,
    deleted_at  timestamptz
);