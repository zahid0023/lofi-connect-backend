-- 1. Currencies
create table if not exists currencies
(
    id         bigserial primary key,
    code       varchar(10)                                        not null,
    symbol     varchar(10)                                        not null,

    created_by bigint                                             not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint                                             not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version    bigint                   default 0                 not null,
    is_active  boolean                  default true              not null,
    is_deleted boolean                  default false             not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

-- 2. Subscription Models
create table if not exists subscription_models
(
    id                bigserial primary key,
    name              varchar(100)                                       not null unique,
    description       text                                               not null,
    details           text[]                                             not null default '{}'::text[],
    max_app_keys      integer                                            not null check (max_app_keys >= 0),
    api_calls_quota   bigint                                             not null check (api_calls_quota >= 0),
    quota_period_days integer                                            not null check (quota_period_days >= 0),

    price             decimal                                            not null default 0,
    currency_id       bigint references currencies (id),

    created_by        bigint                                             not null,
    created_at        timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by        bigint                                             not null,
    updated_at        timestamp with time zone default CURRENT_TIMESTAMP not null,
    version           bigint                   default 0                 not null,
    is_active         boolean                  default true              not null,
    is_deleted        boolean                  default false             not null,
    deleted_by        bigint,
    deleted_at        timestamp with time zone
);