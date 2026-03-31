-- 1. Limit Keys
create table if not exists limit_keys
(
    id          bigserial primary key,
    limit_key   varchar(100)                                       not null,
    description text,
    unit        varchar(50),

    created_by  bigint                                             not null,
    created_at  timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by  bigint                                             not null,
    updated_at  timestamp with time zone default CURRENT_TIMESTAMP not null,
    version     bigint                   default 0                 not null,
    is_active   boolean                  default true              not null,
    is_deleted  boolean                  default false             not null,
    deleted_by  bigint,
    deleted_at  timestamp with time zone
);
