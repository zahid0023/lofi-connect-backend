# Lofi Connect API Documentation

**Base URL:** `https://{host}/api/v1`
**Version:** v1
**Auth:** JWT Bearer token (except where noted)

---

## Table of Contents

1. [Authentication](#1-authentication)
2. [App Keys](#2-app-keys)
3. [GHL OAuth (Connect GHL Account)](#3-ghl-oauth)
4. [Subscription Plans](#4-subscription-plans)
5. [Tenant Subscriptions](#5-tenant-subscriptions)
6. [Payments & Billing](#6-payments--billing)
7. [Refund Requests](#7-refund-requests)
8. [Admin Dashboard](#8-admin-dashboard)
9. [Usage Tracking](#9-usage-tracking)
10. [GHL Proxy — Contacts](#10-ghl-proxy--contacts)
11. [GHL Proxy — Calendars](#11-ghl-proxy--calendars)
12. [GHL Proxy — Conversations & Messages](#12-ghl-proxy--conversations--messages)
13. [GHL Proxy — Opportunities & Pipelines](#13-ghl-proxy--opportunities--pipelines)
14. [GHL Proxy — Users & Businesses](#14-ghl-proxy--users--businesses)
15. [GHL Proxy — Locations (Sub-Accounts)](#15-ghl-proxy--locations-sub-accounts)
16. [GHL Proxy — Invoices & Payments](#16-ghl-proxy--invoices--payments)
17. [GHL Proxy — Products & Store](#17-ghl-proxy--products--store)
18. [GHL Proxy — Funnels, Forms & Surveys](#18-ghl-proxy--funnels-forms--surveys)
19. [GHL Proxy — Media, Templates & Campaigns](#19-ghl-proxy--media-templates--campaigns)
20. [GHL Proxy — Custom Fields, Values & Menu Links](#20-ghl-proxy--custom-fields-values--menu-links)
21. [GHL Proxy — Blogs & Social Posts](#21-ghl-proxy--blogs--social-posts)
22. [GHL Proxy — Associations & Object Schemas](#22-ghl-proxy--associations--object-schemas)
23. [GHL Proxy — Snapshots](#23-ghl-proxy--snapshots)
24. [GHL Proxy — OAuth Social Integrations](#24-ghl-proxy--oauth-social-integrations)
25. [GHL Proxy — Workflows & Trigger Links](#25-ghl-proxy--workflows--trigger-links)
26. [Currencies](#26-currencies)
27. [Limit Keys](#27-limit-keys)
28. [Admin User Management](#28-admin-user-management)
29. [Error Responses](#29-error-responses)
30. [Enums Reference](#30-enums-reference)

---

## Authentication

All protected endpoints require:
```
Authorization: Bearer <access_token>
```

Tokens are JWT. Access tokens are short-lived; use the refresh token to obtain a new one.

---

## 1. Authentication

### POST `/auth/registration/user`
Register a new user account.

**Auth:** None

**Request Body:**
```json
{
  "username": "john@example.com",
  "password": "SecurePass123!",
  "first_name": "John",
  "last_name": "Doe"
}
```

**Response `201 Created`:**
```json
{
  "id": 42,
  "username": "john@example.com",
  "first_name": "John",
  "last_name": "Doe"
}
```

---

### POST `/auth/login`
Authenticate and receive JWT tokens.

**Auth:** None

**Request Body:**
```json
{
  "user_name": "john@example.com",
  "password": "SecurePass123!"
}
```

**Response `200 OK`:**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Errors:**
- `401 Unauthorized` — invalid credentials
- `403 Forbidden` — account disabled

---

### POST `/auth/forgot-password`
Send a password reset OTP to the user's email.

**Auth:** None

**Request Body:**
```json
{
  "user_name": "john@example.com"
}
```

**Response `200 OK`:**
```json
{
  "message": "OTP sent to registered email address."
}
```

---

### POST `/auth/verify-otp`
Verify the OTP received by email.

**Auth:** None

**Request Body:**
```json
{
  "user_name": "john@example.com",
  "otp": "123456"
}
```

---

### POST `/auth/reset-password`
Reset the user's password using the OTP.

**Auth:** None

**Request Body:**
```json
{
  "user_name": "john@example.com",
  "otp": "123456",
  "new_password": "NewSecurePass456!"
}
```

**Response `200 OK`:**
```json
"Password has been reset successfully."
```

---

## 2. App Keys

App Keys are credentials your users create to authenticate GHL API calls through the proxy. Each key can be linked to one GHL subaccount via OAuth.

### POST `/app-keys/generate`
Generate a new app key for the authenticated user.

**Auth:** JWT
**Requires:** Active subscription

**Request Body:**
```json
{
  "name": "My Production Key",
  "description": "Used for CRM automation"
}
```

**Response `201 Created`:**
```json
{
  "id": 7,
  "name": "My Production Key",
  "description": "Used for CRM automation",
  "key": "lc_live_aBcDeFgHiJkLmNoPqRsTuVwXyZ",
  "is_active": true,
  "created_at": "2026-07-04T10:00:00Z"
}
```

> **Important:** The full `key` value is only returned once at creation. Store it securely.

---

### GET `/app-keys`
List all app keys for the authenticated user.

**Auth:** JWT

**Response `200 OK`:**
```json
[
  {
    "id": 7,
    "name": "My Production Key",
    "description": "Used for CRM automation",
    "key_preview": "lc_live_aBcD...XyZ",
    "is_active": true,
    "created_at": "2026-07-04T10:00:00Z"
  }
]
```

---

## 3. GHL OAuth

Connect an App Key to a GoHighLevel subaccount via OAuth 2.0. Once connected, all GHL proxy calls made with that key are routed to the linked subaccount.

### GET `/authorization/ghl/init?app-key-id={id}`
Start the GHL OAuth flow. Redirects the browser to the GHL authorization page.

**Auth:** None (uses `app-key-id` to identify the key)

**Query Params:**

| Param | Type | Required | Description |
|---|---|---|---|
| `app-key-id` | Long | Yes | The app key to link after authorization |

**Response `302 Found`:**
Redirects to GHL authorization URL with all configured scopes and `state={app-key-id}`.

---

### GET `/authorization/redirect`
OAuth callback — GHL redirects here after the user approves access. Exchanges the code for tokens and saves them.

**Auth:** None (GHL callback)

**Query Params:**

| Param | Type | Description |
|---|---|---|
| `code` | String | Authorization code from GHL |
| `state` | String | The `app-key-id` passed during init |

**Response `302 Found`:**
Redirects to `{frontend_url}/portal/connections` on success.

---

### GET `/authorization/ghl/ping`
Health check for GHL connectivity.

**Auth:** None

**Response `200 OK`**

---

## 4. Subscription Plans

### GET `/subscriptions/plans/public`
List all publicly visible subscription plans with full limit and feature details.

**Auth:** None

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "name": "Starter",
    "code": "STARTER",
    "description": "Perfect for small teams",
    "price": 29.00,
    "billing_cycle": "MONTHLY",
    "trial_period_days": 14,
    "product_type": "STANDALONE",
    "paddle_price_id": "pri_01abc123",
    "currency": {
      "id": 1,
      "code": "USD",
      "symbol": "$"
    },
    "limits": [
      { "limit_key_code": "MONTHLY_OPERATIONS", "limit_value": 5000 },
      { "limit_key_code": "APP_KEYS", "limit_value": 3 }
    ],
    "is_active": true
  }
]
```

---

### GET `/subscriptions/plans/{id}`
Get full details for a single plan.

**Auth:** None

**Path Params:**

| Param | Type | Description |
|---|---|---|
| `id` | Long | Plan ID |

**Response `200 OK`:** Same shape as a single plan object above.

**Errors:**
- `404 Not Found` — plan not found

---

### GET `/subscriptions/plans`
Paginated list of all plans including inactive ones.

**Auth:** JWT + ADMIN role

**Query Params:** See [Pagination](#pagination).

**Response `200 OK`:** Paginated list of plan objects.

---

### POST `/subscriptions/plans`
Create a new subscription plan. Also provisions the product and price in Paddle automatically.

**Auth:** JWT + ADMIN role

**Request Body:**
```json
{
  "name": "Pro",
  "code": "PRO",
  "description": "For growing agencies",
  "price": 79.00,
  "billing_cycle": "MONTHLY",
  "trial_period_days": 7,
  "product_type": "STANDALONE",
  "currency_id": 1,
  "limits": [
    { "limit_key_id": 1, "limit_value": 20000 },
    { "limit_key_id": 2, "limit_value": 10 }
  ]
}
```

**`billing_cycle`:** `MONTHLY` | `QUARTERLY` | `ANNUAL` | `LIFETIME`
**`product_type`:** `STANDALONE` (user self-provisions GHL) | `BUNDLED` (admin provisions GHL manually)

**Response `201 Created`:** Created plan object with `paddle_price_id` set.

---

### PUT `/subscriptions/plans/{id}`
Update an existing plan.

**Auth:** JWT + ADMIN role

**Request Body:**
```json
{
  "name": "Pro Plus",
  "description": "Updated description",
  "is_active": true
}
```

**Response `200 OK`:** Updated plan object.

---

### DELETE `/subscriptions/plans/{id}`
Soft-delete a plan (sets `is_active=false`, `is_deleted=true`).

**Auth:** JWT + ADMIN role

**Response `200 OK`:**
```json
{ "success": true, "id": 1 }
```

---

## 5. Tenant Subscriptions

### Subscribe Flow (Paddle-hosted)

```
1. GET  /subscriptions/plans/public        → choose a plan
2. POST /payments/checkout { plan_id }     → get checkout_url
3. Open checkout_url in browser            → user pays via Paddle
4. Paddle fires webhook → subscription created on backend
5. Paddle redirects to /subscriptions/tenant-subscriptions/success
6. GET  /payments/status                   → poll until active = true
```

---

### GET `/subscriptions/tenant-subscriptions/me`
Get the authenticated user's active subscription.

**Auth:** JWT

**Response `200 OK`:**
```json
{
  "id": 15,
  "user_id": 42,
  "plan": {
    "id": 1,
    "name": "Starter",
    "code": "STARTER"
  },
  "status": "ACTIVE",
  "provisioning_status": "PROVISIONED",
  "start_date": "2026-07-01T00:00:00Z",
  "end_date": "2026-08-01T00:00:00Z",
  "trial_ends_at": null,
  "grace_period_starts_at": null,
  "cancelled_at": null
}
```

**Errors:**
- `404 Not Found` — no active subscription

---

### POST `/subscriptions/tenant-subscriptions/upgrade`
Upgrade or downgrade the plan via Paddle. The local subscription is updated asynchronously when Paddle fires the `subscription.updated` webhook.

**Auth:** JWT

**Request Body:**
```json
{
  "new_plan_id": 2
}
```

**Response `200 OK`:**
```json
{ "success": true, "message": "Plan change submitted to Paddle." }
```

---

### DELETE `/subscriptions/tenant-subscriptions/cancel`
Cancel the subscription at the end of the current billing period. The user retains full access until then.

**Auth:** JWT

**Response `200 OK`:**
```json
{
  "success": true,
  "message": "Cancellation scheduled at end of billing period. You will retain access until then."
}
```

---

### GET `/subscriptions/tenant-subscriptions/success`
Paddle success redirect handler. No JWT is available at this point — Paddle redirects the browser here. Immediately redirects to the frontend success page.

**Auth:** None

**Response `302 Found`:** Redirects to `{frontend_url}/subscription/success`.

---

### GET `/subscriptions/tenant-subscriptions`
Paginated list of all tenant subscriptions across all users.

**Auth:** JWT + ADMIN role

**Query Params:** See [Pagination](#pagination).

**Response `200 OK`:** Paginated list of subscription summary objects.

---

## 6. Payments & Billing

### POST `/payments/checkout`
Create a Paddle-hosted checkout session for a selected plan.

**Auth:** JWT

**Request Body:**
```json
{
  "plan_id": 1
}
```

**Response `200 OK`:**
```json
{
  "checkout_url": "https://buy.paddle.com/checkout/...",
  "transaction_id": "txn_01abc..."
}
```

> Open `checkout_url` in the browser. After payment, Paddle redirects to the configured success URL. Then poll `/payments/status`.

---

### GET `/payments/status`
Poll subscription activation status after checkout. Returns immediately with current state.

**Auth:** JWT

**Response `200 OK`:**
```json
{
  "active": true,
  "status": "ACTIVE",
  "subscription_id": 15,
  "plan_name": "Starter",
  "paddle_subscription_id": "sub_01abc..."
}
```

| `active` | Meaning |
|---|---|
| `false` | Webhook not yet received — keep polling |
| `true` | Subscription is live and ready |

---

### POST `/payments/webhooks/paddle`
Receive Paddle webhook events. **This endpoint is for Paddle only — do not call it directly.**

**Auth:** None (HMAC-SHA256 signature verification via `Paddle-Signature` header)

**Headers:**

| Header | Description |
|---|---|
| `Paddle-Signature` | Paddle HMAC signature for verification |

**Handled Events:**

| Event | Action |
|---|---|
| `subscription.created` | Create local subscription (TRIAL or deferred) |
| `subscription.activated` | Set status → ACTIVE, trigger provisioning |
| `subscription.updated` | Sync plan change and billing period |
| `subscription.cancelled` | Set status → CANCELLED, deprovision |
| `subscription.past_due` | Set status → PAST_DUE |
| `subscription.paused` | Set status → PAUSED |
| `subscription.resumed` | Set status → ACTIVE |
| `transaction.completed` | First payment: create subscription; renewal: extend end date |

**Response:** Always `200 OK` (even on error, to prevent Paddle retries).

---

## 7. Refund Requests

### POST `/subscriptions/refund-requests`
Submit a refund request for the active subscription.

**Auth:** JWT

**Request Body:**
```json
{
  "reason": "Service did not meet expectations",
  "details": "I was unable to use the GHL integration due to connectivity issues."
}
```

**Response `200 OK`:**
```json
{
  "id": 3,
  "subscription_id": 15,
  "status": "PENDING",
  "reason": "Service did not meet expectations",
  "submitted_at": "2026-07-04T10:00:00Z"
}
```

**Errors:**
- `409 Conflict` — refund already submitted for this subscription

---

### GET `/subscriptions/refund-requests/me`
List all refund requests submitted by the authenticated user.

**Auth:** JWT

**Response `200 OK`:**
```json
[
  {
    "id": 3,
    "subscription_id": 15,
    "status": "PENDING",
    "reason": "Service did not meet expectations",
    "submitted_at": "2026-07-04T10:00:00Z",
    "reviewed_at": null,
    "admin_note": null
  }
]
```

---

### GET `/admin/refund-requests`
List all pending refund requests for admin review.

**Auth:** JWT + ADMIN role

**Response `200 OK`:** List of refund request objects including user info.

---

### POST `/admin/refund-requests/{id}/approve`
Approve a refund request. Admin must then manually process the refund in Paddle.

**Auth:** JWT + ADMIN role

**Path Params:**

| Param | Type | Description |
|---|---|---|
| `id` | Long | Refund request ID |

**Request Body** *(optional)*:
```json
{
  "admin_note": "Approved — refund processed in Paddle dashboard."
}
```

**Response `200 OK`:**
```json
{ "success": true, "id": 3 }
```

---

### POST `/admin/refund-requests/{id}/reject`
Reject a refund request.

**Auth:** JWT + ADMIN role

**Request Body** *(optional)*:
```json
{
  "admin_note": "Outside the 7-day refund window."
}
```

**Response `200 OK`:**
```json
{ "success": true, "id": 3 }
```

---

## 8. Admin Dashboard

### GET `/admin/dashboard/stats`
Aggregate KPIs for the admin dashboard.

**Auth:** JWT + ADMIN role

**Response `200 OK`:**
```json
{
  "estimated_mrr": 12450.00,
  "active_subscriptions": 320,
  "trialing_subscriptions": 47,
  "past_due_subscriptions": 12,
  "grace_period_subscriptions": 5,
  "read_only_subscriptions": 3,
  "suspended_subscriptions": 1,
  "cancelled_this_month": 8,
  "new_customers_this_month": 34,
  "standalone_active": 290,
  "bundled_active": 30,
  "pending_provisioning": 4,
  "pending_refund_requests": 2
}
```

---

### GET `/admin/dashboard/provisioning-queue`
List BUNDLED subscriptions awaiting manual GHL provisioning (status: `PENDING` or `IN_PROGRESS`).

**Auth:** JWT + ADMIN role

**Response `200 OK`:**
```json
[
  {
    "subscription_id": 18,
    "user_id": 55,
    "plan_id": 3,
    "plan_name": "Agency Bundled",
    "plan_code": "AGENCY_BUNDLED",
    "subscription_status": "PROVISIONING_REQUIRED",
    "provisioning_status": "PENDING",
    "start_date": "2026-07-01T00:00:00Z",
    "created_at": "2026-07-01T09:15:00Z"
  }
]
```

---

### POST `/admin/subscriptions/{id}/provisioning/start`
Mark a BUNDLED subscription provisioning as started (`IN_PROGRESS`). Use when you begin setting up the GHL subaccount for the user.

**Auth:** JWT + ADMIN role

**Path Params:**

| Param | Type | Description |
|---|---|---|
| `id` | Long | Subscription ID |

**Response `200 OK`:**
```json
{ "success": true, "id": 18 }
```

---

### POST `/admin/subscriptions/{id}/provisioning/complete`
Mark provisioning as complete (`PROVISIONED`). Sets subscription status to `ACTIVE` and activates the user's account.

**Auth:** JWT + ADMIN role

**Response `200 OK`:**
```json
{ "success": true, "id": 18 }
```

---

## 9. Usage Tracking

All usage endpoints require a valid JWT. GHL proxy calls are tracked automatically — no action needed from integrators.

### GET `/usage/summary`
Full usage page for the authenticated user. Covers the current calendar month with month-over-month comparisons, plus 30-day charts and per-connection breakdowns.

**Auth:** JWT

**Response `200 OK`:**
```json
{
  "period_start": "2026-07-01T00:00:00Z",
  "period_end": "2026-07-04T14:32:00Z",

  "total_api_calls": 1240,
  "total_api_calls_last_month": 980,
  "total_api_calls_change_percent": 26.5,
  "total_api_calls_trend": "UP",

  "success_rate": 98.3,
  "success_rate_last_month": 97.1,
  "success_rate_change_pct": 1.2,
  "success_rate_trend": "UP",

  "error_count": 21,
  "error_count_last_month": 28,
  "error_count_change_percent": -25.0,
  "error_count_trend": "DOWN",

  "avg_calls_per_day": 310.0,

  "monthly_operation_limit": 5000,
  "usage_percentage": 24.8,

  "connected_crm_accounts": 3,

  "api_calls_over_time": [
    { "date": "2026-06-05", "total_calls": 120, "error_calls": 3, "success_calls": 117 },
    { "date": "2026-06-06", "total_calls": 95,  "error_calls": 1, "success_calls": 94  }
  ],

  "usage_by_connection": [
    {
      "app_key_id": 7,
      "location_id": "abc123xyz",
      "subaccount_name": "Acme Marketing",
      "call_count": 820
    },
    {
      "app_key_id": 9,
      "location_id": "def456uvw",
      "subaccount_name": "Beta Agency",
      "call_count": 420
    }
  ],

  "top_endpoints": [
    {
      "http_method": "GET",
      "endpoint": "/api/v1/ghl/contacts/abc123",
      "call_count": 340,
      "avg_response_time_ms": 185.4
    },
    {
      "http_method": "POST",
      "endpoint": "/api/v1/ghl/contacts",
      "call_count": 210,
      "avg_response_time_ms": 240.1
    }
  ]
}
```

**Field Notes:**

| Field | Note |
|---|---|
| `*_change_percent` | `null` when last month had zero calls (no prior data) |
| `*_trend` | `"UP"` / `"DOWN"` / `"UNCHANGED"` |
| `success_rate_change_pct` | Absolute percentage-point change (not relative %) |
| `monthly_operation_limit` | `null` = unlimited plan |
| `usage_percentage` | `null` when plan is unlimited |
| `api_calls_over_time` | Last 30 days, only days with activity included |

---

### GET `/usage/me`
Paginated raw API call history for the authenticated user.

**Auth:** JWT

**Query Params:**

| Param | Type | Required | Description |
|---|---|---|---|
| `app_key_id` | Long | No | Filter to a specific app key |
| `from` | ISO-8601 | No | Start timestamp (default: epoch) |
| `to` | ISO-8601 | No | End timestamp (default: now) |
| `page` | Int | No | Page number (default: 0) |
| `size` | Int | No | Page size (default: 20) |
| `sort` | String | No | Field to sort by (default: `requestedAt,desc`) |

**Example:**
```
GET /usage/me?from=2026-07-01T00:00:00Z&to=2026-07-04T23:59:59Z&size=50
```

**Response `200 OK`:** Paginated list:
```json
{
  "content": [
    {
      "id": 1001,
      "app_key_id": 7,
      "user_id": 42,
      "subscription_id": 15,
      "http_method": "GET",
      "endpoint": "/api/v1/ghl/contacts/abc123",
      "response_status": 200,
      "response_time_ms": 182,
      "requested_at": "2026-07-04T10:15:30Z"
    }
  ],
  "page": 0,
  "size": 20,
  "total_elements": 1240,
  "total_pages": 62
}
```

---

### GET `/admin/usage`
All API call logs across every user, with optional filters.

**Auth:** JWT + ADMIN role

**Query Params:**

| Param | Type | Required | Description |
|---|---|---|---|
| `user_id` | Long | No | Filter to a specific user |
| `app_key_id` | Long | No | Filter to a specific app key |
| `from` | ISO-8601 | No | Start timestamp |
| `to` | ISO-8601 | No | End timestamp |
| `page` / `size` / `sort` | — | No | Pagination |

**Example:**
```
GET /admin/usage?user_id=42&from=2026-07-01T00:00:00Z
```

**Response `200 OK`:** Same paginated format as `/usage/me`.

---

### GET `/admin/usage/users/{userId}`
All API call logs for a specific user. Shorthand for `/admin/usage?user_id={userId}`.

**Auth:** JWT + ADMIN role

**Path Params:**

| Param | Type | Description |
|---|---|---|
| `userId` | Long | Target user ID |

**Query Params:** `app_key_id`, `from`, `to`, `page`, `size`, `sort`

**Response `200 OK`:** Same paginated format.

---

## 10. GHL Proxy — Contacts

All GHL proxy endpoints:
- **Auth:** App Key via `X-App-Key: lc_live_...` header (JWT is NOT used)
- **Base path:** `/api/v1/ghl`
- **Tracked:** Every call is logged to usage tracking automatically

---

### GET `/ghl/contacts/{contact-id}`
Get a single contact.

### PUT `/ghl/contacts/{contact-id}`
Update a contact.

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane@example.com",
  "phone": "+15550001234",
  "tags": ["lead", "vip"]
}
```

### DELETE `/ghl/contacts/{contact-id}`
Delete a contact.

### POST `/ghl/contacts`
Create a new contact.

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane@example.com",
  "locationId": "abc123xyz",
  "phone": "+15550001234",
  "tags": ["lead"]
}
```

### POST `/ghl/contacts/upsert`
Create or update a contact by email/phone.

### GET `/ghl/contacts/business/{business-id}`
Get all contacts belonging to a business.

### GET `/ghl/contacts/search/duplicate`
Search for duplicate contacts.

### GET `/ghl/contacts/{contact-id}/appointments`
List appointments for a contact.

### GET `/ghl/contacts/{contact-id}/notes`
List notes on a contact.

### GET `/ghl/contacts/{contact-id}/notes/{note-id}`
Get a specific note.

### PUT `/ghl/contacts/{contact-id}/notes/{note-id}`
Update a note.

### DELETE `/ghl/contacts/{contact-id}/notes/{note-id}`
Delete a note.

### GET `/ghl/contacts/{contact-id}/tasks`
List tasks for a contact.

### GET `/ghl/contacts/{contact-id}/tasks/{task-id}`
Get a specific task.

### PUT `/ghl/contacts/{contact-id}/tasks/{task-id}`
Update a task.

### PUT `/ghl/contacts/{contact-id}/tasks/{task-id}/completed`
Mark a task as completed.

### DELETE `/ghl/contacts/{contact-id}/tasks/{task-id}`
Delete a task.

### POST `/ghl/contacts/{contact-id}/{workflow-id}`
Add a contact to a workflow.

### DELETE `/ghl/contacts/{contact-id}/{workflow-id}`
Remove a contact from a workflow.

### DELETE `/ghl/contacts/{contact-id}/campaigns/{campaign-id}`
Remove a contact from a campaign.

### DELETE `/ghl/contacts/{contact-id}/campaigns/removeAll`
Remove a contact from all campaigns.

### DELETE `/ghl/contacts/{contact-id}/followers`
Remove followers from a contact.

### DELETE `/ghl/contacts/{contact-id}/tags`
Remove tags from a contact.

---

## 11. GHL Proxy — Calendars

### GET `/ghl/calendars`
List all calendars.

### GET `/ghl/calendars/{calendar-id}`
Get a calendar.

### DELETE `/ghl/calendars/{calendar-id}`
Delete a calendar.

### GET `/ghl/calendars/{calendar-id}/free-slots`
Get available time slots for a calendar.

### GET `/ghl/calendars/groups`
List calendar groups.

### POST `/ghl/calendars/groups`
Create a calendar group (accepts group data in body).

### PUT `/ghl/calendars/groups/{group-id}`
Update a calendar group.

### PUT `/ghl/calendars/groups/{group-id}/status`
Enable or disable a calendar group.

### DELETE `/ghl/calendars/groups/{group-id}`
Delete a calendar group.

### GET `/ghl/calendars/events/appointments/{appointmentId}`
Get an appointment.

### PUT `/ghl/calendars/events/appointments/{appointment-id}`
Update an appointment.

### GET `/ghl/calendars/events/events`
List calendar events.

### PUT `/ghl/calendars/events/block-slots/{eventId}`
Update a blocked-time slot.

### DELETE `/ghl/calendars/events/events/{eventId}`
Delete a calendar event.

### GET `/ghl/calendars/events/blocked-slots`
List blocked time slots.

### GET `/ghl/calendars/notifications/{calendar-id}`
List notifications for a calendar.

### GET `/ghl/calendars/notifications/{calendar-id}/{notification-id}`
Get a specific notification.

### PUT `/ghl/calendars/notifications/{calendar-id}/{notification-id}`
Update a notification.

### DELETE `/ghl/calendars/notifications/{calendar-id}/{notification-id}`
Delete a notification.

### GET `/ghl/appointments/{appointment-id}/notes`
List notes on an appointment.

### PUT `/ghl/appointments/{appointment-id}/notes/{note-id}`
Update an appointment note.

### DELETE `/ghl/appointments/{appointment-id}/notes/{note-id}`
Delete an appointment note.

---

## 12. GHL Proxy — Conversations & Messages

### GET `/ghl/conversations/search`
Search conversations.

### GET `/ghl/conversations/{conversation-id}`
Get a conversation.

### PUT `/ghl/conversations/{conversation-id}`
Update a conversation.

### DELETE `/ghl/conversations/{conversation-id}`
Delete a conversation.

### GET `/ghl/conversations/{conversationId}/messages`
List messages in a conversation.

### GET `/ghl/conversations/messages/{id}`
Get a specific message.

### GET `/ghl/conversations/messages/email/{id}`
Get an email message.

### DELETE `/ghl/conversations/messages/email/{email-message-id}/schedule`
Cancel a scheduled email.

### DELETE `/ghl/conversations/messages/{messageId}/schedule`
Cancel a scheduled message.

### POST `/ghl/messages/upload`
Upload a media attachment for messaging.

### PUT `/ghl/messages/{messageId}/status`
Update message status.

### GET `/ghl/messages/{messageId}/locations/{locationId}/recording`
Get a call recording.

### GET `/ghl/locations/{locationId}/messages/{messageId}/transcription`
Get a call transcription.

### GET `/ghl/locations/{locationId}/messages/{messageId}/transcription/download`
Download a call transcription.

### POST `/ghl/providers/live-chat/typing`
Send a typing indicator for live chat.

---

## 13. GHL Proxy — Opportunities & Pipelines

### GET `/ghl/opportunities/search`
Search opportunities.

**Query Params:** `location_id`, `pipeline_id`, `stage_id`, `status`, `assigned_to`, `q` (search term), pagination params.

### GET `/ghl/opportunities/{opportunity-id}`
Get an opportunity.

### POST `/ghl/opportunities`
Create an opportunity.

### PUT `/ghl/opportunities/{opportunity-id}`
Update an opportunity.

### PUT `/ghl/opportunities/{opportunity-id}/status`
Update opportunity status (`open`, `won`, `lost`, `abandoned`).

### DELETE `/ghl/opportunities/{opportunity-id}`
Delete an opportunity.

### POST `/ghl/opportunities/upsert`
Create or update an opportunity.

### POST `/ghl/opportunities/{id}/followers`
Add followers to an opportunity.

### DELETE `/ghl/opportunities/{id}/followers`
Remove followers from an opportunity.

### GET `/ghl/pipelines`
List all pipelines.

---

## 14. GHL Proxy — Users & Businesses

### GET `/ghl/users`
List users in the location.

### GET `/ghl/users/{user-id}`
Get a user.

### POST `/ghl/users`
Create a user.

### PUT `/ghl/users/{user-id}`
Update a user.

### DELETE `/ghl/users/{user-id}`
Delete a user.

### GET `/ghl/users/search`
Search users by keyword.

### POST `/ghl/users/search/filter-by-email`
Search users by email.

### GET `/ghl/companies/{company-id}`
Get a company.

### GET `/ghl/businesses`
List all businesses.

### GET `/ghl/businesses/{business-id}`
Get a business.

### PUT `/ghl/businesses/{business-id}`
Update a business.

### DELETE `/ghl/businesses/{business-id}`
Delete a business.

---

## 15. GHL Proxy — Locations (Sub-Accounts)

### GET `/ghl/locations/search`
Search locations (sub-accounts).

### GET `/ghl/locations/{location-id}`
Get a location.

### POST `/ghl/locations`
Create a location.

### PUT `/ghl/locations/{location-id}`
Update a location.

### DELETE `/ghl/locations/{location-id}`
Delete a location.

### GET `/ghl/locations/{location-id}/timezones`
List available timezones for a location.

### GET `/ghl/locations/tags`
List location tags.

### PUT `/ghl/locations/tags/{tag-id}`
Update a location tag.

### DELETE `/ghl/locations/tags/{tag-id}`
Delete a location tag.

### GET `/ghl/locations/custom-values/{custom-value-id}`
Get a custom value for a location.

### GET `/ghl/locations/{location-id}/accounts`
List sub-accounts for a location.

### DELETE `/ghl/locations/{location-id}/accounts/{id}`
Remove a sub-account.

### POST `/ghl/locations/tasks/search`
Search tasks across locations.

### GET `/ghl/locations/{location-id}/posts/{id}`
Get a social post.

### POST `/ghl/locations/{location-id}/posts`
Create a social post.

### PUT `/ghl/locations/{location-id}/posts/{id}`
Update a social post.

### DELETE `/ghl/locations/{location-id}/posts/{id}`
Delete a social post.

### POST `/ghl/locations/{location-id}/posts/list`
List posts for a location.

### POST `/ghl/locations/{location-id}/posts/bulk-delete`
Bulk delete posts.

---

## 16. GHL Proxy — Invoices & Payments

### GET `/ghl/invoices`
List invoices.

### GET `/ghl/invoices/{invoice-id}`
Get an invoice.

### PUT `/ghl/invoices/{invoice-id}`
Update an invoice.

### DELETE `/ghl/invoices/{invoice-id}`
Delete an invoice.

### POST `/ghl/invoices/{invoice-id}/send`
Send an invoice to the contact.

### POST `/ghl/invoices/{invoice-id}/void`
Void an invoice.

### POST `/ghl/invoices/{invoice-id}/record-payment`
Record a manual payment on an invoice.

### POST `/ghl/invoices/text2pay`
Send a text-to-pay link.

### GET `/ghl/invoices/generate-invoice-number`
Generate the next invoice number.

### GET `/ghl/invoices/template`
List invoice templates.

### GET `/ghl/invoices/template/{template-id}`
Get an invoice template.

### POST `/ghl/invoices/template`
Create an invoice template.

### PUT `/ghl/invoices/template/{template-id}`
Update an invoice template.

### DELETE `/ghl/invoices/template/{template-id}`
Delete an invoice template.

### GET `/ghl/invoices/schedules/list`
List invoice schedules.

### GET `/ghl/invoices/schedules/{schedule-id}`
Get a schedule.

### PUT `/ghl/invoices/schedules/{schedule-id}`
Update a schedule.

### DELETE `/ghl/invoices/schedules/{schedule-id}`
Delete a schedule.

### POST `/ghl/invoices/schedules/{schedule-id}/schedule`
Activate an invoice schedule.

### POST `/ghl/invoices/schedules/{schedule-id}/cancel`
Cancel an invoice schedule.

### POST `/ghl/invoices/schedules/{schedule-id}/auto-payment`
Configure auto-payment for a schedule.

### POST `/ghl/invoices/schedules/{schedule-id}/update-and-schedule`
Update and immediately schedule an invoice.

### GET `/ghl/invoices/estimate/list`
List estimates.

### GET `/ghl/invoices/estimate/{estimateId}`
Get an estimate.

### PUT `/ghl/invoices/estimate/{estimateId}`
Update an estimate.

### DELETE `/ghl/invoices/estimate/{estimateId}`
Delete an estimate.

### GET `/ghl/invoices/estimate/templates`
List estimate templates.

### GET `/ghl/invoices/estimate/template/{template-id}`
Get an estimate template.

### PUT `/ghl/invoices/estimate/template/{template-id}`
Update an estimate template.

### DELETE `/ghl/invoices/estimate/template/{template-id}`
Delete an estimate template.

### GET `/ghl/invoices/estimate/template/preview`
Preview an estimate template.

### GET `/ghl/invoices/estimate/generate-number`
Generate the next estimate number.

### PATCH `/ghl/invoices/estimate/stats/last-visited-at`
Update last-visited timestamp for estimates.

### PATCH `/ghl/invoices/stats/last-visited-at`
Update last-visited timestamp for invoices.

### GET `/ghl/payments/orders`
List payment orders.

### GET `/ghl/payments/orders/{order-id}`
Get an order.

### GET `/ghl/payments/orders/{order-id}/fulfillments`
List fulfillments for an order.

### POST `/ghl/payments/orders/{order-id}/fulfillments`
Create a fulfillment.

### GET `/ghl/payments/transactions`
List transactions.

### GET `/ghl/payments/transactions/{transaction-id}`
Get a transaction.

### GET `/ghl/payments/subscriptions`
List GHL payment subscriptions (GHL's own subscription system, separate from Lofi Connect plans).

### GET `/ghl/payments/subscriptions/{subscription-id}`
Get a GHL payment subscription.

### GET `/ghl/payments/coupons`
List coupons.

### GET `/ghl/payments/coupons/details`
Get coupon details.

### POST `/ghl/payments/coupons`
Create a coupon.

### PUT `/ghl/payments/coupons`
Update a coupon.

### DELETE `/ghl/payments/coupons`
Delete a coupon.

### GET `/ghl/payments/custom-providers`
List custom payment providers.

### POST `/ghl/payments/custom-providers`
Create a custom payment provider.

### DELETE `/ghl/payments/custom-providers`
Delete a custom payment provider.

### GET `/ghl/payments/integrations/provider/whitelabel`
Get whitelabel payment integration.

### POST `/ghl/payments/integrations/provider/whitelabel`
Create/update whitelabel integration.

### POST `/ghl/payments/custom-providers/payments/custom-provider/connect`
Connect a custom provider.

### POST `/ghl/payments/custom-providers/payments/custom-provider/disconnect`
Disconnect a custom provider.

### GET `/ghl/marketplace/billing/charges`
List marketplace charges.

### GET `/ghl/marketplace/billing/charges/{charge-id}`
Get a charge.

### POST `/ghl/marketplace/billing/charges`
Create a charge.

### DELETE `/ghl/marketplace/billing/charges/{charge-id}`
Delete a charge.

### GET `/ghl/marketplace/billing/charges/has-funds`
Check if location has sufficient funds.

---

## 17. GHL Proxy — Products & Store

### GET `/ghl/products`
List products.

### GET `/ghl/products/{product-id}`
Get a product.

### POST `/ghl/products`
Create a product.

### PUT `/ghl/products/{product-id}`
Update a product.

### DELETE `/ghl/products/{product-id}`
Delete a product.

### POST `/ghl/products/bulk-update`
Bulk update products.

### GET `/ghl/products/inventory`
Get inventory levels.

### POST `/ghl/products/inventory`
Update inventory.

### GET `/ghl/products/{product-id}/price`
List prices for a product.

### GET `/ghl/products/{product-id}/price/{price-id}`
Get a specific price.

### POST `/ghl/products/{product-id}/price`
Create a price.

### PUT `/ghl/products/{product-id}/price/{price-id}`
Update a price.

### DELETE `/ghl/products/{product-id}/price/{price-id}`
Delete a price.

### GET `/ghl/products/collections`
List collections.

### GET `/ghl/products/collections/{collection-id}`
Get a collection.

### POST `/ghl/products/collections`
Create a collection.

### PUT `/ghl/products/collections/{collection-id}`
Update a collection.

### DELETE `/ghl/products/collections/{collection-id}`
Delete a collection.

### GET `/ghl/products/reviews`
List product reviews.

### GET `/ghl/products/reviews/count`
Get review count.

### PUT `/ghl/products/reviews/{review-id}`
Update a review.

### DELETE `/ghl/products/reviews/{review-id}`
Delete a review.

### POST `/ghl/products/reviews/bulk-update`
Bulk update reviews.

### GET `/ghl/products/store/{store-id}/stats`
Get store statistics.

### POST `/ghl/products/store/{store-id}`
Update store settings.

### GET `/ghl/store/store-settings`
Get store settings.

### POST `/ghl/store/store-settings`
Update store settings.

### GET `/ghl/store/shipping-carriers`
List shipping carriers.

### GET `/ghl/store/shipping-carriers/{shipping-carrier-id}`
Get a shipping carrier.

### POST `/ghl/store/shipping-carriers`
Create a shipping carrier.

### PUT `/ghl/store/shipping-carriers/{shipping-carrier-id}`
Update a shipping carrier.

### DELETE `/ghl/store/shipping-carriers/{shipping-carrier-id}`
Delete a shipping carrier.

### GET `/ghl/store/shipping-zone`
List shipping zones.

### GET `/ghl/store/shipping-zone/{shipping-zone-id}`
Get a shipping zone.

### POST `/ghl/store/shipping-zone`
Create a shipping zone.

### PUT `/ghl/store/shipping-zone/{shipping-zone-id}`
Update a shipping zone.

### DELETE `/ghl/store/shipping-zone/{shipping-zone-id}`
Delete a shipping zone.

### GET `/ghl/store/shipping-zone/{shipping-zone-id}/shipping-rate`
List rates for a shipping zone.

### GET `/ghl/store/shipping-zone/{shipping-zone-id}/shipping-rate/{shipping-rate-id}`
Get a shipping rate.

### POST `/ghl/store/shipping-zone/{shipping-zone-id}/shipping-rate`
Create a shipping rate.

### POST `/ghl/store/shipping-zone/shipping-rates`
Bulk create shipping rates.

### PUT `/ghl/store/shipping-zone/{shipping-zone-id}/shipping-rate/{shipping-rate-id}`
Update a shipping rate.

### DELETE `/ghl/store/shipping-zone/{shipping-zone-id}/shipping-rate/{shipping-rate-id}`
Delete a shipping rate.

---

## 18. GHL Proxy — Funnels, Forms & Surveys

### GET `/ghl/funnels/list`
List funnels.

### GET `/ghl/funnels/pages`
List funnel pages.

### GET `/ghl/funnels/pages/count`
Count funnel pages.

### GET `/ghl/funnels/redirects/list`
List funnel redirects.

### PATCH `/ghl/funnels/redirects/{id}`
Update a redirect.

### DELETE `/ghl/funnels/redirects/{id}`
Delete a redirect.

### GET `/ghl/forms`
List forms.

### GET `/ghl/forms/submissions`
List form submissions.

### GET `/ghl/surveys`
List surveys.

### GET `/ghl/surveys/submissions`
List survey submissions.

---

## 19. GHL Proxy — Media, Templates & Campaigns

### GET `/ghl/medias/files`
List media files.

### POST `/ghl/medias/upload-file`
Upload a file to the media library.

### DELETE `/ghl/medias/{id}`
Delete a media file.

### GET `/ghl/templates`
List templates.

### GET `/ghl/templates/{id}`
Get a template (accepts `location-id` query param).

### DELETE `/ghl/templates/{id}`
Delete a template.

### GET `/ghl/email/builder`
Get email builder data.

### GET `/ghl/emails/schedule`
List scheduled emails.

### GET `/ghl/documents`
List documents.

### GET `/ghl/documents/templates`
List document templates.

### GET `/ghl/campaigns`
List campaigns.

### GET `/ghl/csv/{location-id}`
List CSV imports for a location.

### GET `/ghl/csv/{location-id}/{id}`
Get a specific CSV import.

### PATCH `/ghl/csv/{location-id}/{id}`
Update a CSV import.

### DELETE `/ghl/csv/{location-id}/{id}`
Delete a CSV import.

### DELETE `/ghl/csv/{location-id}/{csv-id}/post/{post-id}`
Delete a post from a CSV import.

---

## 20. GHL Proxy — Custom Fields, Values & Menu Links

### GET `/ghl/custom-fields`
List custom fields.

### GET `/ghl/custom-fields/{custom-field-id}`
Get a custom field.

### PUT `/ghl/custom-fields/{custom-field-id}`
Update a custom field.

### DELETE `/ghl/custom-fields/{custom-field-id}`
Delete a custom field.

### GET `/ghl/custom-fields/types`
List available custom field types.

### GET `/ghl/custom-values`
List custom values.

### PUT `/ghl/custom-values/{custom-value-id}`
Update a custom value.

### DELETE `/ghl/custom-values/{custom-value-id}`
Delete a custom value.

### GET `/ghl/custom-menu-links`
List custom menu links.

### GET `/ghl/custom-menu-links/{custom-menu-id}`
Get a custom menu link.

### PUT `/ghl/custom-menu-links/{custom-menu-id}`
Update a custom menu link.

### DELETE `/ghl/custom-menu-links/{custom-menu-id}`
Delete a custom menu link.

### GET `/ghl/tags`
List location tags.

### GET `/ghl/tags/{tag-id}`
Get a tag.

### POST `/ghl/tags`
Create a tag.

### POST `/ghl/tags/details`
Get details for multiple tags by ID.

---

## 21. GHL Proxy — Blogs & Social Posts

### GET `/ghl/blogs/site/all`
List blog sites.

### GET `/ghl/blogs/authors`
List blog authors.

### GET `/ghl/blogs/categories`
List blog categories.

### GET `/ghl/blogs/posts/all`
List blog posts.

### PUT `/ghl/blogs/posts/{post-id}`
Update a blog post.

### GET `/ghl/blogs/posts/url-slug-exists`
Check if a URL slug is taken.

### GET `/ghl/category/{location-id}`
List categories for a location.

### GET `/ghl/category/{location-id}/{id}`
Get a specific category.

---

## 22. GHL Proxy — Associations & Object Schemas

### GET `/ghl/associations`
List association schemas.

### GET `/ghl/associations/{association-id}`
Get an association schema.

### PUT `/ghl/associations/{association-id}`
Update an association schema.

### DELETE `/ghl/associations/{association-id}`
Delete an association schema.

### GET `/ghl/associations/key/{key_name}`
Get an association by key name.

### GET `/ghl/associations/object-key/{object-key}`
Get associations for an object key.

### GET `/ghl/associations/relations/{record-id}`
Get relations for a record.

### DELETE `/ghl/associations/relations/{relation-id}`
Delete a relation.

### GET `/ghl/objects`
List custom object schemas.

### GET `/ghl/objects/{key}`
Get a custom object schema.

### POST `/ghl/objects`
Create a custom object schema.

### PUT `/ghl/objects/{key}`
Update a schema.

### GET `/ghl/objects/{schema-key}/records/{id}`
Get a record.

### POST `/ghl/objects/{schema-key}/records`
Create a record.

### PUT `/ghl/objects/{schema-key}/records/{id}`
Update a record.

### DELETE `/ghl/objects/{schema-key}/records/{id}`
Delete a record.

### POST `/ghl/objects/{schema-key}/records/search`
Search records.

### POST `/ghl/search`
Global search.

### GET `/ghl/statictics`
Get statistics.

### POST `/ghl/statictics`
Submit statistics data.

---

## 23. GHL Proxy — Snapshots

### GET `/ghl/snapshots`
List snapshots.

### GET `/ghl/snapshots/status/{snapshot-id}`
Get snapshot import status.

### GET `/ghl/snapshots/status/{snapshot-id}/location/{location-id}`
Get snapshot status for a specific location.

### POST `/ghl/snapshots/share`
Share a snapshot.

---

## 24. GHL Proxy — OAuth Social Integrations

### GET `/ghl/oauth/facebook/start`
Start Facebook OAuth flow.

### GET `/ghl/oauth/facebook/locations/{location-id}/accounts/{account-id}/facebook/pages`
List connected Facebook pages.

### POST `/ghl/oauth/facebook/locations/{location-id}/accounts/{account-id}/facebook/attach`
Attach a Facebook page to a location.

### GET `/ghl/oauth/google/start`
Start Google OAuth flow.

### GET `/ghl/oauth/google/locations/{location-id}/accounts/{account-id}/google/locations`
List connected Google Business locations.

### POST `/ghl/oauth/google/locations/{location-id}/accounts/{account-id}/google/locations`
Connect a Google Business location.

### GET `/ghl/oauth/instagram/start`
Start Instagram OAuth flow.

### GET `/ghl/oauth/instagram/locations/{location-id}/accounts/{account-id}/instagram/accounts`
List connected Instagram accounts.

### POST `/ghl/oauth/instagram/locations/{location-id}/accounts/{account-id}/instagram/attach`
Attach an Instagram account.

### GET `/ghl/oauth/linkedin/start`
Start LinkedIn OAuth flow.

### GET `/ghl/oauth/linkedin/{location-id}/accounts/{account-id}`
Get LinkedIn connection.

### POST `/ghl/oauth/linkedin/{location-id}/accounts/{account-id}`
Connect LinkedIn.

### GET `/ghl/oauth/tiktok/start`
Start TikTok OAuth flow.

### GET `/ghl/oauth/tiktok/tiktok-business/start`
Start TikTok Business OAuth flow.

### GET `/ghl/oauth/tiktok/{location-id}/accounts/{account-id}`
Get TikTok connection.

### POST `/ghl/oauth/tiktok/{location-id}/accounts/{account-id}`
Connect TikTok.

### GET `/ghl/oauth/tiktok/{location-id}/tiktok-business/accounts/{account-id}`
Get TikTok Business connection.

---

## 25. GHL Proxy — Workflows & Trigger Links

### GET `/ghl/workflows`
List workflows.

### GET `/ghl/trigger`
List trigger links.

### POST `/ghl/trigger`
Create a trigger link.

### PUT `/ghl/trigger/{link-id}`
Update a trigger link.

### DELETE `/ghl/trigger/{link-id}`
Delete a trigger link.

---

## 26. Currencies

### GET `/currencies`
List all currencies (paginated).

**Auth:** JWT

**Query Params:** See [Pagination](#pagination).

### GET `/currencies/{id}`
Get a currency by ID.

**Auth:** JWT

### POST `/currencies`
Create a currency.

**Auth:** JWT (admin use)

**Request Body:**
```json
{
  "code": "USD",
  "name": "US Dollar",
  "symbol": "$"
}
```

### PUT `/currencies/{id}`
Update a currency.

**Auth:** JWT

### DELETE `/currencies/{id}`
Soft-delete a currency.

**Auth:** JWT

---

## 27. Limit Keys

Limit keys define what plan limits mean (e.g., `MONTHLY_OPERATIONS`, `APP_KEYS`).

### GET `/subscriptions/limit-keys`
List all limit keys (paginated).

**Auth:** JWT + ADMIN role

### GET `/subscriptions/limit-keys/{id}`
Get a limit key.

**Auth:** JWT + ADMIN role

### POST `/subscriptions/limit-keys`
Create a limit key.

**Auth:** JWT + ADMIN role

**Request Body:**
```json
{
  "code": "MONTHLY_OPERATIONS",
  "name": "Monthly API Operations",
  "description": "Maximum number of GHL API calls per calendar month"
}
```

### PUT `/subscriptions/limit-keys/{id}`
Update a limit key.

**Auth:** JWT + ADMIN role

### DELETE `/subscriptions/limit-keys/{id}`
Delete a limit key.

**Auth:** JWT + ADMIN role

---

## 28. Admin User Management

### POST `/admins`
Create a new admin account.

**Auth:** JWT + `CREATE_ADMIN` permission

**Request Body:** Same as user registration.

**Response `201 Created`:** New admin user object.

### PUT `/admins/{admin-id}/activate`
Activate a deactivated admin account.

**Auth:** JWT + `ACTIVATE_ADMIN` permission

### POST `/admins/{admin-id}/permissions`
Assign permissions to an admin.

**Auth:** JWT + `ASSIGN_PERMISSIONS` permission

**Request Body:**
```json
{
  "permission_ids": [1, 2, 3]
}
```

---

## 29. Error Responses

All errors follow this structure:

```json
{
  "timestamp": "2026-07-04T10:15:30Z",
  "status": 404,
  "error": "Not Found",
  "message": "Subscription plan not found with id: 99",
  "path": "/api/v1/subscriptions/plans/99"
}
```

**Common Status Codes:**

| Code | Meaning |
|---|---|
| `200 OK` | Success |
| `201 Created` | Resource created |
| `302 Found` | Redirect |
| `400 Bad Request` | Validation failure |
| `401 Unauthorized` | Missing or invalid JWT / App Key |
| `403 Forbidden` | Insufficient role or permission |
| `404 Not Found` | Resource not found |
| `409 Conflict` | Duplicate resource (e.g. already subscribed) |
| `429 Too Many Requests` | Monthly operation limit exceeded |
| `500 Internal Server Error` | Unexpected server error |

**Usage Enforcement (429):**
When a user's monthly API call count reaches their plan's `MONTHLY_OPERATIONS` limit, all GHL proxy calls return `429 Too Many Requests`:
```json
{
  "status": 429,
  "error": "Too Many Requests",
  "message": "Monthly operation limit of 5000 reached. Upgrade your plan to continue."
}
```

---

## 30. Enums Reference

### TenantSubscriptionStatus

| Value | Description |
|---|---|
| `DRAFT` | Subscription intent created, not yet paid |
| `CHECKOUT_STARTED` | User opened Paddle checkout |
| `TRIAL` | Active trial period |
| `ACTIVE` | Fully active and provisioned |
| `PROVISIONING_REQUIRED` | Payment received, awaiting GHL setup (BUNDLED) |
| `PROVISIONING_IN_PROGRESS` | Admin is setting up GHL (BUNDLED) |
| `PAST_DUE` | Payment failed, in grace window |
| `GRACE_PERIOD` | Extended grace — full access, payment overdue |
| `READ_ONLY` | Degraded access — can read but not write |
| `SUSPENDED` | No API access |
| `PAUSED` | Paddle subscription paused |
| `CANCELLED` | Cancelled by user or admin |
| `REFUND_REQUESTED` | User submitted refund request |
| `REFUNDED` | Refund approved and processed |
| `SYNC_ERROR` | Webhook processing error |
| `REVIEW_REQUIRED` | Flagged for manual admin review |

### ProvisioningStatus

| Value | Description |
|---|---|
| `PENDING` | Awaiting provisioning |
| `IN_PROGRESS` | Admin started provisioning |
| `PROVISIONED` | GHL subaccount ready |
| `FAILED` | Provisioning failed |

### BillingCycle

| Value | Description |
|---|---|
| `MONTHLY` | Billed every month |
| `QUARTERLY` | Billed every 3 months |
| `ANNUAL` | Billed every year |
| `LIFETIME` | One-time payment, no renewal |

### ProductType

| Value | Description |
|---|---|
| `STANDALONE` | User connects their own GHL account via OAuth |
| `BUNDLED` | Lofi Connect provisions a managed GHL account for the user |

### Trend Values (Usage Page)

| Value | Meaning |
|---|---|
| `"UP"` | Metric increased vs last month |
| `"DOWN"` | Metric decreased vs last month |
| `"UNCHANGED"` | No change |

---

## Pagination

Endpoints that return lists accept these query parameters:

| Param | Type | Default | Description |
|---|---|---|---|
| `page` | Int | `0` | Zero-based page number |
| `size` | Int | `20` | Items per page |
| `sortBy` | String | varies | Field to sort by |
| `sortDirection` | String | `ASC` | `ASC` or `DESC` |

**Paginated Response Shape:**
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "total_elements": 156,
  "total_pages": 8
}
```

---

## GHL Proxy — Request Headers

All endpoints under `/api/v1/ghl/**` require:

```
X-App-Key: lc_live_aBcDeFgHiJkLmNoPqRsTuVwXyZ
```

> Do **not** send a `Authorization: Bearer` JWT header for GHL proxy calls — those endpoints use App Key authentication only.

The backend resolves the GHL subaccount linked to the key, injects the GHL OAuth token, and forwards the request. Responses are passed through transparently from GHL.
