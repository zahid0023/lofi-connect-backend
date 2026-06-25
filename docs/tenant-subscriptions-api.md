# Tenant Subscriptions API

Base URL: `/api/v1/subscriptions/tenant-subscriptions`

A **Tenant Subscription** is the record that makes a user a tenant of the API gateway. It links a user account to a
Subscription Plan, captures the billing period, and is the source of truth for what that tenant is allowed to do. An
App Key cannot be generated without an active subscription.

---

## Table of Contents

1. [Concept](#concept)
2. [How a Tenant is Born](#how-a-tenant-is-born)
3. [Endpoints](#endpoints)
4. [Data Model](#data-model)
5. [Subscription Status Lifecycle](#subscription-status-lifecycle)
6. [Subscribe](#subscribe)
7. [Upgrade Plan](#upgrade-plan)
8. [Get My Active Subscription](#get-my-active-subscription)
9. [Cancel Subscription](#cancel-subscription)
10. [List All Subscriptions (Admin)](#list-all-subscriptions-admin)
11. [Error Responses](#error-responses)
12. [Business Rules](#business-rules)

---

## Concept

```
UserEntity  ──subscribes──►  TenantSubscriptionEntity  ──references──►  SubscriptionPlanEntity
                                      │                                         │
                                   (status,                               (price, billing,
                                  start/end date)                          limits config)
                                      │
                                      ▼
                             LofiConnectAppKeyEntity
                           (API key tied to this subscription)
```

| Concept                 | Responsibility                                              |
|-------------------------|-------------------------------------------------------------|
| **User**                | The authenticated account                                   |
| **Tenant Subscription** | Binds the user to a plan; defines billing period and status |
| **Subscription Plan**   | The tier the user is on (Free, Professional, Enterprise)    |
| **App Key**             | An API credential generated against an active subscription  |

---

## How a Tenant is Born

```
1.  POST /api/v1/auth/registration/user    → create account (USER role, auto-activated)
2.  POST /api/v1/auth/login                → receive JWT access token
3.  GET  /api/v1/subscriptions/plans/public → browse available plans (no auth needed)
4.  POST /api/v1/subscriptions/tenant-subscriptions   Bearer <token>
        { "plan_id": 3 }                   → become a tenant (ACTIVE or TRIAL)
5.  POST /api/v1/app-keys/generate         Bearer <token>
        { "name": "Production Key" }       → App Key tied to this subscription
6.  GET  /api/v1/ghl/contacts/...
        X-App-Key: <key>                   → use the API gateway
```

A user without an active subscription cannot generate an App Key and cannot use the gateway.

---

## Endpoints

| Method   | Path                                                 | Auth          | Description                   |
|----------|------------------------------------------------------|---------------|-------------------------------|
| `POST`   | `/api/v1/subscriptions/tenant-subscriptions`         | Authenticated | Subscribe to a plan           |
| `POST`   | `/api/v1/subscriptions/tenant-subscriptions/upgrade` | Authenticated | Switch to a different plan    |
| `GET`    | `/api/v1/subscriptions/tenant-subscriptions/me`      | Authenticated | Get my active subscription    |
| `DELETE` | `/api/v1/subscriptions/tenant-subscriptions/cancel`  | Authenticated | Cancel my active subscription |
| `GET`    | `/api/v1/subscriptions/tenant-subscriptions`         | `ADMIN` role  | List all tenant subscriptions |

---

## Data Model

### Subscription Fields

| Field           | Type                       | Description                                                       |
|-----------------|----------------------------|-------------------------------------------------------------------|
| `id`            | Long                       | Auto-generated identifier                                         |
| `user_id`       | Long                       | The user who owns this subscription                               |
| `plan_id`       | Long                       | The subscription plan ID                                          |
| `plan_code`     | String                     | The plan's machine-readable code (e.g. `PROFESSIONAL`)            |
| `plan_name`     | String                     | The plan's display name (e.g. `Professional`)                     |
| `billing_cycle` | `BillingCycle`             | `MONTHLY`, `QUARTERLY`, `ANNUAL`, or `LIFETIME`                   |
| `price`         | Decimal                    | The plan price at subscription time                               |
| `status`        | `TenantSubscriptionStatus` | Current state: `ACTIVE`, `TRIAL`, `CANCELLED`, `EXPIRED`          |
| `start_date`    | Instant (ISO-8601)         | When the subscription started                                     |
| `end_date`      | Instant (ISO-8601)         | When the current billing period ends. `null` for `LIFETIME` plans |
| `trial_ends_at` | Instant (ISO-8601)         | When the trial period ends. `null` if no trial                    |

### Subscription Status

| Status      | Meaning                                                          |
|-------------|------------------------------------------------------------------|
| `TRIAL`     | Within the plan's free trial period. Full access, no charge yet. |
| `ACTIVE`    | Paying subscription in good standing.                            |
| `CANCELLED` | User cancelled. Access ends at `end_date`.                       |
| `EXPIRED`   | Billing period ended and was not renewed.                        |

---

## Subscription Status Lifecycle

```
                      ┌─────────────────────────────────────────┐
                      │            POST /subscribe               │
                      │                                          │
              trial_period_days > 0           trial_period_days = 0
                      │                                          │
                      ▼                                          ▼
                   TRIAL ──────────────────────────────────► ACTIVE
                      │                                          │
               (trial ends)                            POST /cancel │ POST /upgrade
                      │                                          │
                      ▼                                          ▼
                   ACTIVE                                   CANCELLED
                      │
                POST /cancel │ POST /upgrade
                      │
                      ▼
                 CANCELLED
```

**Key transitions:**

| Action                 | From                | To                                |
|------------------------|---------------------|-----------------------------------|
| Subscribe (with trial) | —                   | `TRIAL`                           |
| Subscribe (no trial)   | —                   | `ACTIVE`                          |
| Cancel                 | `ACTIVE` or `TRIAL` | `CANCELLED`                       |
| Upgrade                | `ACTIVE` or `TRIAL` | old → `CANCELLED`, new → `ACTIVE` |

---

## Subscribe

`POST /api/v1/subscriptions/tenant-subscriptions`

> Requires authentication (`Authorization: Bearer <token>`).

Subscribes the authenticated user to a subscription plan. If the plan has `trial_period_days > 0`, the subscription
starts in `TRIAL` status and `trial_ends_at` is populated. Otherwise it starts as `ACTIVE` immediately.

A user can only have one active (ACTIVE or TRIAL) subscription at a time. Attempting to subscribe while one is active
returns `409 ACTIVE_SUBSCRIPTION_EXISTS`. Use `POST /upgrade` instead.

### Request Body

```json
{
  "plan_id": 3
}
```

### Request Fields

| Field     | Type | Required | Description                                 |
|-----------|------|----------|---------------------------------------------|
| `plan_id` | Long | Yes      | ID of the subscription plan to subscribe to |

### Response `201 Created`

```json
{
  "success": true,
  "id": 42
}
```

Where `id` is the ID of the newly created `TenantSubscriptionEntity`.

### End Date Calculation

The `end_date` is calculated automatically from the plan's `billing_cycle` at subscription time:

| `billing_cycle` | `end_date`              |
|-----------------|-------------------------|
| `MONTHLY`       | `start_date` + 30 days  |
| `QUARTERLY`     | `start_date` + 90 days  |
| `ANNUAL`        | `start_date` + 365 days |
| `LIFETIME`      | `null` (never expires)  |

### Pre-Subscribe Validations

| Check                                    | Error                            | Message                                                                            |
|------------------------------------------|----------------------------------|------------------------------------------------------------------------------------|
| Already has ACTIVE or TRIAL subscription | `409 ACTIVE_SUBSCRIPTION_EXISTS` | `An active subscription already exists. Use the upgrade endpoint to switch plans.` |
| `plan_id` not found or deleted           | `404 ENTITY_NOT_FOUND`           | `Subscription plan not found with id: 99`                                          |
| `plan_id` missing                        | `400 VALIDATION_ERROR`           | `plan_id: plan_id is required`                                                     |
| Not authenticated                        | `401`                            | —                                                                                  |

---

## Upgrade Plan

`POST /api/v1/subscriptions/tenant-subscriptions/upgrade`

> Requires authentication (`Authorization: Bearer <token>`).

Switches the authenticated user to a different plan immediately. The current subscription is cancelled and a brand-new
subscription is created for the new plan. The new subscription starts from the current moment — there is no prorated
billing credit in the current implementation.

**Important:** All App Keys are tied to the old subscription. After an upgrade, the user must generate new App Keys
against the new subscription.

### Request Body

```json
{
  "new_plan_id": 5
}
```

### Request Fields

| Field         | Type | Required | Description                         |
|---------------|------|----------|-------------------------------------|
| `new_plan_id` | Long | Yes      | ID of the plan to upgrade/switch to |

### Response `200 OK`

```json
{
  "success": true,
  "id": 43
}
```

Where `id` is the ID of the newly created subscription for the new plan.

### Validations

| Check                   | Error                        | Message                                                           |
|-------------------------|------------------------------|-------------------------------------------------------------------|
| No active subscription  | `422 NO_ACTIVE_SUBSCRIPTION` | `No active subscription found. Subscribe first before upgrading.` |
| `new_plan_id` not found | `404 ENTITY_NOT_FOUND`       | `Subscription plan not found with id: 99`                         |
| `new_plan_id` missing   | `400 VALIDATION_ERROR`       | `new_plan_id: new_plan_id is required`                            |
| Not authenticated       | `401`                        | —                                                                 |

---

## Get My Active Subscription

`GET /api/v1/subscriptions/tenant-subscriptions/me`

> Requires authentication (`Authorization: Bearer <token>`).

Returns the full details of the authenticated user's current active (ACTIVE or TRIAL) subscription, including plan name,
billing cycle, price, status, and dates.

### Response `200 OK` — ACTIVE plan (no trial)

```json
{
  "subscription": {
    "id": 42,
    "user_id": 7,
    "plan_id": 3,
    "plan_code": "PROFESSIONAL",
    "plan_name": "Professional",
    "billing_cycle": "MONTHLY",
    "price": "29.00",
    "status": "ACTIVE",
    "start_date": "2026-06-25T10:00:00Z",
    "end_date": "2026-07-25T10:00:00Z",
    "trial_ends_at": null
  }
}
```

### Response `200 OK` — TRIAL plan

```json
{
  "subscription": {
    "id": 42,
    "user_id": 7,
    "plan_id": 3,
    "plan_code": "PROFESSIONAL",
    "plan_name": "Professional",
    "billing_cycle": "MONTHLY",
    "price": "29.00",
    "status": "TRIAL",
    "start_date": "2026-06-25T10:00:00Z",
    "end_date": "2026-07-25T10:00:00Z",
    "trial_ends_at": "2026-07-09T10:00:00Z"
  }
}
```

### Response `200 OK` — LIFETIME plan

```json
{
  "subscription": {
    "id": 42,
    "user_id": 7,
    "plan_id": 1,
    "plan_code": "ENTERPRISE_LIFETIME",
    "plan_name": "Enterprise Lifetime",
    "billing_cycle": "LIFETIME",
    "price": "999.00",
    "status": "ACTIVE",
    "start_date": "2026-06-25T10:00:00Z",
    "end_date": null,
    "trial_ends_at": null
  }
}
```

### Error Responses

| Check                  | Error                        | Message                                    |
|------------------------|------------------------------|--------------------------------------------|
| No active subscription | `422 NO_ACTIVE_SUBSCRIPTION` | `No active subscription found for user: 7` |
| Not authenticated      | `401`                        | —                                          |

---

## Cancel Subscription

`DELETE /api/v1/subscriptions/tenant-subscriptions/cancel`

> Requires authentication (`Authorization: Bearer <token>`).

Cancels the authenticated user's active (ACTIVE or TRIAL) subscription. Sets `status = CANCELLED` and
`is_active = false`.

After cancellation:

- Existing App Keys tied to this subscription become unusable (the subscription is no longer ACTIVE/TRIAL).
- The user can subscribe to a new plan by calling `POST /api/v1/subscriptions/tenant-subscriptions` again.

### Response `200 OK`

```json
{
  "success": true,
  "id": 42
}
```

### Error Responses

| Check                            | Error                        | Message                                   |
|----------------------------------|------------------------------|-------------------------------------------|
| No active subscription to cancel | `422 NO_ACTIVE_SUBSCRIPTION` | `No active subscription found to cancel.` |
| Not authenticated                | `401`                        | —                                         |

---

## List All Subscriptions (Admin)

`GET /api/v1/subscriptions/tenant-subscriptions`

> Requires `ADMIN` role.

Returns a paginated list of all non-deleted tenant subscriptions across all users. Useful for admin dashboards and
monitoring.

### Query Parameters

| Parameter  | Type   | Default | Allowed Values                                      | Description           |
|------------|--------|---------|-----------------------------------------------------|-----------------------|
| `page`     | int    | `0`     | >= 0                                                | Zero-based page index |
| `size`     | int    | `10`    | 1 – 50                                              | Items per page        |
| `sort_by`  | String | `id`    | `id`, `status`, `startDate`, `endDate`, `createdAt` | Field to sort by      |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                       | Sort direction        |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 42,
      "user_id": 7,
      "start_date": "2026-06-25T10:00:00Z",
      "end_date": "2026-07-25T10:00:00Z",
      "status": "ACTIVE"
    },
    {
      "id": 43,
      "user_id": 12,
      "start_date": "2026-06-20T08:00:00Z",
      "end_date": "2026-07-20T08:00:00Z",
      "status": "TRIAL"
    }
  ],
  "current_page": 0,
  "total_pages": 1,
  "total_elements": 2,
  "page_size": 10,
  "has_next": false,
  "has_previous": false
}
```

> The list response is a **summary projection** — it shows `user_id`, `status`, and dates. It does not include plan
> details. This is by design to keep list queries fast. Cross-reference with the plan ID if needed.

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 409,
  "error": "ACTIVE_SUBSCRIPTION_EXISTS",
  "message": "An active subscription already exists. Use the upgrade endpoint to switch plans."
}
```

| HTTP Status | Error Code                   | Cause                                                           |
|-------------|------------------------------|-----------------------------------------------------------------|
| `400`       | `VALIDATION_ERROR`           | Missing required fields (e.g. `plan_id` is null)                |
| `401`       | —                            | Missing or invalid `Authorization` header                       |
| `404`       | `ENTITY_NOT_FOUND`           | Plan not found or soft-deleted                                  |
| `409`       | `ACTIVE_SUBSCRIPTION_EXISTS` | Tried to subscribe when already subscribed                      |
| `422`       | `NO_ACTIVE_SUBSCRIPTION`     | Tried to upgrade/cancel/view when no active subscription exists |
| `500`       | `INTERNAL_SERVER_ERROR`      | Unexpected server error                                         |

---

## Business Rules

1. **One active subscription per user.** A user can only hold one subscription in `ACTIVE` or `TRIAL` status at a time.
   Multiple historical subscriptions (CANCELLED, EXPIRED) are allowed.

2. **App Keys are tied to the subscription, not the user.** When a subscription is cancelled or upgraded, App Keys
   linked
   to the old subscription stop working. The user must generate new keys after subscribing/upgrading.

3. **Upgrade is immediate.** The old subscription is cancelled and a new one created in the same transaction. There is
   no
   billing proration in the current implementation.

4. **Subscriptions are never hard-deleted.** `is_deleted = true` and `is_active = false` are set for cancellations.
   The record remains in the database for audit purposes.

5. **Trial period is set by the plan.** If `trial_period_days > 0`, the subscription starts as `TRIAL`. If `0`, it
   starts as `ACTIVE` immediately. The trial end date is `start_date + trial_period_days`.

6. **`LIFETIME` plans have no end date.** `end_date = null` and the subscription never expires unless cancelled.

7. **End date is calculated from billing cycle.** See the end date table in the Subscribe section. The `end_date` is a
   snapshot taken at subscription time — it does not shift if the plan's `billing_cycle` changes later.

8. **Plans must be active and non-deleted.** If a plan has been soft-deleted, subscribing or upgrading to it returns
   `404 ENTITY_NOT_FOUND`.
