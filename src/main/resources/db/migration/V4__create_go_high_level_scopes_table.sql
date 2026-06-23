create table if not exists go_high_level_scopes
(
    id          bigserial primary key,

    name        varchar(50) not null unique ,
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