-- 1. Tenant Subscriptions
create table if not exists tenant_subscriptions
(
    id                   bigserial primary key,
    tenant_id            bigint                                             not null references users (id),
    subscription_plan_id bigint                                             not null references subscription_plans (id),
    status               varchar(30)                                        not null,
    start_at             timestamp with time zone                           not null,
    end_at               timestamp with time zone                           not null,
    auto_renew           boolean                  default true,
    price                numeric(10, 2)                                     not null,
    currency_id          bigint                                             not null,

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

-- 2. Tenant Subscription Limits (snapshot of plan limits at subscription time)
create table if not exists tenant_subscription_limits
(
    id                      bigserial primary key,
    tenant_subscription_id  bigint                                             not null references tenant_subscriptions (id) on delete cascade,
    limit_key_id            bigint                                             not null references limit_keys (id),
    limit_value             bigint                                             not null,

    created_by              bigint                                             not null,
    created_at              timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by              bigint                                             not null,
    updated_at              timestamp with time zone default CURRENT_TIMESTAMP not null,
    version                 bigint                   default 0                 not null,
    is_active               boolean                  default true              not null,
    is_deleted              boolean                  default false             not null,
    deleted_by              bigint,
    deleted_at              timestamp with time zone
);
