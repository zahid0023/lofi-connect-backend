create table if not exists cities
(
    id          bigserial primary key,

    country_id  bigint references countries (id)                   not null,

    code        varchar(50),

    name        varchar(255)                                       not null,
    description text                     default ''                not null,
    sort_order  integer                  default 0                 not null,

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