-- V13: Payment package schema
-- Adds Paddle billing support: payment details table, payment events, and
-- plan/subscription fields for provisioning and product type.

-- 1. Add product_type and paddle_price_id to subscription plans
alter table subscription_plans
    add column if not exists product_type  varchar(20) not null default 'STANDALONE',
    add column if not exists paddle_price_id varchar(100);

-- 2. Add provisioning_status to tenant subscriptions
alter table tenant_subscriptions
    add column if not exists provisioning_status varchar(20) not null default 'PENDING';

-- 3. Subscription payment details (Paddle-specific data, provider-agnostic table name)
create table if not exists subscription_payment_details
(
    id                       bigserial primary key,

    tenant_subscription_id   bigint       not null unique references tenant_subscriptions (id),
    payment_provider         varchar(20)  not null,

    paddle_subscription_id   varchar(100) unique,
    paddle_customer_id       varchar(100),

    created_at               timestamp with time zone default current_timestamp not null,
    updated_at               timestamp with time zone default current_timestamp not null
);

create index if not exists idx_spd_paddle_subscription_id
    on subscription_payment_details (paddle_subscription_id);

-- 4. Payment events — audit log + idempotency for webhooks
create table if not exists payment_events
(
    id           bigserial primary key,
    event_id     varchar(100) not null unique,
    provider     varchar(20)  not null,
    event_type   varchar(100) not null,
    payload      text         not null,
    processed_at timestamp with time zone not null
);

create index if not exists idx_payment_events_event_id
    on payment_events (event_id);

create index if not exists idx_payment_events_event_type
    on payment_events (event_type);
