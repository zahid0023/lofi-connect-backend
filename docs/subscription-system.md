# Subscription System Documentation

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Data Model](#data-model)
4. [Database Schema](#database-schema)
5. [API Reference](#api-reference)
   - [Limit Keys API](#limit-keys-api)
   - [Subscription Plans API](#subscription-plans-api)
   - [Tenant Subscriptions API](#tenant-subscriptions-api)
   - [App Key API](#app-key-api)
6. [Business Logic](#business-logic)
   - [App Key–Subscription Link](#app-keysubscription-link)
   - [One Active Subscription Per User](#one-active-subscription-per-user)
   - [Limit Key Design](#limit-key-design)
   - [Plan Limits as Snapshots](#plan-limits-as-snapshots)
   - [Subscription End Date Calculation](#subscription-end-date-calculation)
   - [Soft Deletes](#soft-deletes)
   - [Who Can Do What](#who-can-do-what)
7. [Request / Response Examples](#request--response-examples)
8. [Status Lifecycle](#status-lifecycle)
9. [Error Reference](#error-reference)
10. [Migration Files](#migration-files)
11. [File Reference](#file-reference)

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

**Core rules:**
- Limits are plan-driven. Usage is app-key-driven. Limits apply at the **tenant** level, not the individual user level.
- **Each user may have at most one active subscription at any point in time.** Attempting to subscribe while an active subscription exists returns `409 Conflict`.

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
│    └── enforces one-active-subscription rule                │
└───────────────────┬─────────────────────────────────────────┘
                    │
┌───────────────────▼─────────────────────────────────────────┐
│                    Repository Layer                         │
│  LimitKeyRepository     SubscriptionPlanRepository          │
│  SubscriptionPlanLimitRepository                            │
│  TenantSubscriptionRepository                               │
│    └── existsByTenantEntityAndStatusAndEndAtAfter...()      │
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
    │ subscribes to (max 1 active at a time)  │
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
| `limits[].limit_key_id` | `long` | Yes (in limits) | ID of the limit key |
| `limits[].limit_value` | `long` | Yes (in limits) | Numeric value for this limit |

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

| Endpoint | Purpose |
|---|---|
| `POST /` | Subscribe to a plan (must have no active subscription) |
| `POST /upgrade` | Switch to a different plan (must have an active subscription) |

---

#### Subscribe to a Plan

The authenticated user subscribes themselves to a plan. `start_at` is set to the current timestamp. `end_at` is calculated automatically from the plan's `duration_in_days`.

> **Constraint:** A user may only have **one active subscription** at a time. If the user already holds a subscription with `status = ACTIVE`, `end_at` in the future, `is_active = true`, and `is_deleted = false`, the request is rejected with `409 Conflict`. The existing subscription must be cancelled or allowed to expire before a new one can be created.

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

**Error — Active Subscription Already Exists** `409 Conflict`

Returned when the authenticated user already has a non-expired, active, non-deleted subscription.

```json
{
  "request_id": null,
  "status": 409,
  "error": "ACTIVE_SUBSCRIPTION_EXISTS",
  "message": "User already has an active subscription. Please cancel the existing subscription before subscribing to a new plan."
}
```

**Internal execution flow:**

```
POST /api/v1/subscriptions/tenant-subscriptions
        │
        ▼
TenantSubscriptionController.subscribePlan()
        │
        ├─ 1. UserService.getAuthenticatedUserEntity()
        │       Resolves the caller from the JWT token.
        │
        ├─ 2. SubscriptionPlanService.getSubscriptionPlanEntityById(request.subscriptionPlanId)
        │       Validates the plan exists; throws EntityNotFoundException if not.
        │
        └─ 3. TenantSubscriptionService.subscribePlan(request, user, plan)
                │
                ├─ 3a. CHECK — existsByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
                │              user, "ACTIVE", OffsetDateTime.now(), true, false)
                │       true  → throw ActiveSubscriptionExistsException  ──► 409 Conflict
                │       false → continue
                │
                ├─ 3b. TenantSubscriptionMapper.fromRequest()
                │       status    = "ACTIVE"
                │       start_at  = now()
                │       end_at    = now() + plan.durationInDays  (fallback: +30 days)
                │       auto_renew = request.autoRenew ?? true
                │
                └─ 3c. tenantSubscriptionRepository.save(entity)
                        → returns SuccessResponse(true, newId)
```

---

### App Key API

Base path: `/api/v1/app-keys`
Required role: `USER` (authenticated tenant)

App keys are always tied to the subscription that was active at the time they were generated. Generating an app key requires an active subscription.

---

#### Generate an App Key

```
POST /api/v1/app-keys/generate
```

**Request Body**

```json
{
  "name": "My Production Key"
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `name` | `string` | Yes | A human-readable label for the key |

**Response** `201 Created`

```json
{
  "app_key": {
    "id": 5,
    "app_key": "lc_abc123...xyz",
    "name": "My Production Key",
    "masked_key": "lc_a****xyz",
    "status": "active",
    "created_at": "2026-04-01T10:00:00Z",
    "updated_at": "2026-04-01T10:00:00Z",
    "subscription_id": 7,
    "subscription_plan_id": 2,
    "subscription_plan_name": "PRO",
    "ghl_connection": null
  }
}
```

| Response Field | Description |
|---|---|
| `app_key` | Full key value — only returned once at creation time |
| `masked_key` | Safe display form: first 4 + `****` + last 4 chars |
| `subscription_id` | ID of the subscription this key was generated under |
| `subscription_plan_id` | ID of the plan active at generation time |
| `subscription_plan_name` | Name of the plan (e.g. `FREE`, `PRO`, `ENTERPRISE`) |

**Error — No Active Subscription** `403 Forbidden`

```json
{
  "status": 403,
  "error": "SUBSCRIPTION_REQUIRED",
  "message": "No active subscription found. Please subscribe to a plan to generate app keys."
}
```

**Error — Plan App Key Limit Reached** `402 Payment Required`

```json
{
  "status": 402,
  "error": "PLAN_LIMIT_EXCEEDED",
  "message": "App key limit reached. Your current plan allows 3 app key(s). Please upgrade your plan to generate more."
}
```

**Internal execution flow:**

```
POST /api/v1/app-keys/generate
        │
        ▼
AppKeyController.generateAppKey()
        │
        ├─ 1. UserService.getAuthenticatedUserEntity()
        │
        └─ 2. AppKeyService.generateAppKey(user, request)
                │
                ├─ 2a. SubscriptionValidationService.validateAppKeyCreation(user)
                │       ├─ find active subscription
                │       │    empty → SubscriptionInvalidException  ──► 403
                │       ├─ check MAX_APP_KEYS limit on the plan
                │       │    exceeded → PlanLimitExceededException ──► 402
                │       └─ return TenantSubscriptionEntity
                │
                ├─ 2b. LofiConnectAppKeyMapper.toEntity(request, subscription)
                │       appKey            = AppKeyGenerator.generateAppKey()
                │       name              = request.name
                │       tenantSubscription = subscription   ← FK stored here
                │
                └─ 2c. lofiConnectAppKeyRepository.save(entity)
                        → return GenerateAppKeyResponse(dto)
```

---

#### List App Keys

```
GET /api/v1/app-keys
```

Returns all active, non-deleted app keys for the authenticated user. Each entry includes the `subscription_id`, `subscription_plan_id`, and `subscription_plan_name` fields showing which plan each key was generated under.

**Response** `200 OK`

```json
{
  "app_keys": [
    {
      "id": 3,
      "name": "Staging Key",
      "masked_key": "lc_a****abc",
      "status": "active",
      "subscription_id": 4,
      "subscription_plan_id": 1,
      "subscription_plan_name": "FREE",
      "ghl_connection": null
    },
    {
      "id": 5,
      "name": "My Production Key",
      "masked_key": "lc_a****xyz",
      "status": "active",
      "subscription_id": 7,
      "subscription_plan_id": 2,
      "subscription_plan_name": "PRO",
      "ghl_connection": { ... }
    }
  ]
}
```

> **Note:** Keys generated before the subscription link was introduced will have `subscription_id`, `subscription_plan_id`, and `subscription_plan_name` as `null`.

---

#### Upgrade (Change) a Plan

Cancels the user's current active subscription immediately and creates a new one for the requested plan — all within a single database transaction. There is no gap in coverage and no orphaned subscription.

> **Constraint:** The user must have an existing active subscription to upgrade. Calling this endpoint without one returns `400 Bad Request`.

```
POST /api/v1/subscriptions/tenant-subscriptions/upgrade
```

**Request Body** — identical shape to subscribe:

```json
{
  "subscription_plan_id": 3,
  "auto_renew": true
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| `subscription_plan_id` | `long` | Yes | ID of the new plan to switch to |
| `auto_renew` | `boolean` | No | Auto-renew setting for the new subscription. Default `true` |

**Response** `200 OK`

```json
{
  "success": true,
  "id": 12
}
```

The `id` in the response is the **new** subscription's ID. The old subscription is now `CANCELLED` with `end_at = now()`.

**Error — No Active Subscription** `400 Bad Request`

```json
{
  "request_id": null,
  "status": 400,
  "error": "NO_ACTIVE_SUBSCRIPTION",
  "message": "No active subscription found. Please subscribe to a plan before attempting an upgrade."
}
```

**Internal execution flow:**

```
POST /api/v1/subscriptions/tenant-subscriptions/upgrade
        │
        ▼
TenantSubscriptionController.upgradePlan()
        │
        ├─ 1. UserService.getAuthenticatedUserEntity()
        │
        ├─ 2. SubscriptionPlanService.getSubscriptionPlanEntityById(request.subscriptionPlanId)
        │       Validates the new plan exists; throws EntityNotFoundException if not.
        │
        └─ 3. TenantSubscriptionService.upgradePlan(request, user, newPlan)   [@Transactional]
                │
                ├─ 3a. findFirstByTenantEntityAndStatus...OrderByStartAtDesc(
                │              user, "ACTIVE", now(), true, false)
                │       empty → throw NoActiveSubscriptionException  ──► 400 Bad Request
                │
                ├─ 3b. Cancel current subscription
                │       current.status  = "CANCELLED"
                │       current.end_at  = now()
                │       save(current)
                │
                ├─ 3c. TenantSubscriptionMapper.fromRequest(request, user, newPlan)
                │       status    = "ACTIVE"
                │       start_at  = now()
                │       end_at    = now() + newPlan.durationInDays
                │       auto_renew = request.autoRenew ?? true
                │
                └─ 3d. save(newSubscription)
                        → return SuccessResponse(true, newSubscription.id)
```

**Atomicity:** Steps 3b–3d run inside `@Transactional`. If the new subscription cannot be saved for any reason, the cancellation of the old subscription is also rolled back — the user's original plan remains intact.

---

## Business Logic

### App Key–Subscription Link

Every app key is permanently linked to the `TenantSubscriptionEntity` that was active at the moment the key was generated. This relationship is stored in the `tenant_subscription_id` FK column on `lofi_connect_app_key`.

#### Why this matters

| Scenario | Benefit |
|---|---|
| User upgrades from FREE → PRO | Old keys retain their FREE subscription link; new keys created after the upgrade are linked to the PRO subscription. Historical records remain accurate. |
| Subscription expires or is cancelled | The app key still knows which plan it was generated under, even after that subscription is no longer active. |
| Usage enforcement | `AppKeyUsageFilter` resolves the tenant's **current** active subscription at request time (not the one stored on the key) to enforce live quotas. The stored link is for audit and attribution only. |
| Auditing / analytics | Admins can query which plan a key was created under, or how many keys were created per plan. |

#### Enforcement flow at generation time

```
generateAppKey() is called
        │
        ▼
SubscriptionValidationService.validateAppKeyCreation(user)
        │
        ├─ No active subscription → 403 SUBSCRIPTION_REQUIRED
        │   (user must subscribe before generating any key)
        │
        ├─ MAX_APP_KEYS limit exceeded → 402 PLAN_LIMIT_EXCEEDED
        │   (user must upgrade to a plan with a higher key allowance)
        │
        └─ Returns TenantSubscriptionEntity
                │
                ▼
        LofiConnectAppKeyMapper.toEntity(request, subscription)
                sets entity.tenantSubscription = subscription
                │
                ▼
        Saved to DB with tenant_subscription_id = subscription.id
```

#### What happens to app keys on upgrade

When a user upgrades their plan via `POST /upgrade`, the old subscription is cancelled and a new one is created. App keys generated before the upgrade remain linked to the old (now cancelled) subscription. App keys generated after the upgrade are linked to the new subscription. **App keys are not re-linked automatically** — this is intentional for audit accuracy.

If the user's plan limit decreases after an upgrade (e.g. downgrade), existing keys are not deleted. The `MAX_APP_KEYS` limit is only enforced at **creation time**.

---

### One Active Subscription Per User

**Rule:** At any moment, a user may hold at most one subscription that satisfies all four conditions simultaneously:

| Condition | Value |
|---|---|
| `status` | `ACTIVE` |
| `end_at` | after `now()` (not yet expired) |
| `is_active` | `true` |
| `is_deleted` | `false` |

If all four conditions are true for an existing record, any attempt to subscribe again is blocked.

**Why these four conditions together?**

- `status = ACTIVE` excludes rows that have been logically cancelled (`CANCELLED`), suspended (`SUSPENDED`), or expired (`EXPIRED`) but not yet soft-deleted.
- `end_at > now()` excludes subscriptions that have passed their billing period end date, even if their `status` column has not been updated by a background job yet.
- `is_active = true AND is_deleted = false` exclude soft-deleted rows that are retained for audit/history purposes.

This means a user whose only subscription is `EXPIRED`, `CANCELLED`, `SUSPENDED`, or past its `end_at` date is free to subscribe again without any manual cleanup.

**Implementation:**

```
TenantSubscriptionRepository
  .existsByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
      userEntity,         // the tenant
      "ACTIVE",           // status
      OffsetDateTime.now(), // end_at must be after this
      true,               // is_active
      false               // is_deleted
  )
```

This generates a single SQL `EXISTS` query — it does not fetch the full entity — making the check as lightweight as possible.

**Exception thrown:** `ActiveSubscriptionExistsException` (extends `RuntimeException`)

**HTTP response:** `409 Conflict` with error code `ACTIVE_SUBSCRIPTION_EXISTS`

**To subscribe to a new plan, the user must first:**
1. Cancel the current subscription (set `status = CANCELLED`), or
2. Wait for it to naturally expire (`end_at` passes), or
3. Have an admin suspend and deactivate the existing record.

---

### Limit Key Design

Limit keys are shared definitions reused across many plans. They are created once by admins and referenced by plans. Examples of common limit keys:

| `limit_key` | `unit` | Description |
|---|---|---|
| `max_api_keys` | `keys` | How many app keys a tenant can generate |
| `requests_per_minute` | `RPM` | Rate limit per minute |
| `requests_per_month` | `MONTHLY` | Monthly quota |
| `max_crm_connections` | `connections` | CRM integrations allowed |

The `unit` field drives enforcement behaviour in the usage pipeline (see `docs/usage-enforcement.md`).

---

### Plan Limits as Snapshots

When a plan's limits are updated, existing tenant subscriptions are **not affected** — they hold their own snapshot in `tenant_subscription_limits`. This ensures:

- Billing records stay accurate.
- Plan changes only apply to **new** subscribers.
- Downgrade/upgrade paths can be managed explicitly.

---

### Subscription End Date Calculation

`end_at` is not provided by the client. It is derived server-side at subscription time:

```
end_at = OffsetDateTime.now() + subscriptionPlan.durationInDays
```

If `durationInDays` is not set on the plan, a default of **30 days** is used as a fallback.

---

### Soft Deletes

All entities use soft deletes. Rows are never physically removed. Instead:

- `is_active = false`
- `is_deleted = true`

All repository queries filter with `isActive = true AND isDeleted = false`. Soft-deleted subscriptions do **not** count against the one-active-subscription limit.

---

### Who Can Do What

| Action | Role |
|---|---|
| Create / update / delete limit keys | `ADMIN` |
| Create / update / delete subscription plans | `ADMIN` |
| Subscribe a tenant to a plan | `USER` (self-service) |
| Subscribe while already having an active plan | **Blocked — `409 Conflict`** |

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
{ "limit_key": "requests_per_minute", "description": "RPM limit", "unit": "RPM" }
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
Authorization: Bearer <jwt>
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

### Upgrading a Plan

**User is on the FREE plan (subscription id = 1) and upgrades to PRO (plan id = 2):**

```
POST /api/v1/subscriptions/tenant-subscriptions/upgrade
Authorization: Bearer <jwt>
{ "subscription_plan_id": 2, "auto_renew": true }

← 200 OK
{ "success": true, "id": 8 }
```

State after the call:

| id | plan | status | end_at |
|---|---|---|---|
| 1 | FREE | `CANCELLED` | `2026-04-01T10:05:00+00:00` ← set to now() |
| 8 | PRO | `ACTIVE` | `2026-05-01T10:05:00+00:00` ← now() + 30 days |

The old subscription (id = 1) is immediately cancelled. The new subscription (id = 8) starts from the moment of the upgrade request.

---

### Attempting to Upgrade Without a Subscription (Blocked)

```
POST /api/v1/subscriptions/tenant-subscriptions/upgrade
Authorization: Bearer <jwt>
{ "subscription_plan_id": 2, "auto_renew": true }

← 400 Bad Request
{
  "request_id": null,
  "status": 400,
  "error": "NO_ACTIVE_SUBSCRIPTION",
  "message": "No active subscription found. Please subscribe to a plan before attempting an upgrade."
}
```

---

### Attempting to Subscribe Twice (Blocked)

**User already has an active subscription (id = 1) and tries to subscribe again:**

```
POST /api/v1/subscriptions/tenant-subscriptions
Authorization: Bearer <jwt>
{ "subscription_plan_id": 2, "auto_renew": false }

← 409 Conflict
{
  "request_id": null,
  "status": 409,
  "error": "ACTIVE_SUBSCRIPTION_EXISTS",
  "message": "User already has an active subscription. Please cancel the existing subscription before subscribing to a new plan."
}
```

**What the service does internally:**

```
existsByTenantEntityAndStatusAndEndAtAfterAndIsActiveAndIsDeleted(
    user=42, status="ACTIVE", now=2026-04-01T10:05:00Z, isActive=true, isDeleted=false
)
→ true  (subscription id=1 matches all conditions)
→ throw ActiveSubscriptionExistsException
→ GlobalExceptionHandler maps to 409 ACTIVE_SUBSCRIPTION_EXISTS
```

**The request is rejected before any new record is created.** No `tenant_subscriptions` row is inserted.

---

### Subscribing After Expiry (Allowed)

**User's subscription (id = 1) has passed its `end_at` date:**

```
Existing record:
  status   = ACTIVE
  end_at   = 2026-03-31T23:59:59+00:00   ← in the past
  is_active = true
  is_deleted = false

POST /api/v1/subscriptions/tenant-subscriptions
Authorization: Bearer <jwt>
{ "subscription_plan_id": 2, "auto_renew": true }

← 201 Created
{ "success": true, "id": 8 }
```

**Why this is allowed:** The `existsBy...` query checks `end_at > now()`. The old record's `end_at` is in the past, so it does not match — the check returns `false` and the new subscription is created.

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

| Status | Meaning | Blocks new subscription? |
|---|---|---|
| `ACTIVE` | Subscription is live, limits are enforced | **Yes** — if `end_at` is still in the future |
| `EXPIRED` | Billing cycle ended, `auto_renew = false` | No |
| `CANCELLED` | Manually cancelled by tenant or admin | No |
| `SUSPENDED` | Temporarily disabled (e.g. payment failure) | No |

> The "blocks new subscription" column reflects the one-active-subscription enforcement rule.
> An `ACTIVE` record whose `end_at` has already passed does **not** block a new subscription,
> even if its `status` column was never updated by a background job.

Status is stored as a plain `varchar(30)` string — no enum constraint in the DB — allowing future statuses without a migration.

---

## Error Reference

All error responses use the shared `ApiErrorResponse` shape:

```json
{
  "request_id": "<X-Request-Id header or null>",
  "status": <http status code>,
  "error": "<error code>",
  "message": "<human-readable message>"
}
```

### Subscription-Related Errors

| HTTP Status | Error Code | Trigger |
|---|---|---|
| `409 Conflict` | `ACTIVE_SUBSCRIPTION_EXISTS` | `POST /tenant-subscriptions` while user already has an active, non-expired subscription |
| `400 Bad Request` | `NO_ACTIVE_SUBSCRIPTION` | `POST /tenant-subscriptions/upgrade` when user has no active subscription to upgrade from |
| `403 Forbidden` | `SUBSCRIPTION_REQUIRED` | Accessing a protected feature without any valid subscription |
| `429 Too Many Requests` | `QUOTA_EXCEEDED` | API call exceeds RPM or monthly request limit |
| `402 Payment Required` | `PLAN_LIMIT_EXCEEDED` | Creating a resource (e.g. app key) that exceeds the plan's static limit |
| `404 Not Found` | `ENTITY_NOT_FOUND` | Requested plan or subscription does not exist |
| `400 Bad Request` | `INVALID_ARGUMENT` | Malformed request field |

### 409 — Active Subscription Already Exists

```
Trigger: POST /api/v1/subscriptions/tenant-subscriptions
         when user already has status=ACTIVE, end_at > now(), is_active=true, is_deleted=false
```

```json
{
  "request_id": null,
  "status": 409,
  "error": "ACTIVE_SUBSCRIPTION_EXISTS",
  "message": "User already has an active subscription. Please cancel the existing subscription before subscribing to a new plan."
}
```

**Handler:** `GlobalExceptionHandler.handleActiveSubscriptionExists()`

**Resolution for the user:** Cancel or wait for the existing subscription to expire, then retry.

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

---

## File Reference

### New Files

| File | Package | Description |
|---|---|---|
| `ActiveSubscriptionExistsException.java` | `commons.exception` | Thrown when a user tries to subscribe while already having an active plan → `409 Conflict` |
| `NoActiveSubscriptionException.java` | `commons.exception` | Thrown when a user calls `/upgrade` but has no active subscription to upgrade from → `400 Bad Request` |
| `V7__app_key_subscription_link.sql` | `db/migration` | Adds `tenant_subscription_id` FK column to `lofi_connect_app_key` + index |

### Modified Files

| File | Change |
|---|---|
| `LofiConnectAppKeyEntity.java` | Added `@ManyToOne tenantSubscription` → `TenantSubscriptionEntity` |
| `LofiConnectAppKeyDTO.java` | Added `subscriptionId`, `subscriptionPlanId`, `subscriptionPlanName` fields |
| `LofiConnectAppKeyMapper.java` | `toEntity()` now accepts `TenantSubscriptionEntity` and sets the FK; `toDto()` maps the three new subscription fields |
| `SubscriptionValidationService.java` | Return type changed from `void` → `TenantSubscriptionEntity` so the caller can reuse the resolved subscription without a second DB lookup |
| `SubscriptionValidationServiceImpl.java` | Updated to return the resolved `TenantSubscriptionEntity` on success |
| `AppKeyServiceImpl.java` | Captures the subscription returned by `validateAppKeyCreation()` and passes it to the mapper |
| `TenantSubscriptionRepository.java` | Added `existsByTenantEntityAndStatus...()` (subscribe guard) and `findFirstByTenantEntityAndStatus...OrderByStartAtDesc()` (upgrade lookup) |
| `TenantSubscriptionService.java` | Added `upgradePlan()` method to the interface |
| `TenantSubscriptionServiceImpl.java` | Added `@Transactional` on `subscribePlan()`; implemented `upgradePlan()` — cancels current, creates new in one transaction |
| `TenantSubscriptionController.java` | Added `POST /upgrade` endpoint |
| `GlobalExceptionHandler.java` | Added handlers for `ActiveSubscriptionExistsException` (`409`) and `NoActiveSubscriptionException` (`400`) |

### Pre-existing Files (Subscription Core)

| File | Role |
|---|---|
| `TenantSubscriptionController.java` | `POST /api/v1/subscriptions/tenant-subscriptions` — resolves user from JWT, delegates to service |
| `TenantSubscriptionServiceImpl.java` | Orchestrates subscription creation including the one-active guard |
| `TenantSubscriptionMapper.java` | Builds `TenantSubscriptionEntity` from request + user + plan |
| `TenantSubscriptionEntity.java` | JPA entity for `tenant_subscriptions` table |
| `TenantSubscriptionRepository.java` | JPA repository with existence and lookup queries |
| `SubscriptionPlanEntity.java` | Plan definition including `durationInDays` used for `end_at` calculation |
| `SubscriptionPlanService.java` | `getSubscriptionPlanEntityById()` — validates plan exists before subscribe |
| `AuditableEntity.java` | Base class supplying `is_active`, `is_deleted`, and other audit columns |
