# Payment Integration — Paddle

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Package Structure](#package-structure)
4. [End-to-End Checkout Flow](#end-to-end-checkout-flow)
5. [Plan Creation Flow](#plan-creation-flow)
6. [API Endpoints](#api-endpoints)
7. [Webhook Event Handling](#webhook-event-handling)
8. [Signature Verification](#signature-verification)
9. [Provisioning Strategies](#provisioning-strategies)
10. [GHL Access Enforcement](#ghl-access-enforcement)
11. [Data Model](#data-model)
12. [Configuration](#configuration)
13. [Error Handling](#error-handling)
14. [Sandbox Testing](#sandbox-testing)
15. [Adding a New Provisioning Type](#adding-a-new-provisioning-type)

---

## Overview

The `payment` package integrates **Paddle** as the billing provider for lofi-connect subscriptions.

**Core design decisions:**

- **Subscription creation is payment-driven** — the local `TenantSubscription` record is created only when Paddle fires
  `transaction.completed`, which means the payment was actually collected. The `subscription.created` event is only used
  for **trial** subscriptions (no upfront payment). The checkout success redirect is treated as advisory only; it simply
  redirects the browser to the frontend.
- **Paddle IDs are isolated** — `TenantSubscriptionEntity` knows nothing about Paddle. All Paddle-specific data (
  `paddleSubscriptionId`, `paddleCustomerId`) lives in a separate `SubscriptionPaymentDetailsEntity` table. This keeps
  the domain model provider-agnostic.
- **Plan creation syncs to Paddle automatically** — when an admin creates a subscription plan via the API, the backend
  calls Paddle to create the corresponding product and price, and stores the returned `paddle_price_id`. No manual
  Paddle Dashboard setup is needed.
- **Idempotent webhook processing** — every incoming event is checked against `payment_events` by `event_id` before
  processing. Duplicate deliveries are silently skipped.
- **Webhook always returns 200** — even on errors, to prevent Paddle from retrying events that have already been
  processed or rejected for a valid business reason.
- **`ProvisioningStrategy` abstracts access grant** — STANDALONE plans provision access immediately; BUNDLED plans flag
  for manual ops action.

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                          FRONTEND                               │
│                                                                 │
│  1. GET /plans/public  →  select plan                          │
│  2. POST /payments/checkout  →  receive checkout_url           │
│  3. Open checkout_url in browser                               │
│  4. GET /payments/status  (poll until active=true)             │
└────────────────┬────────────────────────────────────────────────┘
                 │ HTTPS
┌────────────────▼────────────────────────────────────────────────┐
│                          BACKEND                                │
│                                                                 │
│  PaymentController                                              │
│    POST /api/v1/payments/checkout                               │
│      → calls Paddle POST /transactions                          │
│      → returns { checkout_url, transaction_id }                 │
│                                                                 │
│  GET /api/v1/subscriptions/tenant-subscriptions/success        │
│      → 302 redirect to {frontend_url}/subscription/success     │
│                                                                 │
│  GET /api/v1/payments/status                                    │
│      → returns subscription + provisioning status               │
│                                                                 │
│  PaddleWebhookController                                        │
│    POST /api/v1/payments/webhooks/paddle  (permit-all)         │
│      → PaddleSignatureVerifier  (HMAC-SHA256)                  │
│      → PaddleWebhookProcessor   (routes by event type)         │
│          ├── transaction.completed  → first payment: CREATE subscription
│          │                           renewal: extend end_date   │
│          ├── subscription.created   → trial only: CREATE subscription
│          ├── subscription.activated → set ACTIVE + provision    │
│          ├── subscription.cancelled → set CANCELLED + deprovision│
│          └── subscription.past_due  → set PAST_DUE             │
└─────────────────────────────────────────────────────────────────┘
                 │ HTTPS
┌────────────────▼────────────────────────────────────────────────┐
│                           PADDLE                                │
│   Hosts checkout page                                           │
│   Processes payment                                             │
│   Sends webhooks (subscription.* / transaction.*)              │
│   Redirects browser to success URL after payment               │
└─────────────────────────────────────────────────────────────────┘
```

---

## Package Structure

```
payment/
├── config/
│   ├── PaddleProperties.java               # Binds paddle.* yaml config (apiKey, webhookSecret, apiBaseUrl, successUrl)
│   └── PaddleRestClientConfig.java         # Spring RestClient bean (qualifier: "paddleRestClient")
│
├── controller/
│   ├── PaymentController.java              # POST /checkout, GET /status
│   └── PaddleWebhookController.java        # POST /webhooks/paddle (permit-all)
│
├── dto/
│   ├── request/
│   │   └── CheckoutRequest.java            # { plan_id }
│   ├── response/
│   │   ├── CheckoutResponse.java           # { checkout_url, transaction_id }
│   │   └── PaymentStatusResponse.java      # { subscription_status, provisioning_status, active }
│   ├── paddle/
│   │   └── PaddleCreateTransactionResponse.java  # Maps Paddle POST /transactions response
│   └── webhook/
│       ├── PaddleWebhookPayload.java        # Generic envelope { event_id, event_type, data }
│       ├── PaddleSubscriptionEventData.java # Data for subscription.* events
│       ├── PaddleTransactionEventData.java  # Data for transaction.* events (includes billingPeriod)
│       ├── PaddleBillingPeriod.java         # { starts_at, ends_at }
│       └── PaddleCustomData.java            # { user_id, plan_id } — our own metadata
│
├── exception/
│   ├── PaymentException.java               # Paddle API / configuration errors → 502
│   └── WebhookVerificationException.java   # Signature mismatch → logged, swallowed
│
├── model/
│   ├── entity/
│   │   ├── PaymentEventEntity.java         # Audit log + idempotency store (payment_events)
│   │   └── SubscriptionPaymentDetailsEntity.java  # Paddle-specific IDs (subscription_payment_details)
│   └── enums/
│       ├── PaymentProvider.java            # PADDLE
│       ├── ProductType.java                # STANDALONE | BUNDLED
│       └── ProvisioningStatus.java         # PENDING | PROVISIONED | FAILED
│
├── repository/
│   ├── PaymentEventRepository.java
│   └── SubscriptionPaymentDetailsRepository.java
│
├── service/
│   ├── PaymentService.java                 # createCheckout, getPaymentStatus, cancelUserSubscription
│   └── provisioning/
│       ├── ProvisioningStrategy.java       # Interface: provision(ctx), deprovision(ctx), supports()
│       ├── ProvisioningContext.java        # Immutable record passed to strategies
│       ├── ProvisioningStrategyFactory.java# Auto-discovers all ProvisioningStrategy beans by ProductType
│       ├── StandaloneProvisioningStrategy.java  # STANDALONE: grants access immediately
│       └── BundledProvisioningStrategy.java     # BUNDLED: flags for manual ops action
│
├── serviceImpl/
│   └── PaymentServiceImpl.java            # Implements PaymentService via Paddle REST API
│
└── webhook/
    ├── PaddleSignatureVerifier.java        # HMAC-SHA256 signature verification
    └── PaddleWebhookProcessor.java         # Routes events, updates subscription state

subscription/
├── service/
│   └── PaddleProductService.java          # provisionPlan() — creates Paddle product + price
└── serviceImpl/
    └── PaddleProductServiceImpl.java      # Calls POST /products then POST /prices
```

---

## End-to-End Checkout Flow

### Step-by-step

```
1.  USER visits pricing page
        │
        ▼
2.  GET /api/v1/subscriptions/plans/public               [no auth]
        │  Returns all public plans with paddle_price_id
        │
        ▼
3.  USER selects a plan — frontend calls:
    POST /api/v1/payments/checkout                        [JWT required]
        { "plan_id": 3 }
        │
        │  Backend validates:
        │    - Plan exists and is active
        │    - Plan has a paddle_price_id configured
        │    - User has no existing ACTIVE or TRIAL subscription
        │
        │  Backend calls Paddle:
        │    POST https://api.paddle.com/transactions
        │    {
        │      "items": [{ "price": { "id": "pri_01h..." }, "quantity": 1 }],
        │      "custom_data": { "user_id": "42", "plan_id": "3" },
        │      "checkout": { "url": "https://api.example.com/.../success" }
        │    }
        │
        ▼
4.  Backend returns:
        { "checkout_url": "https://checkout.paddle.com/checkout/...", "transaction_id": "txn_01h..." }
        │
        ▼
5.  FRONTEND opens checkout_url in the browser
        │  (redirect or window.location.href)
        │
        ▼
6.  USER completes payment on Paddle's hosted checkout page
        │
        ├──► PADDLE sends webhooks (server-to-server, order guaranteed):
        │
        │        1. event_type: "transaction.completed"   ← PAYMENT CONFIRMED
        │             POST /api/v1/payments/webhooks/paddle
        │               → Verify HMAC-SHA256 signature
        │               → Check event_id not already in payment_events (idempotency)
        │               → Save raw event to payment_events
        │               → No existing SubscriptionPaymentDetails for this paddle_subscription_id?
        │                     YES (first payment):
        │                       → Read custom_data.user_id + custom_data.plan_id
        │                       → Create TenantSubscriptionEntity (status = ACTIVE)
        │                       → Create SubscriptionPaymentDetailsEntity
        │                             (paddle_subscription_id, paddle_customer_id)
        │                       → Set end_date from transaction billing_period.ends_at
        │                     NO (renewal):
        │                       → Extend end_date by billing cycle
        │                       → Set status = ACTIVE
        │
        │        2. event_type: "subscription.activated"
        │             → Confirm status = ACTIVE, update end_date
        │             → Trigger ProvisioningStrategy
        │                   STANDALONE → provisioningStatus = PROVISIONED (instant)
        │                   BUNDLED    → provisioningStatus = PENDING (ops alert)
        │
        └──► PADDLE redirects browser to success URL:
                 GET /api/v1/subscriptions/tenant-subscriptions/success
                   → 302 redirect to {frontend_url}/subscription/success
        │
        ▼
7.  FRONTEND shows success page, then polls:
    GET /api/v1/payments/status                           [JWT required]
        │
        ▼
8.  Backend returns:
        {
          "subscription_status": "ACTIVE",
          "provisioning_status": "PROVISIONED",
          "active": true
        }
        │
        ▼
9.  USER can now use GHL features (AppKeyInterceptor enforces subscription)
```

### Why `transaction.completed` and not `subscription.created`?

`subscription.created` fires when Paddle **creates the subscription record** — this can happen before payment is
collected (e.g. when the checkout session starts). It does not mean the payment succeeded.

`transaction.completed` fires when Paddle **confirms money was actually collected**. This is the correct moment to grant
access.

| Event                    | Meaning                              | Used for                                                   |
|--------------------------|--------------------------------------|------------------------------------------------------------|
| `subscription.created`   | Paddle subscription object exists    | Trial subscriptions only                                   |
| `transaction.completed`  | Payment was collected                | Creating subscription (paid), extending end_date (renewal) |
| `subscription.activated` | Subscription is now active in Paddle | Confirming ACTIVE status + triggering provisioning         |

### Why not the success redirect?

The browser redirect is not reliable as a payment confirmation mechanism:

- The user can close the browser before the redirect.
- The redirect can be intercepted, replayed, or never reached.
- Query parameters can be tampered with.

**Paddle always sends the webhook** (with retries) as long as the endpoint is reachable. The webhook is the
authoritative source of truth. The success redirect only serves to return the browser to the frontend.

---

## Plan Creation Flow

When an admin creates a subscription plan, Paddle is automatically provisioned:

```
POST /api/v1/subscriptions/plans                          [ADMIN role required]
  {
    "name": "Pro Plan",
    "code": "PRO",
    "price": 29.99,
    "currency_id": 1,
    "billing_cycle": "MONTHLY",
    "trial_period_days": 14,
    "product_type": "STANDALONE",
    "is_public": true,
    "limits": [...]
  }
        │
        ▼
  SubscriptionPlanController.create()
        │
        ├── 1. Resolve currency entity from currency_id
        │
        ├── 2. PaddleProductService.provisionPlan()
        │       │
        │       ├── POST /products
        │       │     { "name": "Pro Plan", "tax_category": "saas" }
        │       │     → paddleProductId = "pro_01h..."
        │       │
        │       └── POST /prices
        │             {
        │               "product_id": "pro_01h...",
        │               "description": "Pro Plan - MONTHLY",
        │               "unit_price": { "amount": "2999", "currency_code": "USD" },
        │               "billing_cycle": { "interval": "month", "frequency": 1 },
        │               "trial_period": { "interval": "day", "frequency": 14 }
        │             }
        │             → paddlePriceId = "pri_01h..."
        │
        └── 3. SubscriptionPlanService.create()
                  → Saves SubscriptionPlanEntity with paddlePriceId = "pri_01h..."
```

If the Paddle API call fails at any point, `PaymentException` is thrown and **nothing is written to the database**.

### BillingCycle → Paddle interval mapping

| Enum value  | Paddle `interval`         | Paddle `frequency` |
|-------------|---------------------------|--------------------|
| `MONTHLY`   | `month`                   | `1`                |
| `QUARTERLY` | `month`                   | `3`                |
| `ANNUAL`    | `year`                    | `1`                |
| `LIFETIME`  | *(none — one-time price)* | —                  |

### Price amount conversion

Paddle expects prices in the **smallest currency unit** as a string.

```
price (BigDecimal) × 100 → toBigInteger() → toString()
Example: 29.99 → 2999 → "2999"
```

---

## API Endpoints

### Public (no authentication required)

#### `GET /api/v1/subscriptions/plans/public`

Returns all active, public subscription plans with their full limit/feature details and `paddle_price_id`.
Frontend uses this to display the pricing page.

**Response `200 OK`**

```json
[
  {
    "id": 3,
    "name": "Pro Plan",
    "code": "PRO",
    "price": 29.99,
    "billing_cycle": "MONTHLY",
    "trial_period_days": 14,
    "product_type": "STANDALONE",
    "paddle_price_id": "pri_01h...",
    "limits": [
      {
        "limit_key": "max_contacts",
        "limit_value": 5000
      }
    ]
  }
]
```

#### `GET /api/v1/subscriptions/plans/{id}`

Returns a single plan by ID.

#### `GET /api/v1/subscriptions/tenant-subscriptions/success`

Paddle redirects the browser here after a successful checkout (configured as the Checkout Return URL).
Returns `302 Found` redirecting the browser to `{frontend.url}/subscription/success`.

> **Note:** Subscription creation happens via the webhook, not here. This endpoint only handles the browser redirect.

---

### Authenticated (JWT Bearer token required)

#### `POST /api/v1/payments/checkout`

Creates a Paddle-hosted checkout session. Returns a URL the frontend opens in the browser.

**Request**

```json
{
  "plan_id": 3
}
```

**Response `200 OK`**

```json
{
  "checkout_url": "https://checkout.paddle.com/checkout/custom/...",
  "transaction_id": "txn_01h..."
}
```

**Error responses**

| Condition                                     | Status            | Code                     |
|-----------------------------------------------|-------------------|--------------------------|
| Plan not found / inactive                     | `404 Not Found`   | `ENTITY_NOT_FOUND`       |
| Plan has no `paddle_price_id`                 | `400 Bad Request` | `INVALID_ARGUMENT`       |
| User already has ACTIVE or TRIAL subscription | `400 Bad Request` | `INVALID_ARGUMENT`       |
| Paddle API call fails                         | `502 Bad Gateway` | `PAYMENT_PROVIDER_ERROR` |

---

#### `GET /api/v1/payments/status`

Returns the user's current subscription status. Designed to be polled by the frontend after the Paddle success redirect
to confirm the webhook has been processed.

**Response `200 OK`**

```json
{
  "subscription_status": "ACTIVE",
  "provisioning_status": "PROVISIONED",
  "active": true
}
```

Returns the **most recent** subscription regardless of status (uses `findFirstByUserIdOrderByIdDesc`).
If the user has no subscription at all: `{ "subscription_status": null, "provisioning_status": null, "active": false }`.

**Frontend polling strategy**

```javascript
async function waitForActivation(maxAttempts = 10, intervalMs = 2000) {
    for (let i = 0; i < maxAttempts; i++) {
        const {data} = await api.get('/payments/status');
        if (data.active && data.provisioning_status === 'PROVISIONED') {
            return data;   // subscription is live
        }
        await sleep(intervalMs);
    }
    throw new Error('Subscription activation timed out');
}
```

---

#### `POST /api/v1/subscriptions/tenant-subscriptions/upgrade`

Upgrades to a different plan. Cancels the current subscription and creates a new one.

**Request**

```json
{
  "new_plan_id": 5
}
```

> Note: This does not call the Paddle API — it only updates the local subscription record. A future enhancement should
> call Paddle's subscription upgrade API to keep billing aligned.

---

#### `DELETE /api/v1/subscriptions/tenant-subscriptions/cancel`

Cancels the user's subscription.

1. Calls Paddle `POST /subscriptions/{id}/cancel` with `effective_from: next_billing_period`.
2. Locally cancels the subscription record (sets status = `CANCELLED`, `is_active = false`).

Paddle will also send a `subscription.cancelled` webhook which is handled idempotently.

---

#### `POST /api/v1/payments/webhooks/paddle` *(permit-all)*

Receives Paddle webhook events. Called server-to-server by Paddle — no JWT.

**Required header:** `Paddle-Signature: ts=<unix_timestamp>;h1=<hex_hmac>`

Always returns `200 OK`, even on processing errors (to prevent Paddle retries for non-transient failures).

---

### Admin-only

#### `POST /api/v1/subscriptions/plans` *(ADMIN role)*

Creates a new subscription plan and auto-provisions a product + price in Paddle.

#### `PUT /api/v1/subscriptions/plans/{id}` *(ADMIN role)*

Updates plan metadata and limits. Does **not** update the Paddle product/price.

#### `DELETE /api/v1/subscriptions/plans/{id}` *(ADMIN role)*

Soft-deletes a plan (`is_deleted = true`, `is_active = false`). Does not archive the Paddle product.

#### `GET /api/v1/subscriptions/plans` *(ADMIN role)*

Paginated list of all plans.

#### `GET /api/v1/subscriptions/tenant-subscriptions` *(ADMIN role)*

Paginated list of all active tenant subscriptions.

---

## Webhook Event Handling

All events flow through `PaddleWebhookProcessor` after passing signature verification.

### Idempotency

```
Incoming webhook
      │
      ▼
PaymentEventRepository.existsByEventId(eventId)
      │
      ├── TRUE  → log "skipping duplicate" and return
      │
      └── FALSE → save to payment_events (raw payload + timestamp)
                        │
                        ▼
                  route by event_type
```

### Event routing table

| Paddle event             | Local action                                                                                                                                                                                                          |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `transaction.completed`  | **First payment:** creates `TenantSubscriptionEntity` (ACTIVE) + `SubscriptionPaymentDetailsEntity`, sets `end_date` from `billing_period`. **Renewal:** extends `end_date` by billing cycle, sets status → `ACTIVE`. |
| `subscription.created`   | **Trial only** (Paddle status = `"trialing"`): creates `TenantSubscriptionEntity` (TRIAL) + `SubscriptionPaymentDetailsEntity`. Skipped for paid subscriptions.                                                       |
| `subscription.activated` | Sets status → `ACTIVE`, updates `end_date` from `current_billing_period.ends_at`, triggers provisioning.                                                                                                              |
| `subscription.cancelled` | Sets status → `CANCELLED`, `is_active = false`, triggers deprovisioning.                                                                                                                                              |
| `subscription.past_due`  | Sets status → `PAST_DUE`. Access is **not** revoked automatically — the user enters a grace period.                                                                                                                   |

### `transaction.completed` — custom data

Paddle's `custom_data` carries `user_id` and `plan_id` injected at checkout time by the backend. These are read on the
first payment to know which user and plan to create the subscription for.

```json
"custom_data": {
"user_id": "42",
"plan_id": "3"
}
```

If either field is `null` on a first-payment transaction, the event is skipped with a warning log and no subscription is
created.

### First payment vs. renewal detection

`handleTransactionCompleted` looks up `SubscriptionPaymentDetailsEntity` by `data.subscriptionId`:

```
existingDetails present?
  NO  → first payment  → createSubscriptionFromTransaction()
  YES → renewal        → extend end_date by billing cycle
```

### `billing_period` on `transaction.completed`

`PaddleTransactionEventData` now includes a `billingPeriod` field (`starts_at` / `ends_at`). On first payment,`end_date`
is set directly from `billing_period.ends_at`. On renewal, `end_date` is calculated from the local billing cycle enum
instead (Paddle's value is a secondary source of truth).

### Duplicate subscription guard

On `transaction.completed` (first payment), the processor checks:

- `SubscriptionPaymentDetailsRepository.existsByPaddleSubscriptionId(data.subscriptionId)` — guards against Paddle
  delivering `transaction.completed` twice for the same subscription.
- `TenantSubscriptionRepository.existsByUserIdAndStatusIn(userId, [ACTIVE, TRIAL])` — guards against double-checkout (
  user opens checkout twice and both payments go through).

### Renewal end date calculation

On `transaction.completed` (renewal path — `SubscriptionPaymentDetailsEntity` already exists), end date is extended from
the current `endDate` (or `now()` if null). For a first payment, `end_date` is taken directly from Paddle's
`billing_period.ends_at`.

| Billing cycle | Extension          |
|---------------|--------------------|
| `MONTHLY`     | +30 days           |
| `QUARTERLY`   | +90 days           |
| `ANNUAL`      | +365 days          |
| `LIFETIME`    | `null` (no expiry) |

---

## Signature Verification

`PaddleSignatureVerifier`
implements [Paddle's HMAC-SHA256 webhook signing scheme](https://developer.paddle.com/webhooks/signature-verification):

```
Paddle-Signature: ts=1700000000;h1=abc123def456...

Signed payload = "<ts>:<raw_body_utf8>"
Expected HMAC  = HMAC-SHA256(key=PADDLE_WEBHOOK_SECRET, data=signed_payload)
```

1. Parses the `Paddle-Signature` header for `ts` and `h1` parts.
2. Validates the timestamp is within **300 seconds** of server time (replay attack prevention).
3. Computes HMAC-SHA256 using `PADDLE_WEBHOOK_SECRET`.
4. Compares using `MessageDigest.isEqual` — constant-time comparison to prevent timing attacks.

`WebhookVerificationException` is thrown on any failure, caught in `PaddleWebhookController`, logged, and a `200 OK` is
returned.

---

## Provisioning Strategies

Triggered on `subscription.activated` (provision) and `subscription.cancelled` (deprovision).
`ProvisioningStrategyFactory` auto-discovers all `@Component` implementations of `ProvisioningStrategy`.

### STANDALONE

Plans where product access can be granted automatically (e.g. API rate limits, feature flags).

| Event           | Action                                                                     |
|-----------------|----------------------------------------------------------------------------|
| `provision()`   | Sets `provisioningStatus = PROVISIONED` on the `TenantSubscriptionEntity`. |
| `deprovision()` | Hook for access cleanup (log + placeholder for feature-flag revocation).   |

After `provision()` completes, the user's app key will pass the `AppKeyInterceptor` checks.

### BUNDLED

Plans that require manual setup (e.g. creating a GHL subaccount for the customer).

| Event           | Action                                                                |
|-----------------|-----------------------------------------------------------------------|
| `provision()`   | Sets `provisioningStatus = PENDING`. Logs a warning for the ops team. |
| `deprovision()` | Logs a warning for the ops team to deactivate the GHL subaccount.     |

> **TODO (`BundledProvisioningStrategy`):** Replace the log warning with an actual admin notification — email, Slack, or
> ticket system.

---

## GHL Access Enforcement

`AppKeyInterceptor` runs before every controller method annotated with `@AppKey`. All GHL proxy endpoints use this
annotation.

### Checks performed (in order)

1. **Header present** — `Authorization: Bearer <appKey>` must be in the request.
2. **Key valid** — the app key must exist in the database and be active (`is_active = true`, `is_deleted = false`).
3. **Subscription status valid** — the subscription linked to the app key must have status `ACTIVE` or `TRIAL`.
4. **Subscription not expired** — `end_date` must be `null` (LIFETIME plan) or in the future.

### Failures

| Condition                     | Exception                       | HTTP Status                |
|-------------------------------|---------------------------------|----------------------------|
| Missing/malformed header      | `401` (direct response)         | `401 Unauthorized`         |
| Invalid or unknown app key    | `AppKeyInvalidException`        | `401 Unauthorized`         |
| Subscription not ACTIVE/TRIAL | `NoActiveSubscriptionException` | `422 Unprocessable Entity` |
| Subscription expired          | `NoActiveSubscriptionException` | `422 Unprocessable Entity` |

### Automatic renewal

Paddle sends `transaction.completed` when a subscription renews. `PaddleWebhookProcessor` extends `endDate` and sets
status = `ACTIVE`. The user's next API call passes the interceptor automatically — no user action required.

---

## Data Model

### `subscription_payment_details`

Stores Paddle-specific data, decoupled from the domain `tenant_subscriptions` table.

| Column                   | Type                  | Notes                                                 |
|--------------------------|-----------------------|-------------------------------------------------------|
| `id`                     | `bigserial PK`        |                                                       |
| `tenant_subscription_id` | `bigint UNIQUE FK`    | One-to-one with `tenant_subscriptions`                |
| `payment_provider`       | `varchar(20)`         | Enum: `PADDLE`                                        |
| `paddle_subscription_id` | `varchar(100) UNIQUE` | `sub_01h...` — used to look up local sub from webhook |
| `paddle_customer_id`     | `varchar(100)`        | `ctm_01h...`                                          |
| `created_at`             | `timestamptz`         | Set on insert                                         |
| `updated_at`             | `timestamptz`         | Updated on every change                               |

Index: `idx_spd_paddle_subscription_id` on `paddle_subscription_id` (unique).

### `payment_events`

Audit log and idempotency store. Every processed webhook is recorded here before business logic runs.

| Column         | Type                  | Notes                                     |
|----------------|-----------------------|-------------------------------------------|
| `id`           | `bigserial PK`        |                                           |
| `event_id`     | `varchar(100) UNIQUE` | Paddle's `evt_xxx` ID — deduplication key |
| `provider`     | `varchar(20)`         | Enum: `PADDLE`                            |
| `event_type`   | `varchar(100)`        | e.g. `subscription.created`               |
| `payload`      | `text`                | Full raw webhook JSON body                |
| `processed_at` | `timestamptz`         | When the event was first seen             |

Indexes: `idx_payment_events_event_id` (unique), `idx_payment_events_event_type`.

### Changes to existing tables (migration V13)

**`subscription_plans`** — two new columns:

| Column            | Type                   | Default        | Notes                                          |
|-------------------|------------------------|----------------|------------------------------------------------|
| `product_type`    | `varchar(20) NOT NULL` | `'STANDALONE'` | Determines provisioning path                   |
| `paddle_price_id` | `varchar(100)`         | `NULL`         | Auto-populated on plan creation via Paddle API |

**`tenant_subscriptions`** — one new column:

| Column                | Type                   | Default     | Notes                                   |
|-----------------------|------------------------|-------------|-----------------------------------------|
| `provisioning_status` | `varchar(20) NOT NULL` | `'PENDING'` | `PENDING` → `PROVISIONED` after webhook |

---

## Configuration

Missing required environment variables cause a **startup failure** with a descriptive error message.

```yaml
paddle:
  api-key: ${PADDLE_API_KEY:?PADDLE_API_KEY must be set!}
  webhook-secret: ${PADDLE_WEBHOOK_SECRET:?PADDLE_WEBHOOK_SECRET must be set!}
  api-base-url: ${PADDLE_API_BASE_URL:https://api.paddle.com}
  success-url: ${PADDLE_SUCCESS_URL:${backend.url}/api/v1/subscriptions/tenant-subscriptions/success}
```

| Property                | Env var                 | Required                | Description                                                                                                |
|-------------------------|-------------------------|-------------------------|------------------------------------------------------------------------------------------------------------|
| `paddle.api-key`        | `PADDLE_API_KEY`        | **Yes** — startup fails | Paddle secret API key for server-side calls                                                                |
| `paddle.webhook-secret` | `PADDLE_WEBHOOK_SECRET` | **Yes** — startup fails | Used to verify HMAC-SHA256 webhook signatures                                                              |
| `paddle.api-base-url`   | `PADDLE_API_BASE_URL`   | No                      | Defaults to `https://api.paddle.com`. Set to sandbox URL for testing.                                      |
| `paddle.success-url`    | `PADDLE_SUCCESS_URL`    | No                      | Defaults to `{backend.url}/api/v1/subscriptions/tenant-subscriptions/success`. Override for custom domain. |

### Required Paddle API key scopes

The `PADDLE_API_KEY` must have the following permissions in the Paddle Dashboard:

- `product:read` / `product:create`
- `price:read` / `price:create`
- `transaction:read` / `transaction:create`
- `subscription:read` / `subscription:update` (for cancellation)

### Paddle Dashboard setup

In **Paddle Dashboard → Checkout Settings**, set the **Return URL** to:

```
https://{your-backend-domain}/api/v1/subscriptions/tenant-subscriptions/success
```

In **Paddle Dashboard → Notifications**, register the webhook endpoint:

```
https://{your-backend-domain}/api/v1/payments/webhooks/paddle
```

Subscribe to these events:

- `subscription.created`
- `subscription.activated`
- `subscription.cancelled`
- `subscription.past_due`
- `transaction.completed`

---

## Error Handling

### `GlobalExceptionHandler` mapping

| Exception                         | HTTP Status                | Error code               |
|-----------------------------------|----------------------------|--------------------------|
| `EntityNotFoundException`         | `404 Not Found`            | `ENTITY_NOT_FOUND`       |
| `IllegalArgumentException`        | `400 Bad Request`          | `INVALID_ARGUMENT`       |
| `PaymentException`                | `502 Bad Gateway`          | `PAYMENT_PROVIDER_ERROR` |
| `NoActiveSubscriptionException`   | `422 Unprocessable Entity` | `NO_ACTIVE_SUBSCRIPTION` |
| `AppKeyInvalidException`          | `401 Unauthorized`         | `INVALID_APP_KEY`        |
| `MethodArgumentNotValidException` | `400 Bad Request`          | `VALIDATION_ERROR`       |

### Webhook error behaviour

Errors inside `PaddleWebhookProcessor` are caught in `PaddleWebhookController` and logged.
`200 OK` is always returned, because:

- Paddle retries on non-2xx, which can cause duplicate processing.
- Idempotency is already handled — re-processing a stored event is a no-op.

If an event fails processing (e.g. plan not found), the raw payload is still in `payment_events` and can be replayed
manually after the issue is fixed.

---

## Sandbox Testing

1. Set `PADDLE_API_BASE_URL=https://sandbox-api.paddle.com`.
2. Use a sandbox API key from [sandbox-vendors.paddle.com](https://sandbox-vendors.paddle.com).
3. Use Paddle's sandbox checkout (sandbox billing details).
4. Forward Paddle sandbox webhooks to your local machine
   using [Paddle's CLI](https://developer.paddle.com/webhooks/overview#test-webhooks) or a tunnel tool.

**Testing webhook signature locally:**

```bash
# Paddle CLI
paddle webhook listen --sandbox --endpoint http://localhost:8080/api/v1/payments/webhooks/paddle
```

---

## Adding a New Provisioning Type

1. Add a value to the `ProductType` enum (e.g. `ENTERPRISE`).
2. Create a new class implementing `ProvisioningStrategy`, annotate with `@Component`, and return the new `ProductType`
   from `supports()`.
3. `ProvisioningStrategyFactory` will auto-discover it — no factory changes needed.
4. Admins set `product_type = ENTERPRISE` when creating plans.

Example skeleton:

```java

@Slf4j
@Component
public class EnterpriseProvisioningStrategy implements ProvisioningStrategy {

    @Override
    public void provision(ProvisioningContext ctx) {
        // e.g. call internal CRM, send welcome email, create dedicated resources
        log.info("[ENTERPRISE] Provisioning userId={}", ctx.userId());
    }

    @Override
    public void deprovision(ProvisioningContext ctx) {
        log.info("[ENTERPRISE] Deprovisioning userId={}", ctx.userId());
    }

    @Override
    public ProductType supports() {
        return ProductType.ENTERPRISE;
    }
}
```
