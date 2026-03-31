-- 1. Tenant Usage — rolling counters per tenant subscription + limit key
create table if not exists tenant_usage
(
    id                     bigserial primary key,
    tenant_subscription_id bigint                                             not null references tenant_subscriptions (id) on delete cascade,
    app_key_id             bigint                                             not null references lofi_connect_app_key (id),
    limit_key_id           bigint                                             not null references limit_keys (id),
    usage_count            bigint                   default 0                 not null,
    window_start           timestamp with time zone                           not null,
    window_end             timestamp with time zone                           not null,

    created_by             bigint                                             not null,
    created_at             timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by             bigint                                             not null,
    updated_at             timestamp with time zone default CURRENT_TIMESTAMP not null,
    version                bigint                   default 0                 not null,
    is_active              boolean                  default true              not null,
    is_deleted             boolean                  default false             not null,
    deleted_by             bigint,
    deleted_at             timestamp with time zone,

    unique (tenant_subscription_id, limit_key_id)
);

-- 2. Tenant API Logs — append-only log of every GHL API call
create table if not exists tenant_api_logs
(
    id           bigserial primary key,
    tenant_id    bigint                                             not null references users (id),
    app_key_id   bigint                                             not null references lofi_connect_app_key (id),
    endpoint     varchar(255),
    method       varchar(10),
    status_code  integer,

    created_by   bigint                                             not null,
    created_at   timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by   bigint                                             not null,
    updated_at   timestamp with time zone default CURRENT_TIMESTAMP not null,
    version      bigint                   default 0                 not null,
    is_active    boolean                  default true              not null,
    is_deleted   boolean                  default false             not null,
    deleted_by   bigint,
    deleted_at   timestamp with time zone
);

create index if not exists idx_tenant_api_logs_tenant_id on tenant_api_logs (tenant_id);
create index if not exists idx_tenant_usage_subscription_limit on tenant_usage (tenant_subscription_id, limit_key_id);
