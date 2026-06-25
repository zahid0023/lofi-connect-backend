create table if not exists tenant_subscriptions
(
    id                   bigserial primary key,

    user_id              bigint                                             not null references users (id),
    subscription_plan_id bigint                                             not null references subscription_plans (id),

    status               varchar(20)                                        not null,
    start_date           timestamp with time zone                           not null,
    end_date             timestamp with time zone,
    trial_ends_at        timestamp with time zone,

    created_by           bigint                                             not null references users (id),
    created_at           timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by           bigint                                             not null references users (id),
    updated_at           timestamp with time zone default CURRENT_TIMESTAMP not null,
    version              bigint                   default 0                 not null,
    is_active            boolean                  default true              not null,
    is_deleted           boolean                  default false             not null,
    deleted_by           bigint,
    deleted_at           timestamp with time zone
);

create index if not exists idx_tenant_subscriptions_user_id
    on tenant_subscriptions (user_id);

create index if not exists idx_tenant_subscriptions_status
    on tenant_subscriptions (status);

create index if not exists idx_tenant_subscriptions_plan_id
    on tenant_subscriptions (subscription_plan_id);
