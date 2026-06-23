create table if not exists lofi_connect_app_keys
(
    id         bigserial primary key,

    app_key    text                                               not null unique,
    name       text                                               not null default '',

    created_by bigint                                             not null references users (id),
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint                                             not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version    bigint                   default 0                 not null,
    is_active  boolean                  default true              not null,
    is_deleted boolean                  default false             not null,
    deleted_by bigint,
    deleted_at timestamp with time zone,
    unique (name, created_by)
);