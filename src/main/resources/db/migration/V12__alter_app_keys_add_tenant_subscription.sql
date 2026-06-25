-- Add tenant_subscription_id FK
alter table lofi_connect_app_keys
    add column if not exists tenant_subscription_id bigint not null references tenant_subscriptions (id);

create index if not exists idx_app_key_tenant_subscription_id
    on lofi_connect_app_keys (tenant_subscription_id);
