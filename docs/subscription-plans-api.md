# Subscription Plans API

Base URL: `/api/v1/subscriptions/plans`

A **Subscription Plan** is a named pricing tier that bundles a price, currency, and a set of enforced limits. Admins
design plans; tenants subscribe to them. Plans reference Limit Keys (defined separately) and assign concrete values to
each key — for example, "Professional plan allows 20 API keys and 10,000 requests per billing period."

---

## Table of Contents

1. [Concept](#concept)
2. [Flow](#flow)
3. [Endpoints](#endpoints)
4. [Data Model](#data-model)
5. [Enumerations](#enumerations)
6. [Create Subscription Plan](#create-subscription-plan)
7. [Get Subscription Plan](#get-subscription-plan)
8. [List All Subscription Plans (Admin)](#list-all-subscription-plans-admin)
9. [List Public Subscription Plans](#list-public-subscription-plans)
10. [Update Subscription Plan](#update-subscription-plan)
11. [Delete Subscription Plan](#delete-subscription-plan)
12. [Error Responses](#error-responses)
13. [Design Rules](#design-rules)
14. [Recommended Plan Structure](#recommended-plan-structure)

---

## Concept

| Layer                   | Responsibility                                              | Example                                    |
|-------------------------|-------------------------------------------------------------|--------------------------------------------|
| **Limit Key**           | Defines *what* can be limited (global, reusable)            | `API_KEYS` — max API keys allowed per user |
| **Subscription Plan**   | Assigns *how much* is allowed per tier, sets a price        | Professional: `API_KEYS = 20`, $29/month   |
| **Tenant Subscription** | Assigns a plan to a tenant, snapshots limits at signup time | User X → Professional, `API_KEYS = 20`     |

A plan without limits is valid — it means the plan enforces no caps (effectively unlimited on all dimensions). A plan
with limits must reference existing, active Limit Keys.

### `is_public` Flag

Plans have an `is_public` flag (default `true`). When `true`, the plan appears on the public listing endpoint that
tenants use to browse and choose a plan. When `false`, the plan is hidden from tenants and is only visible to admins.

Use `is_public = false` for:

- Plans under construction
- Internal/enterprise plans not yet ready for self-service
- Legacy plans that are no longer sold but still active for existing subscribers

---

## Flow

```
[Admin creates Limit Keys]          ← one-time setup
        ↓
[Admin creates Subscription Plans]  ← assigns prices + limit values per key
        ↓
[Tenant browses GET /plans/public]  ← sees available plans
        ↓
[Tenant subscribes to a Plan]       ← plan limits snapshotted onto subscription
        ↓
[API Gateway enforces limits]       ← per request, checks tenant usage vs. limits
```

### End-to-End Example

**Step 1 — Limit Keys already exist:**

| Code           | Category | Unit    |
|----------------|----------|---------|
| `API_KEYS`     | RESOURCE | `COUNT` |
| `API_REQUESTS` | USAGE    | `COUNT` |
| `CRM_ACCOUNTS` | RESOURCE | `COUNT` |

**Step 2 — Admin creates plans and assigns limit values:**

| Plan         | Price  | `API_KEYS` | `API_REQUESTS` | `CRM_ACCOUNTS` | `is_public` |
|--------------|--------|------------|----------------|----------------|-------------|
| Free         | $0     | 2          | 500            | 1              | true        |
| Professional | $29/mo | 20         | 10,000         | 5              | true        |
| Enterprise   | $99/mo | 100        | 100,000        | Unlimited (-1) | true        |
| Beta Tester  | $0     | 50         | 50,000         | 10             | **false**   |

**Step 3 — Tenant hits the public plans endpoint:**

Only Free, Professional, and Enterprise are returned. Beta Tester is hidden.

**Step 4 — Tenant subscribes to Professional:**

Their subscription records `API_KEYS = 20`, `API_REQUESTS = 10,000`, `CRM_ACCOUNTS = 5`.

Even if the plan is later updated, the tenant's subscription retains the values from the time they subscribed.

---

## Endpoints

| Method   | Path                                 | Auth  | Description                                  |
|----------|--------------------------------------|-------|----------------------------------------------|
| `POST`   | `/api/v1/subscriptions/plans`        | ADMIN | Create a subscription plan                   |
| `GET`    | `/api/v1/subscriptions/plans/{id}`   | ADMIN | Get full plan details by ID                  |
| `GET`    | `/api/v1/subscriptions/plans`        | ADMIN | List all plans (including hidden)            |
| `GET`    | `/api/v1/subscriptions/plans/public` | ANY   | List only public, active plans (for tenants) |
| `PUT`    | `/api/v1/subscriptions/plans/{id}`   | ADMIN | Replace all plan fields and limits           |
| `DELETE` | `/api/v1/subscriptions/plans/{id}`   | ADMIN | Soft-delete a plan                           |

> `GET /plans/public` is the only endpoint accessible without admin role. All other endpoints require `ADMIN`.

---

## Data Model

### Plan Fields

| Field               | Type           | Required | Constraints                  | Description                                                               |
|---------------------|----------------|----------|------------------------------|---------------------------------------------------------------------------|
| `id`                | Long           | —        | read-only                    | Auto-generated identifier                                                 |
| `code`              | String         | Yes      | unique, max 100 chars        | Machine-readable identifier (e.g. `PROFESSIONAL`). Immutable after create |
| `currency_id`       | Long           | Yes      | must reference active record | ID of the currency this plan is priced in                                 |
| `billing_cycle`     | `BillingCycle` | Yes      | enum                         | How often the plan renews: `MONTHLY`, `QUARTERLY`, `ANNUAL`, `LIFETIME`   |
| `trial_period_days` | Integer        | No       | >= 0, default 0              | Free trial days before first charge. `0` = no trial                       |
| `name`              | String         | Yes      | max 100 chars                | Human-readable plan name (e.g. `Professional`)                            |
| `price`             | Decimal        | No       | >= 0.00, default 0.00        | Plan price per billing cycle. Use `0.00` for free tiers                   |
| `description`       | String[]       | Yes      | min 1 entry                  | Array of bullet points displayed in plan UI (e.g. feature highlights)     |
| `sort_order`        | Integer        | No       | >= 0, default 0              | Controls display order. Lower = shown first                               |
| `is_public`         | Boolean        | No       | default `true`               | When `false`, hidden from tenant-facing listing                           |
| `limits`            | LimitEntry[]   | No       | no duplicates per key        | Array of limit assignments for this plan                                  |

---

## Enumerations

### `billing_cycle` — `BillingCycle`

Controls how frequently the subscription renews and how pricing is applied.

| Value       | Description                                                                                        |
|-------------|----------------------------------------------------------------------------------------------------|
| `MONTHLY`   | Subscription renews every calendar month. Standard recurring billing.                              |
| `QUARTERLY` | Subscription renews every 3 months. Often used for a discounted mid-tier option.                   |
| `ANNUAL`    | Subscription renews once per year. Typically offered at a discount vs. monthly.                    |
| `LIFETIME`  | One-time payment, never expires. Subscription remains active indefinitely without further billing. |

> **`LIFETIME` and `trial_period_days`:** A `LIFETIME` plan with a trial period means the tenant gets
`trial_period_days`
> free, then pays once. After that single payment the subscription never expires.

> **Free plans:** A plan priced at `0.00` can use any `billing_cycle`. Use `MONTHLY` for free plans that should appear
> alongside paid monthly tiers, or `LIFETIME` for a permanently-free tier that never renews.

### Limit Entry Fields (nested in `limits`)

| Field          | Type | Required | Constraints               | Description                                        |
|----------------|------|----------|---------------------------|----------------------------------------------------|
| `limit_key_id` | Long | Yes      | must reference active key | References an existing Limit Key                   |
| `limit_value`  | Long | Yes      | >= -1                     | The cap for this dimension. Use `-1` for unlimited |

> **Unlimited convention:** A `limit_value` of `-1` means the gateway skips enforcement for that key entirely. `0` means
> the feature is disabled (zero allowed). `1`+ means a real cap.

---

## Create Subscription Plan

`POST /api/v1/subscriptions/plans`

> Requires `ADMIN` role.

Creates a new subscription plan. The `code` is set once and cannot be changed later. Currency must exist and be active.
All `limit_key_id` values must reference active, non-deleted limit keys. Duplicate `limit_key_id` entries in the same
request are rejected.

### Request Body

```json
{
  "code": "PROFESSIONAL",
  "currency_id": 1,
  "billing_cycle": "MONTHLY",
  "trial_period_days": 14,
  "name": "Professional",
  "price": "29.00",
  "sort_order": 2,
  "is_public": true,
  "description": [
    "20 API keys",
    "10,000 API requests per month",
    "5 CRM accounts",
    "Priority support",
    "14-day free trial"
  ],
  "limits": [
    {
      "limit_key_id": 1,
      "limit_value": 20
    },
    {
      "limit_key_id": 2,
      "limit_value": 10000
    },
    {
      "limit_key_id": 3,
      "limit_value": 5
    }
  ]
}
```

### Request Fields

| Field                   | Type           | Required | Validation                                      |
|-------------------------|----------------|----------|-------------------------------------------------|
| `code`                  | String         | Yes      | Not blank, max 100 chars, globally unique       |
| `currency_id`           | Long           | Yes      | Must reference an active currency               |
| `billing_cycle`         | `BillingCycle` | Yes      | `MONTHLY`, `QUARTERLY`, `ANNUAL`, or `LIFETIME` |
| `trial_period_days`     | Integer        | No       | >= 0 (default `0`). `0` = no trial              |
| `name`                  | String         | Yes      | Not blank, max 100 chars                        |
| `price`                 | Decimal        | No       | >= 0.00 (default `0.00`)                        |
| `sort_order`            | Integer        | No       | >= 0 (default `0`)                              |
| `is_public`             | Boolean        | No       | Default `true`                                  |
| `description`           | String[]       | Yes      | Array, minimum 1 entry                          |
| `limits`                | Object[]       | No       | No duplicate `limit_key_id`                     |
| `limits[].limit_key_id` | Long           | Yes      | Must reference an active limit key              |
| `limits[].limit_value`  | Long           | Yes      | >= -1 (`-1` = unlimited)                        |

### Response `201 Created`

```json
{
  "success": true,
  "id": 3
}
```

### Pre-Create Validations

| Check                    | Error                  | Message Example                                             |
|--------------------------|------------------------|-------------------------------------------------------------|
| `code` already exists    | `400 INVALID_ARGUMENT` | `Subscription plan with code 'PROFESSIONAL' already exists` |
| `currency_id` not found  | `404 ENTITY_NOT_FOUND` | `Currency not found with id: 99`                            |
| `limit_key_id` not found | `404 ENTITY_NOT_FOUND` | `LimitKey not found with id: 5`                             |
| Duplicate `limit_key_id` | `400 INVALID_ARGUMENT` | `Duplicate limit key id in request: 2`                      |
| `billing_cycle` missing  | `400 VALIDATION_ERROR` | `billing_cycle: must not be null`                           |
| `trial_period_days` < 0  | `400 VALIDATION_ERROR` | `trial_period_days must be 0 or greater`                    |
| `limit_value` < -1       | `400 VALIDATION_ERROR` | `limitValue must be -1 (unlimited) or a positive number`    |
| `price` < 0              | `400 VALIDATION_ERROR` | `price must be 0.00 or greater`                             |
| `sort_order` < 0         | `400 VALIDATION_ERROR` | `sort_order must be 0 or greater`                           |
| `description` empty      | `400 VALIDATION_ERROR` | `description must have at least one entry`                  |

---

## Get Subscription Plan

`GET /api/v1/subscriptions/plans/{id}`

> Requires `ADMIN` role.

Returns the full details of a single subscription plan, including all assigned limits with their limit key metadata.

### Path Parameters

| Parameter | Type | Description                 |
|-----------|------|-----------------------------|
| `id`      | Long | ID of the subscription plan |

### Response `200 OK`

```json
{
  "subscription_plan": {
    "id": 3,
    "currency_id": 1,
    "code": "PROFESSIONAL",
    "billing_cycle": "MONTHLY",
    "trial_period_days": 14,
    "sort_order": 2,
    "name": "Professional",
    "price": "29.00",
    "description": [
      "20 API keys",
      "10,000 API requests per month",
      "5 CRM accounts",
      "Priority support",
      "14-day free trial"
    ],
    "is_public": true,
    "limits": [
      {
        "id": 10,
        "limit_key_id": 1,
        "limit_key_code": "API_KEYS",
        "limit_key_name": "API Key Creation Limit",
        "limit_key_unit": "COUNT",
        "limit_value": 20
      },
      {
        "id": 11,
        "limit_key_id": 2,
        "limit_key_code": "API_REQUESTS",
        "limit_key_name": "API Request Limit",
        "limit_key_unit": "COUNT",
        "limit_value": 10000
      },
      {
        "id": 12,
        "limit_key_id": 3,
        "limit_key_code": "CRM_ACCOUNTS",
        "limit_key_name": "CRM Account Limit",
        "limit_key_unit": "COUNT",
        "limit_value": 5
      }
    ]
  }
}
```

> The `limits` array always reflects the current plan configuration. Each entry embeds the key's `code`, `name`, and
> `unit` to avoid extra lookups on the client side.

---

## List All Subscription Plans (Admin)

`GET /api/v1/subscriptions/plans`

> Requires `ADMIN` role.

Returns a paginated list of all active (non-deleted) subscription plans, regardless of `is_public` status. This is the
admin view — it shows hidden plans as well.

### Query Parameters

| Parameter  | Type   | Default      | Allowed Values                                                          | Description           |
|------------|--------|--------------|-------------------------------------------------------------------------|-----------------------|
| `page`     | int    | `0`          | >= 0                                                                    | Zero-based page index |
| `size`     | int    | `10`         | 1 – 50                                                                  | Items per page        |
| `sort_by`  | String | `sort_order` | `id`, `code`, `name`, `price`, `billingCycle`, `sortOrder`, `createdAt` | Field to sort by      |
| `sort_dir` | String | `ASC`        | `ASC`, `DESC`                                                           | Sort direction        |

> Default sort of `sort_order ASC` ensures plans are returned in the display order defined by the admin.

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "FREE",
      "name": "Free",
      "price": "0.00",
      "billing_cycle": "MONTHLY",
      "trial_period_days": 0,
      "sort_order": 1,
      "is_public": true
    },
    {
      "id": 3,
      "code": "PROFESSIONAL",
      "name": "Professional",
      "price": "29.00",
      "billing_cycle": "MONTHLY",
      "trial_period_days": 14,
      "sort_order": 2,
      "is_public": true
    },
    {
      "id": 4,
      "code": "ENTERPRISE",
      "name": "Enterprise",
      "price": "99.00",
      "billing_cycle": "ANNUAL",
      "trial_period_days": 30,
      "sort_order": 3,
      "is_public": true
    },
    {
      "id": 5,
      "code": "BETA_TESTER",
      "name": "Beta Tester",
      "price": "0.00",
      "billing_cycle": "MONTHLY",
      "trial_period_days": 0,
      "sort_order": 99,
      "is_public": false
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 4,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

> The list endpoint returns a **summary** projection — no `description` or `limits` array. Use `GET /{id}` to fetch the
> full plan with all limit details.

---

## List Public Subscription Plans

`GET /api/v1/subscriptions/plans/public`

> No special role required. Available to any authenticated user.

Returns a paginated list of plans where `is_public = true` and `is_active = true` and `is_deleted = false`. This is the
tenant-facing endpoint used to display available pricing tiers on a plan selection page.

### Query Parameters

| Parameter  | Type   | Default      | Allowed Values                                                          | Description           |
|------------|--------|--------------|-------------------------------------------------------------------------|-----------------------|
| `page`     | int    | `0`          | >= 0                                                                    | Zero-based page index |
| `size`     | int    | `10`         | 1 – 50                                                                  | Items per page        |
| `sort_by`  | String | `sort_order` | `id`, `code`, `name`, `price`, `billingCycle`, `sortOrder`, `createdAt` | Field to sort by      |
| `sort_dir` | String | `ASC`        | `ASC`, `DESC`                                                           | Sort direction        |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "FREE",
      "name": "Free",
      "price": "0.00",
      "billing_cycle": "MONTHLY",
      "trial_period_days": 0,
      "sort_order": 1,
      "is_public": true
    },
    {
      "id": 3,
      "code": "PROFESSIONAL",
      "name": "Professional",
      "price": "29.00",
      "billing_cycle": "MONTHLY",
      "trial_period_days": 14,
      "sort_order": 2,
      "is_public": true
    },
    {
      "id": 4,
      "code": "ENTERPRISE",
      "name": "Enterprise",
      "price": "99.00",
      "billing_cycle": "ANNUAL",
      "trial_period_days": 30,
      "sort_order": 3,
      "is_public": true
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 3,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

> Hidden plans (`is_public = false`) are never included. `BETA_TESTER` in the example above would not appear here even
> though it is active. Use `GET /api/v1/subscriptions/plans/{id}` if a tenant needs to view a specific plan (e.g. via a
> referral link that supplies the plan ID directly).

---

## Update Subscription Plan

`PUT /api/v1/subscriptions/plans/{id}`

> Requires `ADMIN` role.

Replaces all updatable fields and the entire `limits` collection. The existing limits are cleared and rebuilt from the
request body — this is a full replacement, not a patch. `code` cannot be changed.

### Path Parameters

| Parameter | Type | Description                 |
|-----------|------|-----------------------------|
| `id`      | Long | ID of the subscription plan |

### Request Body

```json
{
  "currency_id": 1,
  "name": "Professional",
  "price": "35.00",
  "sort_order": 2,
  "is_public": true,
  "description": [
    "20 API keys",
    "10,000 API requests per month",
    "5 CRM accounts",
    "Priority support",
    "Advanced analytics"
  ],
  "limits": [
    {
      "limit_key_id": 1,
      "limit_value": 20
    },
    {
      "limit_key_id": 2,
      "limit_value": 10000
    },
    {
      "limit_key_id": 3,
      "limit_value": 5
    },
    {
      "limit_key_id": 6,
      "limit_value": 1
    }
  ]
}
```

### Request Fields

Same as Create, minus `code` (which is immutable).

| Field                   | Type           | Required | Validation                                      |
|-------------------------|----------------|----------|-------------------------------------------------|
| `currency_id`           | Long           | Yes      | Must reference an active currency               |
| `billing_cycle`         | `BillingCycle` | Yes      | `MONTHLY`, `QUARTERLY`, `ANNUAL`, or `LIFETIME` |
| `trial_period_days`     | Integer        | No       | >= 0 (default `0`)                              |
| `name`                  | String         | Yes      | Not blank, max 100 chars                        |
| `price`                 | Decimal        | No       | >= 0.00 (default `0.00`)                        |
| `sort_order`            | Integer        | No       | >= 0 (default `0`)                              |
| `is_public`             | Boolean        | No       | Default `true`                                  |
| `description`           | String[]       | Yes      | Array, minimum 1 entry                          |
| `limits`                | Object[]       | No       | No duplicate `limit_key_id`                     |
| `limits[].limit_key_id` | Long           | Yes      | Must reference an active limit key              |
| `limits[].limit_value`  | Long           | Yes      | >= -1 (`-1` = unlimited)                        |

### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

> **Important:** The `limits` collection is always replaced entirely. If you omit a limit key that was previously
> assigned, it will be removed. If you want to keep existing limits, include them all in the request.

> **Impact on existing subscribers:** Updating a plan does **not** change the limits of existing tenant subscriptions.
> Tenant subscriptions snapshot limits at subscription time. Only future subscribers see the updated values.

---

## Delete Subscription Plan

`DELETE /api/v1/subscriptions/plans/{id}`

> Requires `ADMIN` role.

Soft-deletes the subscription plan. Sets `is_deleted = true` and `is_active = false`. The record remains in the
database — it will no longer appear in any list or lookup response, but existing tenant subscriptions that reference
this plan are unaffected.

### Path Parameters

| Parameter | Type | Description                 |
|-----------|------|-----------------------------|
| `id`      | Long | ID of the subscription plan |

### Response `200 OK`

```json
{
  "success": true,
  "id": 3
}
```

> **Warning:** Do not delete a plan that has active tenant subscriptions. Deleting the plan does not cancel those
> subscriptions — they continue running against their snapshotted limits. However, deleting a plan removes it from the
> public listing and prevents new signups. To retire a plan gracefully, first set `is_public = false` via an update to
> stop new subscriptions, then delete it once all existing subscribers have migrated.

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "SubscriptionPlan not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                                                                   |
|-------------|----------------------------|---------------------------------------------------------------------------------------------------------|
| `400`       | `VALIDATION_ERROR`         | Field constraint violation (blank name, price < 0, sort_order < 0, empty description, limit_value < -1) |
| `400`       | `INVALID_ARGUMENT`         | Duplicate `code` on create, duplicate `limit_key_id` in `limits` array                                  |
| `403`       | `UNAUTHORIZED`             | Non-admin attempting a protected operation                                                              |
| `404`       | `ENTITY_NOT_FOUND`         | Plan, currency, or limit key not found or already soft-deleted                                          |
| `409`       | `DATA_INTEGRITY_VIOLATION` | Database-level unique constraint violation (fallback)                                                   |
| `500`       | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                                                                 |

---

## Design Rules

1. **`code` is immutable.** It is the stable machine identifier used when subscribing tenants to plans, producing
   subscription history, and referencing plans in code. Never change it after a plan enters production.

2. **Plans do not enforce limits themselves.** They only store the configuration. Enforcement is done by
   `UsageEnforcementService` at request time using the tenant's active subscription snapshot.

3. **Update is a full replacement.** `PUT` replaces the entire `limits` collection using `orphanRemoval = true`. Always
   send the complete desired state of limits — not just the changes.

4. **`limit_value = -1` means unlimited.** This is the only special value. The enforcement layer skips the check for
   any key whose subscribed value is `-1`. `limit_value = 0` means the capability is fully disabled.

5. **Soft delete only.** Plans are never physically removed. This preserves referential integrity with existing tenant
   subscriptions and maintains audit history.

6. **Use `is_public = false` for stealth plans.** Plans under construction, internal/enterprise plans, or legacy plans
   should be hidden from the public listing until they are ready. This avoids cluttering the tenant-facing UI.

7. **Currency must be active.** The `currency_id` is validated on both create and update. If the currency is
   soft-deleted, the request is rejected.

8. **`description` is an array.** Each entry is a bullet point for use in pricing UIs. Minimum one entry is required
   to prevent plans with no visible feature description.

9. **`sort_order` controls display order.** Plans are returned sorted by `sort_order ASC` by default. Set this
   intentionally so the plan listing always renders in the correct order: Free → Professional → Enterprise.

---

## Design Rules — Limits Within a Plan

1. **Each Limit Key may appear at most once per plan.** Duplicate `limit_key_id` entries in a single create/update
   request are rejected with a `400 INVALID_ARGUMENT` error.

2. **Not all Limit Keys must be assigned.** If a plan does not include a particular Limit Key, that key has no cap for
   subscribers on that plan. Enforcement skips unenforced keys.

3. **Limit Keys must be active.** If a Limit Key has been soft-deleted, it cannot be used in a new or updated plan.

4. **Plan limits are snapshotted at subscription time.** Changing a plan's limits after a tenant has subscribed does
   not retroactively change that tenant's limits. The tenant must upgrade or resubscribe to get new limits.

---

## Recommended Plan Structure

The following structure is a recommended starting point for a CRM gateway SaaS with three tiers:

### Limit Keys to Create First

| Code            | Name                   | Data Type | Category | Unit    |
|-----------------|------------------------|-----------|----------|---------|
| `API_KEYS`      | API Key Creation Limit | INTEGER   | RESOURCE | `COUNT` |
| `API_REQUESTS`  | API Request Limit      | INTEGER   | USAGE    | `COUNT` |
| `CRM_ACCOUNTS`  | CRM Account Limit      | INTEGER   | RESOURCE | `COUNT` |
| `WEBHOOK_CALLS` | Webhook Call Limit     | INTEGER   | USAGE    | `COUNT` |
| `TEAM_MEMBERS`  | Team Member Limit      | INTEGER   | RESOURCE | `COUNT` |
| `EXPORT_CSV`    | CSV Export Access      | BOOLEAN   | FEATURE  | `NONE`  |

### Plans

| Field               | Free      | Professional   | Enterprise   |
|---------------------|-----------|----------------|--------------|
| `code`              | `FREE`    | `PROFESSIONAL` | `ENTERPRISE` |
| `name`              | Free      | Professional   | Enterprise   |
| `billing_cycle`     | `MONTHLY` | `MONTHLY`      | `ANNUAL`     |
| `trial_period_days` | 0         | 14             | 30           |
| `price`             | 0.00      | 29.00          | 99.00        |
| `sort_order`        | 1         | 2              | 3            |
| `is_public`         | true      | true           | true         |

### Limits Per Plan

| Limit Key       | Free | Professional | Enterprise |
|-----------------|------|--------------|------------|
| `API_KEYS`      | 2    | 20           | 100        |
| `API_REQUESTS`  | 500  | 10,000       | 100,000    |
| `CRM_ACCOUNTS`  | 1    | 5            | -1 (∞)     |
| `WEBHOOK_CALLS` | 100  | 5,000        | -1 (∞)     |
| `TEAM_MEMBERS`  | 1    | 10           | -1 (∞)     |
| `EXPORT_CSV`    | 0    | 1            | 1          |

> `EXPORT_CSV` uses `BOOLEAN` semantics: `0` = disabled, `1` = enabled. Free tier gets no CSV export; Pro and
> Enterprise do.
