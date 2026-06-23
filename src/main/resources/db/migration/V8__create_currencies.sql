create table if not exists currencies
(
    id          bigserial primary key,

    code        varchar(10)                                        not null,

    name        varchar(100)                                       not null,
    description varchar(100)                                       not null,
    symbol      varchar(10)                                        not null,

    created_by  bigint references users (id)                       not null,
    created_at  timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by  bigint references users (id)                       not null,
    updated_at  timestamp with time zone default CURRENT_TIMESTAMP not null,
    version     bigint                   default 0                 not null,
    is_active   boolean                  default true              not null,
    is_deleted  boolean                  default false             not null,
    deleted_by  bigint,
    deleted_at  timestamp with time zone
);