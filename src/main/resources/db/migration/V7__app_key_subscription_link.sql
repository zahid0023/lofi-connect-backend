-- Link each app key to the tenant subscription it was generated under.
-- Nullable to preserve existing rows; new keys will always have a value.
alter table lofi_connect_app_key
    add column if not exists tenant_subscription_id bigint
        references tenant_subscriptions(id) on delete set null;

create index if not exists idx_app_key_tenant_subscription
    on lofi_connect_app_key (tenant_subscription_id);
