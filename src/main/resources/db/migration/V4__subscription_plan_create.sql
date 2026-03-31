-- 1. Subscription Plans
create table if not exists subscription_plans
(
    id          bigserial primary key,
    currency_id bigint                                             not null references currencies (id),
    name        varchar(100)                                       not null,
    price       numeric(10, 2)           default 0,
    description text[]                                             not null,

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

-- 2. Subscription Plan Limits
create table if not exists subscription_plan_limits
(
    id                   bigserial primary key,
    subscription_plan_id bigint                                             not null references subscription_plans (id) on delete cascade,
    limit_key_id         bigint                                             not null references limit_keys (id),
    limit_value          bigint                                             not null,

    created_by           bigint                                             not null,
    created_at           timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by           bigint                                             not null,
    updated_at           timestamp with time zone default CURRENT_TIMESTAMP not null,
    version              bigint                   default 0                 not null,
    is_active            boolean                  default true              not null,
    is_deleted           boolean                  default false             not null,
    deleted_by           bigint,
    deleted_at           timestamp with time zone
);
