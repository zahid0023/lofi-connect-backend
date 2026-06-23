-- 1. Subscription Plans
create table if not exists subscription_plans
(
    id                bigserial primary key,

    currency_id       bigint references currencies (id)                  not null,

    code              varchar(100)                                       not null unique,
    billing_cycle     varchar(20)                                        not null,
    trial_period_days integer                  default 0                 not null,
    sort_order        integer                  default 0                 not null,

    name              varchar(100)                                       not null,
    price             numeric(10, 2)           default 0                 not null,
    description       text[]                                             not null,
    is_public         boolean                  default true              not null,


    created_by        bigint references users (id)                       not null,
    created_at        timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by        bigint references users (id)                       not null,
    updated_at        timestamp with time zone default CURRENT_TIMESTAMP not null,
    version           bigint                   default 0                 not null,
    is_active         boolean                  default true              not null,
    is_deleted        boolean                  default false             not null,
    deleted_by        bigint,
    deleted_at        timestamp with time zone
);

-- 2. Subscription Plan Limits
create table if not exists subscription_plan_limits
(
    id                   bigserial primary key,

    subscription_plan_id bigint                                             not null references subscription_plans (id) on delete cascade,

    limit_key_id         bigint                                             not null references limit_keys (id),
    limit_value          bigint                                             not null,

    unique (subscription_plan_id, limit_key_id),

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

create index if not exists idx_subscription_plans_sort_order
    on subscription_plans (sort_order);

create index if not exists idx_plan_limits_limit_key_id
    on subscription_plan_limits (limit_key_id);
