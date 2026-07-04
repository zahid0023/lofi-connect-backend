# LofiConnect Subscription & Billing — Complete Documentation

## Table of Contents

1. [System Overview](#1-system-overview)
2. [Architecture](#2-architecture)
3. [What Is Implemented & Why](#3-what-is-implemented--why)
4. [Subscription Lifecycle — State Machine](#4-subscription-lifecycle--state-machine)
5. [Paddle Integration — How It Works](#5-paddle-integration--how-it-works)
6. [Database Schema](#6-database-schema)
7. [API Reference — Complete Endpoint List](#7-api-reference--complete-endpoint-list)
8. [Environment Variables](#8-environment-variables)
9. [User Frontend Integration Guide](#9-user-frontend-integration-guide)
10. [Admin Frontend Integration Guide](#10-admin-frontend-integration-guide)

---

## 1. System Overview

LofiConnect uses **Paddle** as the merchant of record for all billing. Paddle handles:

- Payment processing (credit card, PayPal, etc.)
- Subscription billing and renewals
- Tax/VAT collection
- Refund processing (financial side)
- Cancellations (billing side)

LofiConnect's backend handles:

- Subscription plan definitions and limits
- Orchestrating Paddle checkout sessions
- Receiving and processing Paddle webhooks to mirror subscription state
- Provisioning API keys when payment is confirmed
- Access control enforcement (grace period, read-only, suspension)
- Refund request workflows (approval/rejection before Paddle processes the money)
- GHL OAuth connection (user-initiated, separate from provisioning)
- Admin dashboard with subscription KPIs

**Design principle:** Paddle is the source of truth for billing. The backend never directly cancels, charges, or
refunds — it only calls Paddle APIs and then waits for Paddle's webhooks to confirm state changes locally.

---

## 2. Architecture

```
User Browser
    │
    ├── User Frontend (Next.js / React)
    │       Talks to: Backend REST API (JWT auth)
    │
    └── Admin Frontend (Next.js / React)
            Talks to: Backend REST API (JWT auth + ADMIN role)

Backend (Spring Boot)
    │
    ├── Auth package         — JWT, users, roles, OTP reset
    ├── Subscription package — Plans, limits, tenant subscriptions, refunds, audit, scheduler
    ├── Payment package      — Paddle REST client, webhook processor, provisioning strategies
    └── GHL proxy layer      — Proxies GHL API calls using linked OAuth tokens

External Services
    ├── Paddle API           — Transactions, subscriptions, cancellations
    ├── Paddle Webhooks      — POST /api/v1/payments/webhooks/paddle (no auth, HMAC verified)
    └── GHL OAuth            — User connects API key to GHL account
```

### Key Design Decisions

| Decision                                                                             | Reason                                                                                              |
|--------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| Paddle IDs stored in `subscription_payment_details`, not in `tenant_subscriptions`   | Keeps domain model clean; allows switching payment providers later                                  |
| Webhook endpoint always returns HTTP 200                                             | Prevents Paddle from retrying on application errors                                                 |
| Idempotency via `payment_events.event_id`                                            | Paddle may deliver webhooks more than once; duplicates are silently skipped                         |
| `@Async` for emails and audit logs                                                   | Prevents email/DB slowness from blocking the HTTP response                                          |
| Scheduled jobs for grace/read-only/suspension                                        | These are time-based transitions not triggered by Paddle                                            |
| Both STANDALONE and BUNDLED plans auto-provision immediately                         | API keys are granted on payment confirmation; GHL connection is a separate user-initiated OAuth step |
| `return_url` and customer email passed per-transaction to Paddle                     | Guarantees redirect after checkout regardless of global Paddle Dashboard settings                   |
| Provisioning triggered in `transaction.completed`, not only `subscription.activated` | Guards against race condition where `subscription.activated` arrives before `transaction.completed` |
| Refund submit does NOT change subscription status                                    | User retains access while the refund request is under admin review                                  |
| `previousStatus` stored on refund request                                            | Rejection restores the exact status the subscription had before the request, not always CANCELLED   |
| Renewal `end_date` taken from Paddle's `billing_period.ends_at`                      | Prevents date drift from accumulating across many renewals vs. local 30/90/365-day calculation      |

---

## 3. What Is Implemented & Why

### 3.1 Limit Keys (`/api/v1/subscriptions/limit-keys`)

**What:** A `LimitKey` is a named, typed constraint that can be attached to a subscription plan. Examples: `APP_KEYS` (
how many API keys a user may generate), `CONTACTS`, `API_CALLS_MONTHLY`.

**Why:** Decouples limit definitions from plan definitions. Admins can define new limit dimensions without changing
code. Each plan specifies a value per limit key.

**How:** `LimitKeyEntity` stores the key name, data type (INTEGER/BOOLEAN/DECIMAL), category, and unit.
`SubscriptionPlanLimitEntity` joins a plan to a limit key with a specific value. `UsageEnforcementService` checks the
user's current usage against their plan's limit before allowing actions like generating API keys.

### 3.2 Subscription Plans (`/api/v1/subscriptions/plans`)

**What:** Plans define what users subscribe to. Each plan has a name, price, billing cycle (
MONTHLY/QUARTERLY/ANNUAL/LIFETIME), trial period, product type (STANDALONE/BUNDLED), and a Paddle Price ID.

**Why:** Plans must exist locally so the backend can enforce limits and provision correctly after Paddle confirms
payment.

**How:** When admin creates a plan via `POST /api/v1/subscriptions/plans`, the backend simultaneously calls Paddle's
Products API (`PaddleProductService.provisionPlan()`) to create a corresponding Paddle price, then stores the returned
Paddle Price ID (`paddle_price_id`) on the plan. This Price ID is what gets sent to Paddle at checkout time.

### 3.3 Checkout & Subscription Flow

**What:** A multi-step flow where the backend creates a Paddle-hosted checkout session and returns a URL for the user to
complete payment.

**Why:** Paddle's hosted checkout handles PCI compliance, payment methods, and tax calculation. We never handle raw card
data.

**How:** `PaymentServiceImpl.createCheckout()` calls Paddle `POST /transactions` with three critical fields beyond the
price: `custom_data` (embeds `user_id` and `plan_id` so webhooks can link back), `return_url` (the backend's success
redirect endpoint, sourced from `PADDLE_SUCCESS_URL`), and `customer.email` (pre-fills the user's email on the Paddle
checkout page). Both `return_url` and `customer.email` are passed per-transaction rather than relying solely on global
Paddle Dashboard settings, ensuring the redirect always works. See Section 5 for the full flow diagram.

### 3.4 Webhook Processing (`/api/v1/payments/webhooks/paddle`)

**What:** Paddle sends signed HTTP POST events when subscription state changes. The backend verifies the HMAC-SHA256
signature and updates local state.

**Why:** This is how the backend learns that payment succeeded, subscription was cancelled, payment failed, etc. All
local state changes for billing events are driven exclusively by webhooks.

**How:** `PaddleSignatureVerifier` verifies the signature. `PaddleWebhookProcessor` routes by event type, checks
idempotency, and updates `TenantSubscriptionEntity`. See Section 5.

### 3.5 Provisioning

**What:** After payment is confirmed, the user is provisioned — meaning they gain the ability to generate API keys up to
their plan's `APP_KEYS` limit.

**Why:** Access should only be granted after confirmed payment.

**How:** Two provisioning strategies exist (`StandaloneProvisioningStrategy` and `BundledProvisioningStrategy`), both
auto-provision immediately by setting `provisioning_status = PROVISIONED`.

Provisioning is triggered in two places to guard against webhook ordering race conditions:

1. **`transaction.completed`** — fires when payment is taken. If the local subscription was just created (or already
   exists with `provisioning_status = PENDING`), provisioning runs immediately here. This is the primary trigger for
   paid subscriptions.
2. **`subscription.activated`** — fires after Paddle activates the subscription. Provisioning runs here **only if
   `provisioning_status` is still `PENDING`**, acting as a safety net if `transaction.completed` arrived first and
   already provisioned.
3. **`subscription.created` (trial)** — trial subscriptions are provisioned immediately when created, so trial users
   can generate API keys right away without waiting for a payment event.

This ordering ensures provisioning happens exactly once regardless of which webhook arrives first.

### 3.6 API Keys & GHL OAuth

**What:** Each subscription grants a quota of API keys (defined by the `APP_KEYS` limit). Each API key can be connected
to one GHL account via OAuth.

**Why:** The API key is the source of truth for CRM connectivity. One key = one GHL account connection.

**How:**

1. User generates an API key: `POST /api/v1/auth/app-keys`
2. User initiates GHL OAuth: `GET /api/v1/authorization/ghl/init?app-key-id={id}`
3. Backend redirects to GHL OAuth consent screen
4. GHL redirects back to `GET /api/v1/authorization/redirect?code=...&state={appKeyId}`
5. Backend exchanges code for token, saves `GoHighLevelTokenEntity` linked to the API key
6. Frontend redirected to `/portal/connections`

### 3.7 Upgrade / Downgrade

**What:** User can switch their active subscription to a different plan.

**Why:** Users outgrow plans or need to downgrade.

**How:** `POST /api/v1/subscriptions/tenant-subscriptions/upgrade` → backend calls Paddle
`PATCH /subscriptions/{paddleSubId}` with the new price ID. Paddle fires `subscription.updated` webhook →
`PaddleWebhookProcessor` detects the plan change by comparing `items[0].price.id` to the current plan's`paddle_price_id`
and swaps the plan locally.

### 3.8 Cancellation

**What:** User cancels at end of billing period (retains access until period end).

**Why:** End-of-period cancellation is the standard SaaS model. Users should not lose access immediately.

**How:** `DELETE /api/v1/subscriptions/tenant-subscriptions/cancel` → backend calls Paddle
`POST /subscriptions/{paddleSubId}/cancel` with `effective_from: next_billing_period`. No local status change. When the
period ends, Paddle fires `subscription.cancelled` → backend sets status to `CANCELLED` and `is_active = false`.

### 3.9 Payment Failure Lifecycle (Scheduler)

**What:** When payment fails, Paddle fires `subscription.past_due`. The backend then runs a time-based state machine:

- Immediately: PAST_DUE → GRACE_PERIOD (scheduler, hourly)
- After 5 days: GRACE_PERIOD → READ_ONLY (scheduler, hourly)
- After 7 more days: READ_ONLY → SUSPENDED (scheduler, hourly)

**Why:** Gives users time to update payment info before losing access. Soft degradation is better UX than immediate
suspension.

**How:** `SubscriptionLifecycleScheduler` runs Spring `@Scheduled` jobs. Each transition writes an audit log entry and
sends a lifecycle email to the user.

### 3.10 Refund Requests

**What:** Users can submit a refund request with a reason. Admins review and approve or reject. Approved requests are
then manually processed in the Paddle dashboard by the admin.

**Why:** Paddle handles the financial refund, but business-level approval belongs to LofiConnect.

**How:** The lifecycle has three distinct phases:

1. **Submit** (`POST /api/v1/subscriptions/refund-requests`): Creates a `PENDING` refund request. The subscription
   status is **not changed** — the user retains full access while the request is under review. The subscription's
   current status is stored as `previousStatus` on the refund request entity so it can be restored if rejected.

2. **Approve** (`POST /api/v1/admin/refund-requests/{id}/approve`): Sets the subscription status to
   `REFUND_REQUESTED` and `is_active = false`, immediately revoking access. The admin then manually issues the refund
   in Paddle Dashboard. Paddle fires webhooks which update the local status to `REFUNDED`.

3. **Reject** (`POST /api/v1/admin/refund-requests/{id}/reject`): Restores the subscription to its `previousStatus`
   (whatever it was before the request was submitted — e.g., `ACTIVE`, `TRIAL`, `GRACE_PERIOD`). `is_active` is
   restored based on whether the restored status grants access.

### 3.11 Checkout Intent Tracking

**What:** When a user opens a Paddle checkout, a `CheckoutIntentEntity` is saved with a 48-hour expiry.

**Why:** Allows sending a recovery email after 24 hours if the user did not complete checkout, and expiring stale
intents.

**How:** Created in `PaymentServiceImpl.createCheckout()`. The scheduler sends a reminder at 24h (
`sendCheckoutReminders()`) and marks as EXPIRED at 48h (`expireCheckoutIntents()`). On successful
`subscription.activated`, the intent is marked `COMPLETED`.

### 3.12 Audit Log

**What:** Every significant subscription event is recorded in `subscription_audit_logs` with actor type (
USER/ADMIN/SYSTEM/PADDLE), event type, old value, new value, and Paddle event ID.

**Why:** Provides a full immutable audit trail for debugging, compliance, and support.

**How:** `AuditLogService` is called from webhook handlers, schedulers, and service methods. All writes are `@Async` so
they don't block request threads.

### 3.13 Admin Dashboard

**What:** Aggregate KPIs (MRR estimate, counts by status, new customers this month, pending refunds).

**Why:** Gives the business a real-time view of subscription health.

**How:** `AdminDashboardService.getStats()` aggregates from `TenantSubscriptionRepository` queries and
`RefundRequestRepository`.

---

## 4. Subscription Lifecycle — State Machine

```
[User calls POST /payments/checkout]
         │
  CheckoutIntentEntity created (PENDING, expires 48h)
         │
         │  [Paddle: transaction.completed — first payment]
         ▼
       ACTIVE ◄─────────────────────────────────────────────┐
         │   │                                               │
         │   │  [Paddle: subscription.past_due]              │
         │   │                                               │
         │   ▼                                               │
         │  PAST_DUE                                         │
         │       │ [Scheduler: hourly — startGracePeriod]    │
         │       ▼                                           │
         │  GRACE_PERIOD  ─────────────────── (5 days)       │
         │       │ [Scheduler: after 5 days — enforceReadOnly]
         │       ▼                                           │
         │  READ_ONLY ─────────────────────── (7 days)       │
         │       │ [Scheduler: after 7 days — enforceSuspension]
         │       ▼                                           │
         │  SUSPENDED                                        │
         │                                                   │
         │  [Paddle: subscription.resumed / payment recovered]
         │       └─────────────────────────────────────────►─┘
         │
         │  [Paddle: subscription.created — trialing status]
         ▼
       TRIAL ───────────────────────────────────────────────►─┐
                    [Paddle: subscription.activated]           │
                                                               ▼
                                                             ACTIVE

[User cancels]
  DELETE /tenant-subscriptions/cancel → Paddle cancel (next_billing_period)
  [At period end: Paddle: subscription.cancelled]
         │
       CANCELLED
         │ [end_date passes]
       EXPIRED

PAUSED    ←── [Paddle: subscription.paused]
ACTIVE    ←── [Paddle: subscription.resumed]

PROVISIONING_REQUIRED   ←── [BUNDLED plan paid; auto-provisioned immediately in current impl]
PROVISIONING_IN_PROGRESS ←── [Admin marks bundled provisioning as started]
(→ ACTIVE once admin marks complete)

REFUND_REQUESTED ←── [Admin approves refund; subscription access revoked]
REFUNDED         ←── [Admin processes in Paddle Dashboard; webhook fires]

SYNC_ERROR / REVIEW_REQUIRED ←── [Set manually by admin for data integrity issues]
```

> **Note on DRAFT / CHECKOUT_STARTED:** These statuses are defined in the enum for completeness but are not
> currently assigned to `TenantSubscriptionEntity` records. The checkout session is tracked instead via
> `CheckoutIntentEntity` (a separate table). The `TenantSubscription` is only created once Paddle confirms payment
> (`transaction.completed`) or a trial (`subscription.created`).

### Status Meanings

| Status                     | Access Level             | Trigger                                              |
|----------------------------|--------------------------|------------------------------------------------------|
| `DRAFT`                    | None (enum only)         | Defined for pre-checkout; not currently assigned     |
| `CHECKOUT_STARTED`         | None (enum only)         | Defined for in-progress checkout; not currently assigned |
| `TRIAL`                    | Full access              | Paddle `subscription.created` (trialing)             |
| `ACTIVE`                   | Full access              | Payment confirmed (`transaction.completed`) or renewal |
| `PROVISIONING_REQUIRED`    | Full access              | BUNDLED plan paid; awaiting admin GHL setup          |
| `PROVISIONING_IN_PROGRESS` | Full access              | Admin has started GHL subaccount setup               |
| `PAST_DUE`                 | Full access (briefly)    | Paddle `subscription.past_due`                       |
| `GRACE_PERIOD`             | Full access              | Scheduler — PAST_DUE → GRACE within 1 hour          |
| `READ_ONLY`                | Dashboard only, no API   | Scheduler — after 5 days in GRACE_PERIOD             |
| `SUSPENDED`                | No access                | Scheduler — after 7 days in READ_ONLY                |
| `PAUSED`                   | No access                | Paddle `subscription.paused`                         |
| `CANCELLED`                | Access until `end_date`  | Paddle `subscription.cancelled`                      |
| `EXPIRED`                  | No access                | After `end_date` passes (future scheduler)           |
| `REFUND_REQUESTED`         | No access (revoked)      | Admin approves refund request                        |
| `REFUNDED`                 | No access                | Paddle webhook after admin processes refund          |
| `SYNC_ERROR`               | No access                | Manual admin action — local/Paddle state mismatch    |
| `REVIEW_REQUIRED`          | No access                | Manual admin action — identity/payment mismatch      |

---

## 5. Paddle Integration — How It Works

### 5.1 Paddle Configuration (Paddle Dashboard)

Before going live, configure the following in your Paddle Dashboard:

1. **Return URL / Success URL:**
   Set to: `{BACKEND_URL}/api/v1/subscriptions/tenant-subscriptions/success`
   This is where Paddle redirects the browser after checkout. The backend then redirects to
   `{FRONTEND_URL}/subscription/success`.

2. **Webhook Endpoint:**
   Add `{BACKEND_URL}/api/v1/payments/webhooks/paddle` as a webhook destination.
   Enable these events:
    - `subscription.created`
    - `subscription.activated`
    - `subscription.updated`
    - `subscription.cancelled`
    - `subscription.past_due`
    - `subscription.paused`
    - `subscription.resumed`
    - `transaction.completed`

3. **Webhook Secret:**
   Copy the webhook secret from Paddle Dashboard → set as `PADDLE_WEBHOOK_SECRET` env var.

4. **API Key:**
   Copy from Paddle Dashboard → set as `PADDLE_API_KEY` env var.

### 5.2 New Subscription Flow (Detailed)

```
Frontend                    Backend                         Paddle
   │                           │                               │
   │──POST /payments/checkout──►│                               │
   │   { plan_id: 3 }          │──POST /transactions──────────►│
   │                           │   { items: [price_id],        │
   │                           │     custom_data: {user_id,    │
   │                           │     plan_id} }                │
   │                           │◄──{ checkout.url, id }────────│
   │◄──{ checkout_url,         │                               │
   │    transaction_id }       │                               │
   │                           │                               │
   │  (redirect user to checkout_url)                          │
   │──────────────────────────────────────────────────────────►│
   │                           │     [User pays on Paddle]     │
   │                           │◄──transaction.completed webhook
   │                           │   Creates TenantSubscription + │
   │                           │   SubscriptionPaymentDetails  │
   │                           │◄──subscription.activated webhook
   │                           │   Sets status=ACTIVE,         │
   │                           │   triggers provisioning       │
   │◄──(redirect to success URL)──────────────────────────────►│
   │                           │                               │
   │──GET /payments/status────►│                               │
   │◄──{ active: true, ... }───│                               │
```

### 5.3 Upgrade Flow (Detailed)

```
Frontend                    Backend                         Paddle
   │                           │                               │
   │──POST /subscriptions/     │                               │
   │   tenant-subscriptions/   │                               │
   │   upgrade                 │                               │
   │   { new_plan_id: 5 }     ►│                               │
   │                           │──PATCH /subscriptions/{id}───►│
   │                           │   { items: [new_price_id],    │
   │                           │     proration_billing_mode:   │
   │                           │     prorated_immediately }    │
   │                           │◄──200 OK──────────────────────│
   │◄──{ success: true }───────│                               │
   │                           │                               │
   │                           │◄──subscription.updated webhook│
   │                           │   Detects items[0].price.id   │
   │                           │   changed → updates local plan│
```

### 5.4 Cancellation Flow (Detailed)

```
Frontend                    Backend                         Paddle
   │                           │                               │
   │──DELETE /subscriptions/   │                               │
   │   tenant-subscriptions/   │                               │
   │   cancel─────────────────►│                               │
   │                           │──POST /subscriptions/{id}/────►
   │                           │   cancel                      │
   │                           │   { effective_from:           │
   │                           │     next_billing_period }     │
   │                           │◄──200 OK──────────────────────│
   │◄──{ success: true,        │                               │
   │    message: "scheduled" }─│                               │
   │                           │                               │
   │  [User retains full access until billing period ends]     │
   │                           │                               │
   │  [At period end]          │◄──subscription.cancelled──────│
   │                           │   Sets CANCELLED, is_active=false
```

### 5.5 Webhook Security

Every incoming Paddle webhook is verified using HMAC-SHA256:

- Paddle sends header `Paddle-Signature: ts=...;h1=...`
- Backend (`PaddleSignatureVerifier`) reconstructs the signed payload as `{ts}:{rawBody}` and compares HMAC-SHA256 with
  the stored `PADDLE_WEBHOOK_SECRET`
- Invalid signatures return HTTP 401 immediately
- Duplicate `event_id` values are silently skipped (idempotency)
- The endpoint always returns HTTP 200 for valid, verified events to prevent Paddle retries

---

## 6. Database Schema

### Key Tables

| Table                          | Purpose                                                                 |
|--------------------------------|-------------------------------------------------------------------------|
| `limit_keys`                   | Named limit dimensions (APP_KEYS, CONTACTS, etc.)                       |
| `subscription_plans`           | Plan definitions (price, billing cycle, Paddle price ID, limits)        |
| `subscription_plan_limits`     | Per-plan values for each limit key                                      |
| `tenant_subscriptions`         | One row per user subscription; holds status, dates, provisioning status |
| `subscription_payment_details` | Paddle-specific IDs (paddleSubscriptionId, paddleCustomerId)            |
| `payment_events`               | All received Paddle webhook payloads (for idempotency and audit)        |
| `checkout_intents`             | Open Paddle checkouts (for 24h reminder and 48h expiry)                 |
| `subscription_audit_logs`      | Immutable audit trail of all subscription lifecycle events              |
| `refund_requests`              | User refund requests and admin review outcomes                          |
| `lofi_connect_app_keys`        | API keys generated by users, linked to subscriptions                    |
| `go_high_level_tokens`         | GHL OAuth tokens linked to API keys                                     |

### `tenant_subscriptions` Key Columns

```sql
id                    BIGINT PK
user_id               BIGINT NOT NULL
subscription_plan_id  BIGINT FK → subscription_plans
status                VARCHAR(20) -- TenantSubscriptionStatus enum
provisioning_status   VARCHAR(20) -- PENDING | IN_PROGRESS | PROVISIONED | FAILED
start_date            TIMESTAMP
end_date              TIMESTAMP
trial_ends_at         TIMESTAMP
grace_period_starts_at TIMESTAMP  -- set when PAST_DUE → GRACE_PERIOD
read_only_starts_at   TIMESTAMP  -- set when GRACE_PERIOD → READ_ONLY
suspended_at          TIMESTAMP  -- set when READ_ONLY → SUSPENDED
cancelled_at          TIMESTAMP  -- set when CANCELLED
is_active             BOOLEAN
is_deleted            BOOLEAN
```

### `subscription_plans` Key Columns

```sql
id               BIGINT PK
code             VARCHAR(100) UNIQUE  -- e.g. "STARTER_MONTHLY"
name             VARCHAR(100)
price            DECIMAL(10,2)
billing_cycle    VARCHAR(20)  -- MONTHLY | QUARTERLY | ANNUAL | LIFETIME
trial_period_days INT DEFAULT 0
product_type     VARCHAR(20)  -- STANDALONE | BUNDLED
paddle_price_id  VARCHAR(100) -- e.g. "pri_01h..."  ← required for checkout
currency_id      BIGINT FK
is_public        BOOLEAN
is_active        BOOLEAN
is_deleted       BOOLEAN
```

---

## 7. API Reference — Complete Endpoint List

All endpoints use `Content-Type: application/json`. All authenticated endpoints require
`Authorization: Bearer {jwt_token}`.

JSON fields use **snake_case** throughout.

### Authentication

| Method | Path                           | Auth | Description                               |
|--------|--------------------------------|------|-------------------------------------------|
| POST   | `/api/v1/auth/login`           | None | Login, returns JWT access + refresh token |
| POST   | `/api/v1/auth/refresh`         | None | Refresh access token                      |
| POST   | `/api/v1/auth/forgot-password` | None | Send OTP to email                         |
| POST   | `/api/v1/auth/verify-otp`      | None | Verify OTP, get reset token               |
| POST   | `/api/v1/auth/reset-password`  | None | Reset password with reset token           |

### Limit Keys (Admin only)

| Method | Path                                    | Auth  | Description        |
|--------|-----------------------------------------|-------|--------------------|
| POST   | `/api/v1/subscriptions/limit-keys`      | ADMIN | Create a limit key |
| GET    | `/api/v1/subscriptions/limit-keys`      | ADMIN | Paginated list     |
| GET    | `/api/v1/subscriptions/limit-keys/{id}` | ADMIN | Get by ID          |
| PUT    | `/api/v1/subscriptions/limit-keys/{id}` | ADMIN | Update             |
| DELETE | `/api/v1/subscriptions/limit-keys/{id}` | ADMIN | Soft delete        |

### Subscription Plans

| Method | Path                                 | Auth  | Description                             |
|--------|--------------------------------------|-------|-----------------------------------------|
| GET    | `/api/v1/subscriptions/plans/public` | None  | All active public plans with limits     |
| GET    | `/api/v1/subscriptions/plans/{id}`   | None  | Single plan detail                      |
| GET    | `/api/v1/subscriptions/plans`        | ADMIN | Paginated list (all plans)              |
| POST   | `/api/v1/subscriptions/plans`        | ADMIN | Create plan (also creates Paddle price) |
| PUT    | `/api/v1/subscriptions/plans/{id}`   | ADMIN | Update plan                             |
| DELETE | `/api/v1/subscriptions/plans/{id}`   | ADMIN | Soft delete                             |

### Payments & Checkout

| Method | Path                               | Auth        | Description                                    |
|--------|------------------------------------|-------------|------------------------------------------------|
| POST   | `/api/v1/payments/checkout`        | JWT         | Create Paddle checkout, returns `checkout_url` |
| GET    | `/api/v1/payments/status`          | JWT         | Poll subscription + provisioning status        |
| POST   | `/api/v1/payments/webhooks/paddle` | None (HMAC) | Paddle webhook receiver                        |

### Tenant Subscriptions

| Method | Path                                                 | Auth  | Description                               |
|--------|------------------------------------------------------|-------|-------------------------------------------|
| GET    | `/api/v1/subscriptions/tenant-subscriptions/success` | None  | Paddle return URL — redirects to frontend |
| POST   | `/api/v1/subscriptions/tenant-subscriptions/upgrade` | JWT   | Upgrade/downgrade plan via Paddle         |
| GET    | `/api/v1/subscriptions/tenant-subscriptions/me`      | JWT   | Get current user's active subscription    |
| DELETE | `/api/v1/subscriptions/tenant-subscriptions/cancel`  | JWT   | Schedule cancellation at period end       |
| GET    | `/api/v1/subscriptions/tenant-subscriptions`         | ADMIN | Paginated list of all subscriptions       |

### API Keys

| Method | Path                    | Auth | Description                                     |
|--------|-------------------------|------|-------------------------------------------------|
| POST   | `/api/v1/auth/app-keys` | JWT  | Generate API key (requires active subscription) |
| GET    | `/api/v1/auth/app-keys` | JWT  | List user's API keys                            |

### GHL OAuth (CRM Connection)

| Method | Path                                                | Auth | Description                                             |
|--------|-----------------------------------------------------|------|---------------------------------------------------------|
| GET    | `/api/v1/authorization/ghl/init?app-key-id={id}`    | None | Redirect to GHL OAuth consent                           |
| GET    | `/api/v1/authorization/redirect?code=...&state=...` | None | GHL OAuth callback — saves token, redirects to frontend |

### Refund Requests

| Method | Path                                         | Auth  | Description                      |
|--------|----------------------------------------------|-------|----------------------------------|
| POST   | `/api/v1/subscriptions/refund-requests`      | JWT   | Submit refund request            |
| GET    | `/api/v1/subscriptions/refund-requests/me`   | JWT   | View my refund requests          |
| GET    | `/api/v1/admin/refund-requests`              | ADMIN | List all pending refund requests |
| POST   | `/api/v1/admin/refund-requests/{id}/approve` | ADMIN | Approve a refund request         |
| POST   | `/api/v1/admin/refund-requests/{id}/reject`  | ADMIN | Reject a refund request          |

### Admin Dashboard

| Method | Path                                                     | Auth  | Description                         |
|--------|----------------------------------------------------------|-------|-------------------------------------|
| GET    | `/api/v1/admin/dashboard/stats`                          | ADMIN | Aggregate KPIs                      |
| GET    | `/api/v1/admin/dashboard/provisioning-queue`             | ADMIN | Subscriptions awaiting provisioning |
| POST   | `/api/v1/admin/subscriptions/{id}/provisioning/start`    | ADMIN | Mark provisioning in progress       |
| POST   | `/api/v1/admin/subscriptions/{id}/provisioning/complete` | ADMIN | Mark provisioning complete          |

---

## 8. Environment Variables

```env
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/loficonnect
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=secret

# JWT
JWT_SECRET=your-256-bit-secret
JWT_ACCESS_EXPIRATION_MINUTES=60
JWT_REFRESH_EXPIRATION_DAYS=30
OTP_EXPIRATION_MINUTES=10

# Email (SMTP)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USER_NAME=no-reply@yourapp.com
MAIL_PASSWORD=app-password
MAIL_SENDER_NAME=LofiConnect

# Frontend / Backend URLs
FRONT_END_URL=https://app.yourapp.com
BACKEND_URL=https://api.yourapp.com

# Paddle
PADDLE_API_KEY=live_...
PADDLE_WEBHOOK_SECRET=pdl_ntfset_...
PADDLE_API_BASE_URL=https://api.paddle.com        # omit for production default
PADDLE_SUCCESS_URL=https://api.yourapp.com/api/v1/subscriptions/tenant-subscriptions/success

# GHL
GHL_CLIENT_ID=...
GHL_CLIENT_SECRET=...
GHL_BASE_URL=https://services.leadconnectorhq.com
GHL_CODE_URL=https://marketplace.gohighlevel.com/oauth/chooselocation
GHL_REDIRECT_URI=https://api.yourapp.com/api/v1/authorization/redirect
GHL_TOKEN_URL=https://services.leadconnectorhq.com/oauth/token
```

---

## 9. User Frontend Integration Guide

This section describes every screen and API call the **user-facing frontend** needs to implement.

### 9.1 Authentication

Before any subscription action, the user must be logged in and have a valid JWT.

```
POST /api/v1/auth/login
Body: { "username": "user@example.com", "password": "..." }

Response:
{
  "access_token": "eyJ...",
  "refresh_token": "...",
  "expires_in": 3600
}
```

Store `access_token` in memory (not localStorage for security). Use `refresh_token` to obtain new access tokens before
expiry.

All subsequent requests include:

```
Authorization: Bearer {access_token}
```

---

### 9.2 Plan Selection Page

**Screen:** Show all available plans with pricing and features.

**API call:**

```
GET /api/v1/subscriptions/plans/public
```

**Response shape:**

```json
[
  {
    "id": 1,
    "code": "STARTER_MONTHLY",
    "name": "Starter",
    "price": "29.00",
    "billing_cycle": "MONTHLY",
    "trial_period_days": 14,
    "product_type": "STANDALONE",
    "description": [
      "Up to 3 API keys",
      "GHL integration",
      "Email support"
    ],
    "limits": [
      {
        "limit_key_code": "APP_KEYS",
        "value": "3"
      },
      {
        "limit_key_code": "CONTACTS",
        "value": "1000"
      }
    ],
    "is_public": true
  }
]
```

**UI tips:**

- Display `billing_cycle` as "Monthly / Quarterly / Annual / Lifetime"
- Show `trial_period_days` prominently if > 0 (e.g. "Start 14-day free trial")
- Highlight recommended plan with a badge
- Do not show `paddle_price_id` to the user

---

### 9.3 Subscribe Flow

**Step 1 — Initiate checkout (user clicks "Subscribe" or "Start Trial")**

```
POST /api/v1/payments/checkout
Authorization: Bearer {token}
Body: { "plan_id": 1 }

Response:
{
  "checkout_url": "https://checkout.paddle.com/checkout/...",
  "transaction_id": "txn_01h..."
}
```

**Step 2 — Redirect to Paddle Checkout**

Option A (full redirect — simplest):

```javascript
window.location.href = response.checkout_url;
```

Option B (Paddle.js overlay — keeps user on your page):

```html

<script src="https://cdn.paddle.com/paddle/v2/paddle.js"></script>
<script>
    Paddle.Environment.set("production"); // or "sandbox"
    Paddle.Initialize({token: "live_..."}); // your Paddle client-side token
    Paddle.Checkout.open({transactionId: response.transaction_id});
</script>
```

**Step 3 — Paddle redirects back**

After payment, Paddle redirects the browser to your configured success URL. The backend redirects to:

```
{FRONT_END_URL}/subscription/success
```

**Step 4 — Poll until active (on the success page)**

Poll every 2 seconds until `active: true` (max 30 seconds, then show "still processing" message):

```
GET /api/v1/payments/status
Authorization: Bearer {token}

Response:
{
  "subscription_status": "ACTIVE",
  "provisioning_status": "PROVISIONED",
  "active": true
}
```

```javascript
async function pollUntilActive(maxAttempts = 15) {
    for (let i = 0; i < maxAttempts; i++) {
        await sleep(2000);
        const res = await fetch('/api/v1/payments/status', {headers: authHeaders});
        const data = await res.json();
        if (data.active) {
            router.push('/dashboard'); // redirect to dashboard
            return;
        }
    }
    // Show: "Your payment is processing. You'll receive an email when ready."
}
```

**Possible `subscription_status` values on the success page:**

| Status   | What to show                            |
|----------|-----------------------------------------|
| `ACTIVE` | "Welcome! Your subscription is active." |
| `TRIAL`  | "Your free trial has started!"          |
| `null`   | "Processing... please wait."            |

---

### 9.4 My Subscription Page

**API call:**

```
GET /api/v1/subscriptions/tenant-subscriptions/me
Authorization: Bearer {token}

Response:
{
  "data": {
    "id": 42,
    "user_id": 7,
    "plan_id": 1,
    "plan_code": "STARTER_MONTHLY",
    "plan_name": "Starter",
    "billing_cycle": "MONTHLY",
    "price": "29.00",
    "status": "ACTIVE",
    "start_date": "2026-06-01T00:00:00Z",
    "end_date": "2026-07-01T00:00:00Z",
    "trial_ends_at": null
  }
}
```

**What to show based on `status`:**

| Status                     | Banner / Message                                                                      |
|----------------------------|---------------------------------------------------------------------------------------|
| `ACTIVE`                   | "Active" badge (green) — show renewal date from `end_date`                            |
| `TRIAL`                    | "Trial" badge (blue) — show "Trial ends on {trial_ends_at}"                           |
| `PROVISIONING_REQUIRED`    | Info banner: "Setting up your account. API keys will be available shortly."           |
| `PROVISIONING_IN_PROGRESS` | Info banner: "Account setup in progress. You will receive an email when ready."       |
| `PAST_DUE`                 | Warning banner: "Payment is past due. Please update your payment method."             |
| `GRACE_PERIOD`             | Warning banner: "Payment failed. Update your payment method to avoid losing access."  |
| `READ_ONLY`                | Error banner: "Access limited to dashboard. Update payment to restore full access."   |
| `SUSPENDED`                | Error banner: "Account suspended. Contact support to reactivate."                     |
| `PAUSED`                   | Info banner: "Subscription paused. Resume it in the Paddle customer portal."          |
| `CANCELLED`                | Info banner: "Cancelled. Access continues until {end_date}."                          |
| `REFUND_REQUESTED`         | Info banner: "Refund under review. Access is suspended pending outcome."              |
| `REFUNDED`                 | Info banner: "Refund processed. Your subscription has ended."                         |

**Handle 404 / `NoActiveSubscriptionException`:** If the user has no subscription, redirect to the plan selection page.

---

### 9.5 Upgrade / Downgrade Plan

**Screen:** Show available plans alongside current plan. Highlight current. Allow selecting a different plan.

**API call:**

```
POST /api/v1/subscriptions/tenant-subscriptions/upgrade
Authorization: Bearer {token}
Body: { "new_plan_id": 3 }

Response:
{ "success": true, "id": 42 }
```

**Important:** The plan change is processed by Paddle asynchronously. The local subscription's plan is updated via the
`subscription.updated` webhook, which may take a few seconds.

**UX recommendation:** After a successful response, refresh the subscription data after 3–5 seconds, or show a banner: "
Plan change requested. Your plan will update shortly."

**Errors:**

- `400 User is already subscribed to this plan.` — disable the button for the current plan
- `400 Plan has no Paddle price configured.` — do not show that plan as an upgrade option
- `404 No active subscription` — redirect to subscribe flow

---

### 9.6 Cancel Subscription

**Screen:** "Cancel Subscription" button with a confirmation modal.

**Modal copy:** "Your subscription will be cancelled at the end of your current billing period on {end_date}. You'll
keep full access until then."

**API call:**

```
DELETE /api/v1/subscriptions/tenant-subscriptions/cancel
Authorization: Bearer {token}

Response:
{
  "success": true,
  "message": "Cancellation scheduled at end of billing period. You will retain access until then."
}
```

After success, refresh the subscription info (status may not change immediately — status only changes to `CANCELLED`when
the billing period ends and Paddle fires `subscription.cancelled` webhook).

---

### 9.7 Generate API Keys

**Prerequisite:** User must have an `ACTIVE`, `TRIAL`, or `GRACE_PERIOD` subscription with
`provisioning_status = PROVISIONED`.

**Screen:** "API Keys" section showing existing keys and a "Generate New Key" button.

**Generate key:**

```
POST /api/v1/auth/app-keys
Authorization: Bearer {token}
Body: { "name": "My GHL Connection" }

Response:
{
  "data": {
    "id": 12,
    "app_key": "lc_live_...",
    "name": "My GHL Connection",
    "created_at": "2026-07-03T10:00:00Z"
  }
}
```

**List keys:**

```
GET /api/v1/auth/app-keys
Authorization: Bearer {token}

Response:
{
  "data": [
    { "id": 12, "app_key": "lc_live_...", "name": "My GHL Connection", "ghl_connected": false }
  ]
}
```

**Errors:**

- `403 Usage limit exceeded` — user has hit their `APP_KEYS` plan limit. Show: "You've reached your API key limit.
  Upgrade your plan to generate more."
- `403 No active subscription` — redirect to subscribe flow.

---

### 9.8 Connect API Key to GHL (OAuth)

**Screen:** For each API key, show "Connect to GHL" button if not yet connected.

**Step 1 — Initiate OAuth**

```
GET /api/v1/authorization/ghl/init?app-key-id={appKeyId}
```

This endpoint returns a 302 redirect to GHL's OAuth consent screen. Open in the same window:

```javascript
window.location.href = `/api/v1/authorization/ghl/init?app-key-id=${appKeyId}`;
```

**Step 2 — User consents on GHL**

GHL redirects back to the backend's `/api/v1/authorization/redirect?code=...&state={appKeyId}`. The backend saves the
GHL OAuth token linked to the API key, then redirects the browser to:

```
{FRONTEND_URL}/portal/connections
```

**Step 3 — Show connection status**

On `/portal/connections`, display all API keys with their GHL connection status.

---

### 9.9 Refund Request

**Screen:** "Request Refund" button on the subscription page or order history.

**Submit:**

```
POST /api/v1/subscriptions/refund-requests
Authorization: Bearer {token}
Body: { "reason": "The service did not meet my expectations." }

Response:
{
  "id": 5,
  "tenant_subscription_id": 42,
  "user_id": 7,
  "reason": "The service did not meet my expectations.",
  "status": "PENDING",
  "admin_notes": null,
  "reviewed_by": null,
  "reviewed_at": null,
  "created_at": "2026-07-03T10:00:00Z"
}
```

**View my requests:**

```
GET /api/v1/subscriptions/refund-requests/me
Authorization: Bearer {token}

Response: [ { ...RefundRequestResponse }, ... ]
```

**Status values:**

| Status     | What to show                                                    |
|------------|-----------------------------------------------------------------|
| `PENDING`  | "Under review"                                                  |
| `APPROVED` | "Approved — refund will be processed within 5–10 business days" |
| `REJECTED` | "Not approved" + show `admin_notes` if present                  |

---

### 9.10 Status Banners & Access Guards

Add a global subscription status check on app load. Based on `GET /api/v1/payments/status` or
`GET /api/v1/subscriptions/tenant-subscriptions/me`:

```javascript
function getAccessLevel(status) {
    switch (status) {
        case 'ACTIVE':
        case 'TRIAL':
        case 'GRACE_PERIOD':             // still full access during grace window
        case 'PROVISIONING_REQUIRED':    // paid; GHL setup pending — API keys accessible
        case 'PROVISIONING_IN_PROGRESS': // GHL setup underway — API keys accessible
            return 'FULL';
        case 'READ_ONLY':
            return 'READ_ONLY';  // show dashboard, block API key generation & GHL actions
        case 'SUSPENDED':
        case 'CANCELLED':
        case 'EXPIRED':
        case 'PAUSED':
        case 'REFUND_REQUESTED':
        case 'REFUNDED':
        case 'SYNC_ERROR':
        case 'REVIEW_REQUIRED':
            return 'NONE';       // redirect to subscribe or show "no access" screen
        case null:
        case undefined:
            return 'NO_SUBSCRIPTION';  // redirect to plans page
        default:
            return 'NONE';
    }
}
```

---

## 10. Admin Frontend Integration Guide

This section describes every screen and API call the **admin-facing frontend** needs to implement.

### 10.1 Admin Authentication

The admin must log in as a user with the `ADMIN` role. The JWT is identical in structure. All admin endpoints check
`hasRole('ADMIN')` server-side — if the JWT doesn't have the ADMIN role, the server returns `403 Forbidden`.

---

### 10.2 Admin Dashboard — KPIs Page

**Screen:** Main dashboard showing key metrics.

**API call:**

```
GET /api/v1/admin/dashboard/stats
Authorization: Bearer {admin_token}

Response:
{
  "estimated_mrr": "4350.00",
  "active_subscriptions": 147,
  "trialing_subscriptions": 23,
  "past_due_subscriptions": 4,
  "grace_period_subscriptions": 3,
  "read_only_subscriptions": 1,
  "suspended_subscriptions": 0,
  "cancelled_this_month": 8,
  "new_customers_this_month": 31,
  "standalone_active": 130,
  "bundled_active": 17,
  "pending_provisioning": 0,
  "pending_refund_requests": 2
}
```

**Widget layout suggestions:**

| Widget            | Field                                                 | Color            |
|-------------------|-------------------------------------------------------|------------------|
| MRR               | `estimated_mrr`                                       | Green            |
| Active            | `active_subscriptions`                                | Blue             |
| Trial             | `trialing_subscriptions`                              | Cyan             |
| At Risk           | `past_due_subscriptions + grace_period_subscriptions` | Yellow           |
| Limited           | `read_only_subscriptions + suspended_subscriptions`   | Orange           |
| Cancelled (month) | `cancelled_this_month`                                | Red              |
| New (month)       | `new_customers_this_month`                            | Green            |
| Pending Refunds   | `pending_refund_requests`                             | Red badge if > 0 |

**Auto-refresh:** Poll every 60 seconds or add a manual refresh button.

---

### 10.3 Admin Dashboard — Provisioning Queue

Note: With the current implementation both STANDALONE and BUNDLED plans auto-provision. This queue will typically be
empty. It exists for edge cases or future manual provisioning scenarios.

**API call:**

```
GET /api/v1/admin/dashboard/provisioning-queue
Authorization: Bearer {admin_token}

Response:
[
  {
    "subscription_id": 55,
    "user_id": 12,
    "plan_id": 2,
    "plan_name": "Business Bundle",
    "plan_code": "BUSINESS_BUNDLED",
    "subscription_status": "ACTIVE",
    "provisioning_status": "PENDING",
    "start_date": "2026-07-01T00:00:00Z",
    "created_at": "2026-07-01T00:05:00Z"
  }
]
```

**Mark as in progress:**

```
POST /api/v1/admin/subscriptions/{id}/provisioning/start
Authorization: Bearer {admin_token}
Body: (empty)

Response: { "success": true, "id": 55 }
```

**Mark as complete:**

```
POST /api/v1/admin/subscriptions/{id}/provisioning/complete
Authorization: Bearer {admin_token}
Body: (empty)

Response: { "success": true, "id": 55 }
```

---

### 10.4 Manage Subscription Plans

**Screen:** Table of all plans (including inactive/deleted). Create, update, deactivate.

**List all plans (paginated):**

```
GET /api/v1/subscriptions/plans?page=0&size=20&sort_by=sort_order&sort_dir=asc
Authorization: Bearer {admin_token}
```

Query parameters: `page` (0-based), `size`, `sort_by`, `sort_dir` (asc/desc).

**Create plan:**

```
POST /api/v1/subscriptions/plans
Authorization: Bearer {admin_token}
Body:
{
  "code": "PRO_MONTHLY",
  "name": "Pro",
  "currency_id": 1,
  "price": "79.00",
  "billing_cycle": "MONTHLY",
  "trial_period_days": 14,
  "sort_order": 2,
  "is_public": true,
  "product_type": "STANDALONE",
  "description": ["Up to 10 API keys", "Priority support", "Advanced analytics"],
  "limits": [
    { "limit_key_id": 1, "value": "10" },
    { "limit_key_id": 2, "value": "10000" }
  ]
}
```

This simultaneously creates a Paddle price via Paddle's API. The returned plan will include the `paddle_price_id`.

**Update plan:**

```
PUT /api/v1/subscriptions/plans/{id}
Authorization: Bearer {admin_token}
Body: { "name": "Pro Plus", "price": "89.00", ... }
```

Note: Changing `price` here does not update the Paddle price. To change pricing in Paddle, create a new plan with the
new price, then migrate users.

**Delete (soft):**

```
DELETE /api/v1/subscriptions/plans/{id}
Authorization: Bearer {admin_token}
```

Sets `is_deleted = true` and `is_active = false`. The plan no longer appears in public listing or can be subscribed to.
Existing subscribers are not affected.

---

### 10.5 Manage Limit Keys

**Screen:** Table of all limit keys. CRUD.

**List:**

```
GET /api/v1/subscriptions/limit-keys?page=0&size=20
Authorization: Bearer {admin_token}
```

**Create:**

```
POST /api/v1/subscriptions/limit-keys
Authorization: Bearer {admin_token}
Body:
{
  "code": "APP_KEYS",
  "name": "API Keys",
  "description": "Number of API keys the user can generate",
  "data_type": "INTEGER",
  "category": "CONNECTIVITY",
  "unit": "keys"
}
```

**Update / Delete:**

```
PUT  /api/v1/subscriptions/limit-keys/{id}
DELETE /api/v1/subscriptions/limit-keys/{id}
```

---

### 10.6 All Tenant Subscriptions

**Screen:** Table of all active subscriptions (paginated, filterable).

```
GET /api/v1/subscriptions/tenant-subscriptions?page=0&size=20&sort_by=created_at&sort_dir=desc
Authorization: Bearer {admin_token}

Response:
{
  "content": [
    {
      "subscription_id": 42,
      "user_id": 7,
      "plan_name": "Starter",
      "status": "ACTIVE",
      "start_date": "2026-06-01T00:00:00Z",
      "end_date": "2026-07-01T00:00:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "total_elements": 147,
  "total_pages": 8
}
```

**Allowed sort fields:** `id`, `userId`, `status`, `startDate`, `endDate`, `createdAt`.

---

### 10.7 Admin Override — Plan Change (Bypass Paddle)

For support scenarios (e.g., comping a plan, fixing sync errors), admin can change a user's plan directly without going
through Paddle:

This is an internal service method (`adminOverridePlan`) — it currently has no dedicated controller endpoint exposed. To
use it, you would need to either:

- Call it from a support tool / admin script
- Expose it via a new admin endpoint (e.g., `POST /api/v1/admin/subscriptions/{userId}/override-plan`)

This is intentionally not exposed by default to prevent accidental bypassing of Paddle billing.

---

### 10.8 Refund Requests — Admin Review

**Screen:** Table of pending refund requests with approve/reject actions.

**List pending:**

```
GET /api/v1/admin/refund-requests
Authorization: Bearer {admin_token}

Response:
[
  {
    "id": 5,
    "tenant_subscription_id": 42,
    "user_id": 7,
    "reason": "Service did not meet expectations.",
    "status": "PENDING",
    "admin_notes": null,
    "reviewed_by": null,
    "reviewed_at": null,
    "created_at": "2026-07-03T10:00:00Z"
  }
]
```

**Approve:**

```
POST /api/v1/admin/refund-requests/{id}/approve
Authorization: Bearer {admin_token}
Body: { "admin_notes": "Approved per policy." }

Response: { ...RefundRequestResponse with status: "APPROVED" }
```

**Reject:**

```
POST /api/v1/admin/refund-requests/{id}/reject
Authorization: Bearer {admin_token}
Body: { "admin_notes": "Outside refund window." }

Response: { ...RefundRequestResponse with status: "REJECTED" }
```

**After approving:**

1. The local refund request is marked `APPROVED`
2. The tenant subscription status changes to `REFUND_REQUESTED`
3. Admin must **manually go to Paddle Dashboard** → find the customer → issue the refund
4. Paddle fires a webhook which updates local subscription status to `REFUNDED`

Show a reminder in the UI: "Refund approved. Please process the payment in the Paddle Dashboard and then manually
confirm here or wait for the webhook."

---

### 10.9 Error Handling — Common HTTP Responses

All error responses follow this shape:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "User already has an active subscription. Cancel it before subscribing to a new plan.",
  "timestamp": "2026-07-03T10:00:00Z"
}
```

| HTTP Status | Scenario                                             | Frontend Action                                        |
|-------------|------------------------------------------------------|--------------------------------------------------------|
| `400`       | Validation error, business rule violation            | Show `message` to user                                 |
| `401`       | JWT expired or missing                               | Redirect to login                                      |
| `403`       | Insufficient role (non-admin hitting admin endpoint) | Show "Access denied"                                   |
| `404`       | Entity not found (e.g., no active subscription)      | Handle gracefully (redirect or show empty state)       |
| `500`       | Server error                                         | Show generic "Something went wrong. Please try again." |

**Specific 400 messages to handle:**

| Message                                   | Action                             |
|-------------------------------------------|------------------------------------|
| `User already has an active subscription` | Redirect to "My Subscription" page |
| `Usage limit exceeded for APP_KEYS`       | Show upgrade prompt                |
| `An active subscription is required`      | Redirect to plans page             |
| `User is already subscribed to this plan` | Disable button for current plan    |

---

### 10.10 Admin — Subscription Status Manual Override

For support edge cases, the admin can directly cancel a subscription (without going through Paddle):

This is an internal method (`adminCancelSubscription`) available in `TenantSubscriptionService`. Like
`adminOverridePlan`, it is not currently exposed via a REST endpoint to prevent misuse. To expose it:

```java
// Add to TenantSubscriptionController or a dedicated AdminSubscriptionController:
@PostMapping("/{userId}/cancel")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> adminCancel(@PathVariable Long userId) {
    return ResponseEntity.ok(tenantSubscriptionService.adminCancelSubscription(userId));
}
```

---

### 10.11 Recommended Admin UI Screen Map

```
Admin Frontend
├── /admin/dashboard           → KPI Stats + at-risk counts
├── /admin/plans               → List, create, edit, delete subscription plans
├── /admin/plans/{id}          → Plan detail with limits
├── /admin/limit-keys          → List, create, edit, delete limit keys
├── /admin/subscriptions       → All tenant subscriptions (paginated table)
├── /admin/subscriptions/{id}  → Subscription detail (history, provisioning status)
├── /admin/provisioning        → Provisioning queue (PENDING items)
├── /admin/refunds             → Pending refund requests
└── /admin/refunds/{id}        → Refund detail + approve/reject
```

---

### 10.12 Recommended User UI Screen Map

```
User Frontend
├── /plans                          → Plan selection (public, no auth)
├── /subscription/success           → Post-checkout polling page
├── /dashboard                      → Main app dashboard
├── /settings/subscription          → My subscription (status, plan, dates, upgrade/cancel)
├── /settings/subscription/upgrade  → Plan comparison + upgrade selection
├── /settings/api-keys              → List API keys, generate new, connect to GHL
├── /portal/connections             → GHL connection status per API key
└── /settings/billing/refunds       → My refund requests
```

---

## Appendix: Scheduler Summary

| Job                     | Runs Every | Trigger                                        | Action                                     |
|-------------------------|------------|------------------------------------------------|--------------------------------------------|
| `startGracePeriod`      | 1 hour     | PAST_DUE subscriptions with no grace start     | → GRACE_PERIOD, send email                 |
| `enforceReadOnly`       | 1 hour     | GRACE_PERIOD > 5 days                          | → READ_ONLY, send email                    |
| `enforceSuspension`     | 1 hour     | READ_ONLY > 7 days                             | → SUSPENDED, `is_active=false`, send email |
| `sendCheckoutReminders` | 30 min     | PENDING checkout intent > 24h with no reminder | Send reminder email                        |
| `expireCheckoutIntents` | 30 min     | PENDING checkout intent past `expires_at`      | → EXPIRED                                  |

---

## Appendix: Paddle Events Handled

| Paddle Event                        | Local Action                                                           |
|-------------------------------------|------------------------------------------------------------------------|
| `transaction.completed` (new)       | Create `TenantSubscriptionEntity` + `SubscriptionPaymentDetailsEntity` |
| `transaction.completed` (renewal)   | Set `end_date` from Paddle's `billing_period.ends_at`; falls back to local cycle calculation if not provided |
| `subscription.created` (trial only) | Create TRIAL subscription                                              |
| `subscription.activated`            | Set ACTIVE, set `end_date`, trigger provisioning                       |
| `subscription.updated`              | Detect plan change, sync `end_date`                                    |
| `subscription.cancelled`            | Set CANCELLED, `is_active=false`, trigger deprovisioning               |
| `subscription.past_due`             | Set PAST_DUE                                                           |
| `subscription.paused`               | Set PAUSED, `is_active=false`                                          |
| `subscription.resumed`              | Set ACTIVE, `is_active=true`, update `end_date`                        |

---

## Appendix: Audit Event Types (`AuditEventType`)

All subscription audit log entries use one of the following event types:

| Event Type                      | Actor          | When it fires                                                   |
|---------------------------------|----------------|-----------------------------------------------------------------|
| `CHECKOUT_STARTED`              | USER           | User calls `POST /payments/checkout`                            |
| `CHECKOUT_EXPIRED`              | SYSTEM         | Checkout intent passes its 48h expiry                           |
| `CHECKOUT_REMINDER_SENT`        | SYSTEM         | Reminder email sent at 24h for pending checkout                 |
| `PADDLE_WEBHOOK_RECEIVED`       | PADDLE         | Any Paddle webhook event arrives and is verified                |
| `WEBHOOK_VERIFICATION_FAILED`   | PADDLE         | Incoming webhook has invalid HMAC signature                     |
| `DUPLICATE_WEBHOOK_IGNORED`     | PADDLE         | Webhook `event_id` already processed (idempotency skip)         |
| `SUBSCRIPTION_CREATED`          | SYSTEM/PADDLE  | New `TenantSubscriptionEntity` created from webhook             |
| `SUBSCRIPTION_STATUS_CHANGED`   | SYSTEM/PADDLE  | Any status transition on a subscription                         |
| `PLAN_CHANGED`                  | PADDLE         | `subscription.updated` detected new `items[0].price.id`         |
| `ACCESS_GRANTED`                | SYSTEM         | Provisioning completes; user can now generate API keys          |
| `ACCESS_REVOKED`                | SYSTEM         | Deprovisioning triggered (e.g., cancellation)                   |
| `API_KEY_GENERATED`             | USER           | User generates a new API key                                    |
| `API_KEY_REVOKED`               | USER/ADMIN     | API key is deleted                                              |
| `BUNDLED_PROVISIONING_STARTED`  | ADMIN          | Admin marks bundled subscription as `IN_PROGRESS`               |
| `BUNDLED_PROVISIONING_COMPLETED`| ADMIN          | Admin marks bundled subscription as `PROVISIONED` + `ACTIVE`    |
| `ADMIN_CHANGED_SUBSCRIPTION`    | ADMIN          | Admin overrides plan or status manually                         |
| `CANCELLATION_REQUESTED`        | USER           | User calls `DELETE /tenant-subscriptions/cancel`                |
| `GRACE_PERIOD_STARTED`          | SYSTEM         | Scheduler: `PAST_DUE` → `GRACE_PERIOD`                          |
| `READ_ONLY_STARTED`             | SYSTEM         | Scheduler: `GRACE_PERIOD` → `READ_ONLY` after 5 days            |
| `SUBSCRIPTION_SUSPENDED`        | SYSTEM         | Scheduler: `READ_ONLY` → `SUSPENDED` after 7 days               |
| `REFUND_REQUESTED`              | USER           | User submits a refund request                                   |
| `REFUND_APPROVED`               | ADMIN          | Admin approves the refund request                               |
| `REFUND_REJECTED`               | ADMIN          | Admin rejects the refund request                                |
| `SYNC_ERROR`                    | SYSTEM/ADMIN   | State mismatch detected between local and Paddle                |
