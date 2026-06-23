create table if not exists go_high_level_tokens
(
    id               bigserial primary key,

    app_key_id       bigint references lofi_connect_app_keys (id),

    access_token     text        not null,
    token_type       varchar(50) not null,
    expires_in       integer     not null,
    refresh_token    text        not null,
    refresh_token_id text        not null,
    company_id       text        not null default '',
    subaccount_name  text        not null default '',
    scopes           text        not null default '',
    user_type        varchar(255),
    user_id          text,
    location_id      text,

    version          bigint               default 0 not null,
    created_at       timestamp with time zone,
    is_active        boolean              default true not null,
    is_deleted       boolean              default false not null
);