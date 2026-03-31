# Usage Enforcement & API Tracking Documentation

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Request Lifecycle](#request-lifecycle)
4. [Data Model](#data-model)
5. [Database Schema](#database-schema)
6. [Components](#components)
   - [AppKeyUsageFilter](#appkeyusagefilter)
   - [UsageEnforcementService](#usageenforcementservice)
   - [Quota Window Logic](#quota-window-logic)
7. [Error Responses](#error-responses)
8. [Limit Key Units](#limit-key-units)
9. [End-to-End Example](#end-to-end-example)
10. [File Reference](#file-reference)

---

## Overview

Every request to `/api/v1/ghl/**` passes through a dedicated enforcement pipeline **before** it reaches any controller. The pipeline answers three questions on every call:

| # | Question | Failure Response |
|---|---|---|
| 1 | Is the App Key valid and active? | `401 Unauthorized` |
| 2 | Does the tenant have an active subscription? | `403 Forbidden` |
| 3 | Are the tenant's quotas within limits? | `429 Too Many Requests` |

If all checks pass, the request is forwarded to the controller. After the response is sent, usage counters are incremented and the call is logged — **without ever blocking the response**.

---

## Architecture

```
Client Request  (X-App-Key: <key>)
        │
        ▼
┌───────────────────────────────────┐
│        Spring Filter Chain        │
│                                   │
│  1. AppKeyUsageFilter             │  ◄── only fires for /api/v1/ghl/**
│     ├── validate app key          │
│     ├── check subscription        │
│     ├── enforce quotas            │
│     └── [on pass] → next filter   │
│                                   │
│  2. JwtAuthenticationFilter       │  ◄── skips /api/v1/ghl/** (permitAll)
└───────────────────────────────────┘
        │
        ▼
┌───────────────────────────────────┐
│      GHL Controller               │
│  e.g. ContactController           │
│       CalendarController          │
│       ConversationsController     │
└───────────────────────────────────┘
        │
        ▼  (response committed)
AppKeyUsageFilter (post-processing)
  ├── increment usage counters
  └── save API log entry
```

### Key Design Decisions

- **Filter, not AOP** — the enforcement runs as a Servlet filter so it fires before Spring Security and Spring MVC, allowing it to short-circuit with a JSON response at the lowest possible cost.
- **Post-processing is best-effort** — usage recording happens after `chain.doFilter()`. Any exception during recording is caught and logged; it never affects the client response.
- **App Key drives identity** — GHL endpoints are `permitAll()` in Spring Security. Authentication is exclusively through the `X-App-Key` header. The tenant is resolved from the app key's `createdBy` field.
- **Quota is tenant-level** — limits come from `TenantSubscriptionLimitEntity`, which belongs to the tenant's subscription plan, not to a specific app key. A tenant with 3 app keys shares one quota pool.

---

## Request Lifecycle

```
┌──────────────────────────────────────────────────────────────────┐
│ AppKeyUsageFilter.doFilterInternal                               │
│                                                                  │
│  Read header: X-App-Key                                          │
│       │                                                          │
│       ├── null / blank ──────────────────────► 401 MISSING_APP_KEY
│       │                                                          │
│       ▼                                                          │
│  UsageEnforcementService.enforce(appKeyValue)                    │
│       │                                                          │
│       ├─ Step 1: AppKeyService.getAppKeyEntity()                 │
│       │       └── not found / inactive ───────► 401 INVALID_APP_KEY
│       │                                                          │
│       ├─ Step 2: UserService.getUserById(appKey.createdBy)       │
│       │                                                          │
│       ├─ Step 3: findFirstByTenantAndStatusAndEndAtAfter(ACTIVE) │
│       │       └── not found / expired ────────► 403 SUBSCRIPTION_INVALID
│       │                                                          │
│       ├─ Step 4: load TenantSubscriptionLimits                   │
│       │                                                          │
│       └─ Step 5: for each time-based limit:                      │
│               ├── getOrCreate TenantUsage                        │
│               ├── windowEnd < now? → reset count                 │
│               └── usageCount >= limitValue? ───► 429 QUOTA_EXCEEDED
│                                                                  │
│  [all checks pass]                                               │
│       │                                                          │
│       ▼                                                          │
│  chain.doFilter(request, response)  ◄── controller executes     │
│       │                                                          │
│       ▼  (response committed)                                    │
│  UsageEnforcementService.recordUsage()                           │
│       ├── increment usageCount on each TenantUsage               │
│       └── save TenantApiLogEntity                                │
└──────────────────────────────────────────────────────────────────┘
```

---

## Data Model

### Entity Overview

```
LofiConnectAppKeyEntity (app key)
        │
        │ createdBy ──────────────────► UserEntity (tenant)
        │                                     │
        │                              has subscription
        │                                     │
        │                                     ▼
        │                        TenantSubscriptionEntity
        │                                     │
        │                              has limits (snapshot)
        │                                     │
        │                                     ▼
        │                       TenantSubscriptionLimitEntity
        │                              │           │
        │                              │           ▼
        │                              │      LimitKeyEntity
        │                              │      (unit: RPM / MONTHLY)
        │                              │
        ▼                              ▼
TenantUsageEntity ◄──── tracks usage per (subscription + limitKey + appKey)
        (windowStart / windowEnd — time window for current count)

TenantApiLogEntity ◄──── one row per request (tenant + appKey + endpoint + status)
```

---

### TenantUsageEntity

Tracks the rolling usage counter for a given subscription and limit type within a time window.

| Field | Type | Description |
|---|---|---|
| `id` | `bigint` | Primary key |
| `tenant_subscription_id` | `bigint FK` | The subscription this usage belongs to (CASCADE DELETE) |
| `app_key_id` | `bigint FK` | The app key that last incremented this counter |
| `limit_key_id` | `bigint FK` | Which limit is being tracked (e.g. `requests_per_minute`) |
| `usage_count` | `bigint` | Current number of calls in the active window. Default `0` |
| `window_start` | `timestamptz` | Start of the current counting window |
| `window_end` | `timestamptz` | End of the current counting window |
| *(audit fields)* | — | `created_by`, `updated_by`, `version`, `is_active`, `is_deleted`, etc. |

**Unique constraint:** `(tenant_subscription_id, limit_key_id)` — one counter row per subscription per limit type.

**Window reset rule:**

```
if now() > windowEnd:
    usageCount = 0
    windowStart = now()
    windowEnd   = now() + windowDuration   (depends on unit)
```

---

### TenantApiLogEntity

An append-only record of every API call that passed the App Key check. Written after the response is committed.

| Field | Type | Description |
|---|---|---|
| `id` | `bigint` | Primary key |
| `tenant_id` | `bigint FK` | The tenant who made the call |
| `app_key_id` | `bigint FK` | The specific app key used |
| `endpoint` | `varchar(255)` | Request URI, e.g. `/api/v1/ghl/contacts` |
| `method` | `varchar(10)` | HTTP method: `GET`, `POST`, `PUT`, `DELETE` |
| `status_code` | `integer` | HTTP response code returned to the client |
| *(audit fields)* | — | `created_at` records the exact call timestamp |

> **Note:** Only requests that pass App Key validation are logged. Requests rejected at the key level (`401`) are never logged.

---

## Database Schema

### V6 Migration — `V6__tenant_usage_create.sql`

```sql
-- tenant_usage: one row per (tenant_subscription, limit_key)
create table if not exists tenant_usage (
    id                      bigserial primary key,
    tenant_subscription_id  bigint not null references tenant_subscriptions(id) on delete cascade,
    app_key_id              bigint not null references lofi_connect_app_key(id),
    limit_key_id            bigint not null references limit_keys(id),
    usage_count             bigint default 0 not null,
    window_start            timestamptz not null,
    window_end              timestamptz not null,
    -- audit columns ...
    unique (tenant_subscription_id, limit_key_id)
);

-- tenant_api_logs: append-only call log
create table if not exists tenant_api_logs (
    id           bigserial primary key,
    tenant_id    bigint not null references users(id),
    app_key_id   bigint not null references lofi_connect_app_key(id),
    endpoint     varchar(255),
    method       varchar(10),
    status_code  integer,
    -- audit columns ...
);

-- Indexes for dashboard / analytics queries
create index idx_tenant_api_logs_tenant_id  on tenant_api_logs (tenant_id);
create index idx_tenant_usage_tenant_limit  on tenant_usage    (tenant_subscription_id, limit_key_id);
```

---

## Components

### AppKeyUsageFilter

**File:** `commons/filter/AppKeyUsageFilter.java`
**Type:** `OncePerRequestFilter` (Spring)
**Scope:** Only active for paths matching `/api/v1/ghl/**`

```
Header: X-App-Key: lc_abc123...
```

#### Responsibilities

1. **Path guard** — `shouldNotFilter()` returns `true` for all non-GHL paths. Zero overhead on JWT-authenticated routes.
2. **Header extraction** — reads `X-App-Key` from the request header.
3. **Gate** — calls `UsageEnforcementService.enforce()`. Any thrown exception is translated to a JSON error response.
4. **Pass-through** — on success, calls `chain.doFilter()` to forward the request.
5. **Post-processing** — after the response is written, calls `recordUsage()` in a try-catch so a logging failure never reaches the client.

#### Error Translation

| Exception thrown | HTTP Status | Error Code |
|---|---|---|
| Header is null/blank | `401` | `MISSING_APP_KEY` |
| `AppKeyInvalidException` | `401` | `INVALID_APP_KEY` |
| `SubscriptionInvalidException` | `403` | `SUBSCRIPTION_INVALID` |
| `QuotaExceededException` | `429` | `QUOTA_EXCEEDED` |

---

### UsageEnforcementService

**File:** `commons/service/UsageEnforcementService.java`
**Implementation:** `commons/serviceImpl/UsageEnforcementServiceImpl.java`

#### `enforce(appKeyValue): EnforcementResult`

Runs all pre-request checks in a single `@Transactional` call.

```
Step 1 — Resolve App Key
    AppKeyService.getAppKeyEntity(appKeyValue)
    → RuntimeException caught → rethrown as AppKeyInvalidException (401)

Step 2 — Resolve Tenant
    UserService.getUserById(appKey.getCreatedBy())
    The tenant is the user who owns the app key.

Step 3 — Find Active Subscription
    TenantSubscriptionRepository
        .findFirstByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
            tenant, "ACTIVE", OffsetDateTime.now(), true, false)
    → empty → SubscriptionInvalidException (403)

Step 4 — Load Subscription Limits
    TenantSubscriptionLimitRepository
        .findAllByTenantSubscriptionEntityAndIsActiveAndIsDeleted(subscription, true, false)

Step 5 — Enforce Each Quota
    For each limit where limitKey.unit is time-based (RPM or MONTHLY):
        a) getOrCreateUsage(tenant, appKey, limitKey)
        b) needsReset()? → resetUsage()
        c) usageCount >= limitValue → QuotaExceededException (429)

Returns: EnforcementResult(tenant, appKey, subscription)
```

#### `recordUsage(result, endpoint, method, statusCode)`

Runs after the response is committed. Also `@Transactional`.

```
For each time-based limit in the subscription:
    → load TenantUsage
    → usageCount += 1
    → appKeyEntity = current app key (audit trail of last key to increment)
    → save

Save TenantApiLogEntity:
    → tenantEntity, appKeyEntity, endpoint, method, statusCode
```

#### `EnforcementResult` (record)

A lightweight value object passed from `enforce()` to `recordUsage()` through the filter, avoiding repeated DB lookups.

```java
record EnforcementResult(
    UserEntity tenant,
    LofiConnectAppKeyEntity appKey,
    TenantSubscriptionEntity subscription
)
```

---

### Quota Window Logic

#### `isTimeBasedUnit(unit)`

Only limits whose `limit_key.unit` is one of the following participate in per-request quota counting:

| Unit value | Meaning | Window duration |
|---|---|---|
| `RPM` | Requests per minute | 1 minute |
| `MONTHLY` | Requests per calendar month | Until end of current month |

Any other unit (e.g. `keys`) is skipped in the per-request enforcement loop. Those limits (like `max_api_keys`) are enforced at creation time, not per request.

#### `needsReset(usage, unit)` — Window Expiry

```
RPM:
    usage.windowEnd < now()  →  reset

MONTHLY:
    YearMonth(usage.windowEnd) < YearMonth(now())  →  reset

any other unit:
    never reset automatically
```

#### `resetUsage(usage)`

```
usage.usageCount = 0
usage.windowStart = now()
usage.windowEnd   = computed from unit:
    RPM     → now() + 1 minute
    MONTHLY → first day of next month at 00:00:00
```

#### `getOrCreateUsage(tenant, appKey, limitKey)`

```
TenantUsageRepository.findByTenantSubscriptionAndLimitKey(subscription, limitKey)
    → found  → return existing record
    → absent → create new:
                usageCount = 0
                windowStart = now()
                windowEnd   = computed from unit
                save → return
```

---

## Error Responses

All error responses from the filter use the shared `ApiErrorResponse` shape:

```json
{
  "request_id": null,
  "status": 401,
  "error": "INVALID_APP_KEY",
  "message": "Invalid or inactive app key."
}
```

### 401 — App Key Missing

```
Trigger: X-App-Key header is absent or blank
```
```json
{
  "status": 401,
  "error": "MISSING_APP_KEY",
  "message": "Request header 'X-App-Key' is required."
}
```

### 401 — App Key Invalid

```
Trigger: Key not found in DB, is_active = false, or is_deleted = true
```
```json
{
  "status": 401,
  "error": "INVALID_APP_KEY",
  "message": "Invalid or inactive app key."
}
```

### 403 — Subscription Invalid

```
Trigger: Tenant has no subscription with status=ACTIVE and endAt in the future
```
```json
{
  "status": 403,
  "error": "SUBSCRIPTION_INVALID",
  "message": "No active subscription found. Please subscribe to a plan."
}
```

### 429 — Quota Exceeded

```
Trigger: usageCount >= limitValue for any RPM or MONTHLY limit
```
```json
{
  "status": 429,
  "error": "QUOTA_EXCEEDED",
  "message": "Quota exceeded for [requests_per_minute]. Limit: 1000 RPM."
}
```

---

## Limit Key Units

When creating limit keys via the admin API, the `unit` field controls enforcement behaviour:

| `unit` value | Enforced in filter? | Reset period | Example `limit_key` |
|---|---|---|---|
| `RPM` | Yes | Every 60 seconds | `requests_per_minute` |
| `MONTHLY` | Yes | Every calendar month | `requests_per_month` |
| `keys` | No (creation-time check) | Never | `max_api_keys` |
| *(null or other)* | No | Never | custom admin-only limits |

---

## End-to-End Example

### Setup (Admin)

```
1. Create limit keys:
   POST /api/v1/admins/limit-keys/
   { "limit_key": "requests_per_minute", "unit": "RPM",     "description": "RPM cap" }
   { "limit_key": "requests_per_month",  "unit": "MONTHLY", "description": "Monthly cap" }

2. Create a PRO plan with limits:
   POST /api/v1/admins/subscription-plans/
   {
     "name": "PRO", "currency_id": 1, "price": 29.99,
     "billing_cycle": "MONTHLY", "duration_in_days": 30,
     "limits": [
       { "limit_key_id": 1, "limit_value": 1000 },   ← 1000 RPM
       { "limit_key_id": 2, "limit_value": 50000 }   ← 50,000 / month
     ]
   }
```

### Tenant Subscribes

```
POST /api/v1/subscriptions/tenant-subscriptions
Authorization: Bearer <jwt>
{ "subscription_plan_id": 1, "auto_renew": true }

→ TenantSubscription created: status=ACTIVE, endAt = now + 30 days
→ TenantSubscriptionLimits copied from plan (snapshot)
```

### Tenant Makes a GHL API Call

```
GET /api/v1/ghl/contacts/abc123
X-App-Key: lc_xyz789
```

**Filter executes:**

```
1. Header present ✓
2. App key found, is_active=true ✓
3. Tenant (id=42) has ACTIVE subscription, endAt=2026-05-01 ✓
4. Load limits: [requests_per_minute → 1000, requests_per_month → 50000]
5. RPM limit:
   TenantUsage for (subscription, requests_per_minute):
     windowStart=2026-04-01T10:00:00Z, windowEnd=2026-04-01T10:01:00Z
     usageCount = 743
   743 < 1000 ✓ — not exceeded
6. MONTHLY limit:
   TenantUsage for (subscription, requests_per_month):
     windowStart=2026-04-01T00:00:00Z, windowEnd=2026-05-01T00:00:00Z
     usageCount = 12,304
   12304 < 50000 ✓ — not exceeded
7. → forward to ContactController
```

**After response (200 OK):**

```
RPM   usageCount: 743 → 744
MONTHLY usageCount: 12304 → 12305
TenantApiLog saved: { tenant=42, appKey=lc_xyz789, endpoint=/api/v1/ghl/contacts/abc123, method=GET, statusCode=200 }
```

### Tenant Hits RPM Limit

```
GET /api/v1/ghl/contacts/abc123
X-App-Key: lc_xyz789

RPM usageCount = 1000 (>= limit 1000)

← 429 Too Many Requests
{
  "status": 429,
  "error": "QUOTA_EXCEEDED",
  "message": "Quota exceeded for [requests_per_minute]. Limit: 1000 RPM."
}
```

After 1 minute, `windowEnd` passes → next request resets the counter to 0 and proceeds normally.

---

## File Reference

### New Files

| File | Package | Description |
|---|---|---|
| `AppKeyUsageFilter.java` | `commons.filter` | `OncePerRequestFilter` for `/api/v1/ghl/**` |
| `UsageEnforcementService.java` | `commons.service` | Interface: `enforce()`, `recordUsage()`, `EnforcementResult` record |
| `UsageEnforcementServiceImpl.java` | `commons.serviceImpl` | Full enforcement logic, window management |
| `TenantUsageRepository.java` | `commons.repository` | `findByTenantSubscriptionEntityAndLimitKeyEntity()` |
| `TenantApiLogRepository.java` | `commons.repository` | Simple save for log entries |
| `AppKeyInvalidException.java` | `commons.exception` | Thrown on invalid/inactive app key → 401 |
| `SubscriptionInvalidException.java` | `commons.exception` | Thrown when no active subscription → 403 |
| `QuotaExceededException.java` | `commons.exception` | Thrown when any quota is exceeded → 429 |
| `V6__tenant_usage_create.sql` | `db/migration` | Creates `tenant_usage` and `tenant_api_logs` tables |

### Modified Files

| File | Change |
|---|---|
| `TenantSubscriptionRepository.java` | Added `findFirstByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted()` |
| `SecurityConfig.java` | Registered `AppKeyUsageFilter` before `JwtAuthenticationFilter` |

### Pre-existing Files Used

| File | Role in enforcement |
|---|---|
| `TenantUsageEntity.java` | Usage counter per subscription + limit key + app key |
| `TenantApiLogEntity.java` | Call log record |
| `TenantSubscriptionEntity.java` | Holds status, endAt — checked for active subscription |
| `TenantSubscriptionLimitEntity.java` | Snapshot of plan limits for this tenant |
| `LimitKeyEntity.java` | Provides `unit` field to determine reset period |
| `LofiConnectAppKeyEntity.java` | Validated on every request; `createdBy` resolves the tenant |
| `AppKeyService.getAppKeyEntity()` | Looks up key by value, active + not deleted |
| `UserService.getUserById()` | Resolves tenant from key owner ID |
| `ApiErrorResponse.java` | Shared error response shape written by filter |
