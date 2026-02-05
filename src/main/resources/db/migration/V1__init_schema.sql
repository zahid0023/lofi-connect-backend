create table if not exists roles
(
    id         bigserial
        primary key,
    name       varchar(50)                                        not null
        unique,
    created_by bigint                                             not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint                                             not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version    bigint                   default 0                 not null,
    is_active  boolean                  default true              not null,
    is_deleted boolean                  default false             not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

create table if not exists permissions
(
    id          bigserial
        primary key,
    name        varchar(100)                                       not null
        unique,
    description varchar(255),
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

create table if not exists users
(
    id         bigserial
        primary key,
    username   varchar(255)                                       not null
        unique,
    password   varchar(255)                                       not null,
    role_id    bigint                                             not null
        references roles
            on delete restrict,
    enabled    boolean                  default true              not null,
    locked     boolean                  default false             not null,
    expired    boolean                  default false             not null,
    created_by bigint                                             not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint                                             not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version    bigint                   default 0                 not null,
    is_active  boolean                  default true              not null,
    is_deleted boolean                  default false             not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

create table if not exists refresh_tokens
(
    id         bigserial
        primary key,
    user_id    bigint                                             not null
        references users
            on delete cascade,
    token      varchar(500)                                       not null,
    expires_at timestamp with time zone                           not null,
    created_by bigint                                             not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint                                             not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version    bigint                   default 0                 not null,
    is_active  boolean                  default true              not null,
    is_deleted boolean                  default false             not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

create table if not exists user_permissions
(
    id            bigserial
        primary key,
    user_id       bigint                                             not null
        references users
            on delete cascade,
    permission_id bigint                                             not null
        references permissions
            on delete cascade,
    created_by    bigint                                             not null,
    created_at    timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by    bigint                                             not null,
    updated_at    timestamp with time zone default CURRENT_TIMESTAMP not null,
    version       bigint                   default 0                 not null,
    is_active     boolean                  default true              not null,
    is_deleted    boolean                  default false             not null,
    deleted_by    bigint,
    deleted_at    timestamp with time zone,
    unique (user_id, permission_id)
);

insert into roles(name, created_by, updated_by)
values ('USER', 1, 1)
on conflict do nothing;

CREATE TABLE IF NOT EXISTS lofi_connect_app_key
(
    id              bigserial PRIMARY KEY,

    app_key         text                                               not null,
    connection_name text                                               not null default '',
    company_id      text                                               not null default '',
    subaccount_name text                                               not null default '',
    scopes          text                                               not null default '',

    created_by      bigint                                             not null references users (id),
    created_at      timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by      bigint                                             not null,
    updated_at      timestamp with time zone default CURRENT_TIMESTAMP not null,
    version         bigint                   default 0                 not null,
    is_active       boolean                  default true              not null,
    is_deleted      boolean                  default false             not null,
    deleted_by      bigint,
    deleted_at      timestamp with time zone
);

CREATE TABLE IF NOT EXISTS go_high_level_tokens
(
    id               BIGSERIAL PRIMARY KEY,

    app_key_id       BIGINT REFERENCES lofi_connect_app_key (id),

    access_token     TEXT                  NOT NULL,
    token_type       VARCHAR(50)           NOT NULL,
    expires_in       INTEGER               NOT NULL,
    refresh_token    TEXT                  NOT NULL,
    refresh_token_id TEXT                  NOT NULL,

    version          bigint  default 0     not null,
    is_active        boolean default true  not null,
    is_deleted       boolean default false not null
);

create table if not exists scopes
(
    id          bigserial primary key,
    name        varchar(50),
    description varchar(255),

    created_by  bigint                                             not null references users (id),
    created_at  timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by  bigint                                             not null references users (id),
    updated_at  timestamp with time zone default CURRENT_TIMESTAMP not null,
    version     bigint                   default 0                 not null,
    is_active   boolean                  default true              not null,
    is_deleted  boolean                  default false             not null,
    deleted_by  bigint,
    deleted_at  timestamp with time zone
);

alter table lofi_connect_app_key
    add column if not exists user_type varchar(255) not null default '';

alter table lofi_connect_app_key
    add column if not exists user_id text not null default '';

alter table lofi_connect_app_key
    add column if not exists sub_account_id text not null default '';

alter table go_high_level_tokens
    add column if not exists created_at timestamp with time zone default CURRENT_TIMESTAMP not null;

alter table lofi_connect_app_key
    add column if not exists code text not null default '';

insert into permissions(name, description, created_by, updated_by)
values ('CREATE_SCOPE', 'Create a new scope', '0', '0')
on conflict (name) do nothing;