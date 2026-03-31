# Subscription System Documentation

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Data Model](#data-model)
4. [Database Schema](#database-schema)
5. [API Reference](#api-reference)
   - [Limit Keys](#limit-keys-api)
   - [Subscription Plans](#subscription-plans-api)
   - [Tenant Subscriptions](#tenant-subscriptions-api)
6. [Business Logic](#business-logic)
7. [Request / Response Examples](#request--response-examples)
8. [Status Lifecycle](#status-lifecycle)
9. [Migration Files](#migration-files)

---

## Overview

The subscription system controls what a tenant (user) can do inside the platform. It is built on three pillars:

```
Limit Keys  ──►  Subscription Plans  ──►  Tenant Subscriptions
                  (defines limits)           (assigned to tenants)
```

| Concept | Purpose |
|---|---|
| **Limit Key** | A named, reusable limit definition (e.g. `max_api_keys`, `requests_per_minute`) |
| **Subscription Plan** | A plan (FREE / PRO / ENTERPRISE) with a price, billing cycle, duration, and a set of limits |
| **Tenant Subscription** | The record of a specific tenant subscribing to a specific plan |

**Core rule:** Limits are plan-driven. Usage is app-key-driven. Limits apply at the **tenant** level, not the individual user level.

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        REST Layer                           │
│  LimitKeyController   SubscriptionPlanController            │
│  (ADMIN only)         (ADMIN only)                          │
│                                                             │
│  TenantSubscriptionController  (/api/v1/subscriptions/...)  │
│  (USER — authenticated tenant subscribes themselves)        │
└───────────────────┬─────────────────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────────────────┐
│                      Service Layer                          │
│  LimitKeyService        SubscriptionPlanService             │
│  TenantSubscriptionService                                  │
└───────────────────┬─────────────────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────────────────┐
│                    Repository Layer                         │
│  LimitKeyRepository     SubscriptionPlanRepository          │
│  SubscriptionPlanLimitRepository                            │
│  TenantSubscriptionRepository                               │
└───────────────────┬─────────────────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────────────────┐
│                    PostgreSQL Database                      │
│  limit_keys   subscription_plans   subscription_plan_limits │
│  tenant_subscriptions   tenant_subscription_limits          │
└─────────────────────────────────────────────────────────────┘
```

---

## Data Model

### Entity Relationships

```
CurrencyEntity
    │ (1)
    │ used by
    ▼ (N)
SubscriptionPlanEntity ──────────────────────┐
    │ (1)                                     │
    │ has                                     │ referenced by
    ▼ (N)                                     │
SubscriptionPlanLimitEntity                  │
    │ (N)                                     │
    │ references                              │
    ▼ (1)                                     │
LimitKeyEntity                               │
                                             │
UserEntity (tenant)                          │
    │ (1)                                     │
    │ subscribes to                           │
    ▼ (N)                              (N)   ▼
TenantSubscriptionEntity ◄───────────────────┘
    │ (1)
    │ has
    ▼ (N)
TenantSubscriptionLimitEntity
    │ (N)
    │ references
    ▼ (1)
LimitKeyEntity
```

### LimitKeyEntity

| Field | Type | Description |
|---|---|---|
| `id` | `bigint` | Primary key |
| `limit_key` | `varchar(100)` | Unique identifier string, e.g. `max_api_keys` |
| `description` | `text` | Human-readable description |
| `unit` | `varchar(50)` | Unit of measurement, e.g. `requests`, `keys` |
| *(audit fields)* | — | `created_by`, `created_at`, `updated_by`, `updated_at`, `version`, `is_active`, `is_deleted`, `deleted_by`, `deleted_at` |

### SubscriptionPlanEntity

| Field | Type | Description |
|---|---|---|
| `id` | `bigint` | Primary key |
| `currency_id` | `bigint FK` | References `currencies.id` |
| `name` | `varchar(100)` | Plan name, e.g. `FREE`, `PRO`, `ENTERPRISE` |
| `price` | `numeric(10,2)` | Monthly/cycle price. Default `0` |
| `description` | `text[]` | Array of bullet-point descriptions |
| `billing_cycle` | `varchar(20)` | e.g. `MONTHLY`, `YEARLY`. Default `MONTHLY` |
| `duration_in_days` | `integer` | How many days the subscription lasts. Default `30` |
| *(audit fields)* | — | See above |

### SubscriptionPlanLimitEntity

| Field | Type | Description |
|---|---|---|
| `id` | `bigint` | Primary key |
| `subscription_plan_id` | `bigint FK` | References `subscription_plans.id` (CASCADE DELETE) |
| `limit_key_id` | `bigint FK` | References `limit_keys.id` |
| `limit_value` | `bigint` | The numeric value for this limit on this plan |
| *(audit fields)* | — | See above |

### TenantSubscriptionEntity

| Field | Type | Description |
|---|---|---|
| `id` | `bigint` | Primary key |
| `tenant_id` | `bigint FK` | References `users.id` — the subscribing tenant |
| `subscription_plan_id` | `bigint FK` | References `subscription_plans.id` |
| `status` | `varchar(30)` | Lifecycle status: `ACTIVE`, `CANCELLED`, `EXPIRED`, `SUSPENDED` |
| `start_at` | `timestamptz` | When the subscription started (set to `now()` at creation) |
| `end_at` | `timestamptz` | When the subscription expires (derived from `duration_in_days`) |
| `auto_renew` | `boolean` | Whether to auto-renew at expiry. Default `true` |
| *(audit fields)* | — | See above |

### TenantSubscriptionLimitEntity

| Field | Type | Description |
|---|---|---|
| `id` | `bigint` | Primary key |
| `tenant_subscription_id` | `bigint FK` | References `tenant_subscriptions.id` (CASCADE DELETE) |
| `limit_key_id` | `bigint FK` | References `limit_keys.id` |
| `limit_value` | `bigint` | Snapshotted limit value at subscription time |
| *(audit fields)* | — | See above |

---

## Database Schema

### Migration Files

| File | Creates |
|---|---|
| `V2__subscrition_model_create.sql` | `currencies`, `subscription_models` (legacy) |
| `V3__limit_key_create.sql` | `limit_keys` |
| `V4__subscription_plan_create.sql` | `subscription_plans`, `subscription_plan_limits` |
| `V5__tenant_subscription_create.sql` | `tenant_subscriptions`, `tenant_subscription_limits` |

### Entity-Relationship Summary

```sql
currencies (1) ──────── (N) subscription_plans
limit_keys (1) ──────── (N) subscription_plan_limits (N) ──── (1) subscription_plans
users      (1) ──────── (N) tenant_subscriptions
subscription_plans (1) ─(N) tenant_subscriptions
tenant_subscriptions (1)(N) tenant_subscription_limits (N) ── (1) limit_keys
```

---

## API Reference

All requests must include a valid JWT token in the `Authorization: Bearer <token>` header.

### Limit Keys API

Base path: `/api/v1/admins/limit-keys`
Required role: `ADMIN`

---

#### Create a Limit Key

```
POST /api/v1/admins/limit-keys/
```

**Request Body**

```json
{
  "limit_key": "max_api_keys",
  "description": "Maximum number of API keys a tenant can create",
  "unit": "keys"
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `limit_key` | `string` | Yes | Unique identifier string (max 100 chars) |
| `description` | `string` | No | Human-readable description |
| `unit` | `string` | No | Unit of measurement (max 50 chars) |

**Response** `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

#### List All Limit Keys

```
GET /api/v1/admins/limit-keys/
```

**Response** `200 OK`

```json
{
  "limit_keys": [
    {
      "id": 1,
      "limit_key": "max_api_keys",
      "description": "Maximum number of API keys a tenant can create",
      "unit": "keys"
    },
    {
      "id": 2,
      "limit_key": "requests_per_minute",
      "description": "Maximum API requests per minute",
      "unit": "requests"
    }
  ]
}
```

---

#### Get Limit Key by ID

```
GET /api/v1/admins/limit-keys/{id}
```

**Response** `200 OK`

```json
{
  "data": {
    "id": 1,
    "limit_key": "max_api_keys",
    "description": "Maximum number of API keys a tenant can create",
    "unit": "keys"
  }
}
```

---

#### Update a Limit Key

```
PUT /api/v1/admins/limit-keys/{id}
```

**Request Body** — same fields as create. All fields are replaced.

**Response** `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

#### Delete a Limit Key

Soft delete — sets `is_active = false`, `is_deleted = true`.

```
DELETE /api/v1/admins/limit-keys/{id}
```

**Response** `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

### Subscription Plans API

Base path: `/api/v1/admins/subscription-plans`
Required role: `ADMIN`

---

#### Create a Subscription Plan

```
POST /api/v1/admins/subscription-plans/
```

**Request Body**

```json
{
  "currency_id": 1,
  "name": "PRO",
  "price": 29.99,
  "billing_cycle": "MONTHLY",
  "duration_in_days": 30,
  "description": [
    "Up to 10 API keys",
    "1000 requests per minute",
    "Priority support"
  ],
  "limits": [
    { "limit_key_id": 1, "limit_value": 10 },
    { "limit_key_id": 2, "limit_value": 1000 }
  ]
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `currency_id` | `long` | Yes | ID of the currency for pricing |
| `name` | `string` | Yes | Plan name, e.g. `FREE`, `PRO`, `ENTERPRISE` |
| `price` | `decimal` | No | Price in the given currency. Default `0` |
| `billing_cycle` | `string` | No | `MONTHLY` or `YEARLY`. Default `MONTHLY` |
| `duration_in_days` | `integer` | No | Subscription duration in days. Default `30` |
| `description` | `string[]` | Yes | Array of feature bullet points |
| `limits` | `array` | No | List of limits to attach to this plan |
| `limits[].limit_key_id` | `long` | Yes | ID of the limit key |
| `limits[].limit_value` | `long` | Yes | Numeric value for this limit |

**Response** `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

---

#### List All Subscription Plans

```
GET /api/v1/admins/subscription-plans/
```

**Response** `200 OK`

```json
{
  "subscription_plans": [
    {
      "id": 1,
      "name": "FREE",
      "price": 0.00,
      "billing_cycle": "MONTHLY",
      "duration_in_days": 30,
      "description": ["1 API key", "100 requests per minute"],
      "currency": {
        "id": 1,
        "code": "USD",
        "symbol": "$"
      },
      "limits": [
        {
          "id": 1,
          "limit_key": { "id": 1, "limit_key": "max_api_keys", "unit": "keys" },
          "limit_value": 1
        }
      ]
    }
  ]
}
```

---

#### Get Subscription Plan by ID

```
GET /api/v1/admins/subscription-plans/{id}
```

**Response** `200 OK` — same shape as a single item from the list above, wrapped in `data`.

---

#### Update a Subscription Plan

```
PUT /api/v1/admins/subscription-plans/{id}
```

**Request Body** — same as create. Limits are **fully replaced** (delete all, then re-insert).

**Response** `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

#### Delete a Subscription Plan

Soft delete. CASCADE deletes `subscription_plan_limits` rows at the database level.

```
DELETE /api/v1/admins/subscription-plans/{id}
```

**Response** `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

---

### Tenant Subscriptions API

Base path: `/api/v1/subscriptions/tenant-subscriptions`
Required role: `USER` (authenticated tenant)

The tenant is resolved from the JWT token — no `tenant_id` field is needed in the request body. The currently authenticated user is the subscriber.

---

#### Subscribe to a Plan

The authenticated user subscribes themselves to a plan. `start_at` is set to the current timestamp. `end_at` is calculated automatically from the plan's `duration_in_days`.

```
POST /api/v1/subscriptions/tenant-subscriptions
```

**Request Body**

```json
{
  "subscription_plan_id": 2,
  "auto_renew": true
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `subscription_plan_id` | `long` | Yes | ID of the plan to subscribe to |
| `auto_renew` | `boolean` | No | Whether to auto-renew at expiry. Default `true` |

**Response** `201 Created`

```json
{
  "success": true,
  "id": 7
}
```

**What happens internally:**

1. The tenant is resolved from the JWT token via `UserService.getAuthenticatedUserEntity()`.
2. The plan is validated and fetched via `SubscriptionPlanService.getSubscriptionPlanEntityById()`.
3. `TenantSubscriptionMapper.fromRequest()` builds the entity:
   - `status` → `ACTIVE`
   - `start_at` → `OffsetDateTime.now()`
   - `end_at` → `now() + plan.durationInDays` (falls back to `+30 days` if not set)
   - `auto_renew` → from request, defaults to `true`
4. Entity is persisted to `tenant_subscriptions`.

---

## Business Logic

### Limit Key Design

Limit keys are shared definitions reused across many plans. They are created once by admins and referenced by plans. Examples of common limit keys:

| `limit_key` | `unit` | Description |
|---|---|---|
| `max_api_keys` | `keys` | How many app keys a tenant can generate |
| `requests_per_minute` | `requests` | Rate limit per minute |
| `requests_per_month` | `requests` | Monthly quota |
| `max_crm_connections` | `connections` | CRM integrations allowed |

### Plan Limits as Snapshots

When a plan's limits are updated, existing tenant subscriptions are **not affected** — they hold their own snapshot in `tenant_subscription_limits`. This ensures:

- Billing records stay accurate.
- Plan changes only apply to **new** subscribers.
- Downgrade/upgrade paths can be managed explicitly.

### Subscription End Date Calculation

`end_at` is not provided by the client. It is derived server-side:

```
end_at = OffsetDateTime.now() + subscriptionPlan.durationInDays
```

If `durationInDays` is not set on the plan, a default of **30 days** is used as a fallback.

### Soft Deletes

All entities use soft deletes. Rows are never physically removed. Instead:

- `is_active = false`
- `is_deleted = true`

All repository queries filter with `isActive = true AND isDeleted = false`.

### Who Can Do What

| Action | Role |
|---|---|
| Create / update / delete limit keys | `ADMIN` |
| Create / update / delete subscription plans | `ADMIN` |
| Subscribe a tenant to a plan | `USER` (self-service) |

---

## Request / Response Examples

### Full Flow: Set Up a PRO Plan and Assign It

**Step 1 — Create a currency (if not exists)**

```
POST /api/v1/admins/currency/
{ "code": "USD", "symbol": "$" }
→ { "success": true, "id": 1 }
```

**Step 2 — Create limit keys**

```
POST /api/v1/admins/limit-keys/
{ "limit_key": "max_api_keys", "description": "Max API keys", "unit": "keys" }
→ { "success": true, "id": 1 }

POST /api/v1/admins/limit-keys/
{ "limit_key": "requests_per_minute", "description": "RPM limit", "unit": "requests" }
→ { "success": true, "id": 2 }
```

**Step 3 — Create the PRO plan**

```
POST /api/v1/admins/subscription-plans/
{
  "currency_id": 1,
  "name": "PRO",
  "price": 29.99,
  "billing_cycle": "MONTHLY",
  "duration_in_days": 30,
  "description": ["10 API keys", "1000 requests per minute", "Priority support"],
  "limits": [
    { "limit_key_id": 1, "limit_value": 10 },
    { "limit_key_id": 2, "limit_value": 1000 }
  ]
}
→ { "success": true, "id": 1 }
```

**Step 4 — Tenant subscribes (authenticated as a USER)**

```
POST /api/v1/subscriptions/tenant-subscriptions
{ "subscription_plan_id": 1, "auto_renew": true }
→ { "success": true, "id": 1 }
```

Result in `tenant_subscriptions`:

| field | value |
|---|---|
| `tenant_id` | *(from JWT)* |
| `subscription_plan_id` | `1` |
| `status` | `ACTIVE` |
| `start_at` | `2026-04-01T10:00:00+00:00` |
| `end_at` | `2026-05-01T10:00:00+00:00` |
| `auto_renew` | `true` |

---

## Status Lifecycle

```
                    ┌───────────┐
                    │  ACTIVE   │◄──── created here
                    └─────┬─────┘
                          │
            ┌─────────────┼─────────────┐
            ▼             ▼             ▼
      ┌──────────┐  ┌──────────┐  ┌───────────┐
      │ EXPIRED  │  │CANCELLED │  │ SUSPENDED │
      └──────────┘  └──────────┘  └───────────┘
```

| Status | Meaning |
|---|---|
| `ACTIVE` | Subscription is live, limits are enforced |
| `EXPIRED` | Billing cycle ended, `auto_renew = false` |
| `CANCELLED` | Manually cancelled by tenant or admin |
| `SUSPENDED` | Temporarily disabled (e.g. payment failure) |

Status is stored as a plain `varchar(30)` string — no enum constraint in the DB — allowing future statuses without a migration.

---

## Migration Files

### V3 — Limit Keys

```
src/main/resources/db/migration/V3__limit_key_create.sql
```

Creates: `limit_keys`

### V4 — Subscription Plans

```
src/main/resources/db/migration/V4__subscription_plan_create.sql
```

Creates: `subscription_plans`, `subscription_plan_limits`

> **Note:** The `SubscriptionPlanEntity` contains `billing_cycle` and `duration_in_days` fields.
> If these columns are not yet in the database, a Flyway incremental migration (e.g. `V4_1`) is required to add them before the app will start (Hibernate validates schema on startup).

### V5 — Tenant Subscriptions

```
src/main/resources/db/migration/V5__tenant_subscription_create.sql
```

Creates: `tenant_subscriptions`, `tenant_subscription_limits`

Key constraints:
- `tenant_subscriptions.tenant_id` → FK to `users.id`
- `tenant_subscriptions.subscription_plan_id` → FK to `subscription_plans.id`
- `tenant_subscription_limits.tenant_subscription_id` → FK with `ON DELETE CASCADE`
- `tenant_subscription_limits.limit_key_id` → FK to `limit_keys.id`
