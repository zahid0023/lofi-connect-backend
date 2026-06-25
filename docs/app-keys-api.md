# App Keys API

Base URL: `/api/v1/app-keys`

An **App Key** is the API credential a tenant uses to authenticate requests to the API gateway (`/api/v1/ghl/**`). It
is always generated against an active Tenant Subscription — not against the user account directly. This design means
the key implicitly carries the subscription context (plan, limits) used at enforcement time.

---

## Table of Contents

1. [Concept](#concept)
2. [Prerequisites](#prerequisites)
3. [Endpoints](#endpoints)
4. [Data Model](#data-model)
5. [Generate App Key](#generate-app-key)
6. [List My App Keys](#list-my-app-keys)
7. [Assign to GoHighLevel](#assign-to-gohighlevel)
8. [Using the App Key](#using-the-app-key)
9. [Key Lifecycle](#key-lifecycle)
10. [Error Responses](#error-responses)
11. [Design Rules](#design-rules)

---

## Concept

```
User ──subscribes──► TenantSubscription
                            │
                     generates key against
                            │
                            ▼
                       App Key (credential)
                            │
                     used in every request
                            │
                            ▼
              AppKeyFilter validates key
                            │
                    checks subscription status
                            │
                   ACTIVE/TRIAL? → forward to GHL
                   else        → 401 Unauthorized
```

An App Key carries no permissions of its own. Access is governed entirely by the linked subscription's status and plan
limits. If the subscription is cancelled or expired, the key stops working.

---

## Prerequisites

A user must have an active subscription before generating an App Key:

```
1. Register      POST /api/v1/auth/registration/user
2. Login         POST /api/v1/auth/login
3. Subscribe     POST /api/v1/subscriptions/tenant-subscriptions
4. Generate Key  POST /api/v1/app-keys/generate          ← this section
5. Use Key       GET  /api/v1/ghl/...   X-App-Key: <key>
```

Attempting to generate a key without an active subscription returns `422 NO_ACTIVE_SUBSCRIPTION`.

---

## Endpoints

| Method | Path                          | Auth          | Description                                           |
|--------|-------------------------------|---------------|-------------------------------------------------------|
| `POST` | `/api/v1/app-keys/generate`   | Authenticated | Generate a new App Key against active subscription    |
| `GET`  | `/api/v1/app-keys`            | Authenticated | List all App Keys belonging to the authenticated user |
| `PUT`  | `/api/v1/app-keys/assign-ghl` | Authenticated | Assign an App Key to a GoHighLevel account            |

---

## Data Model

### App Key Fields (response)

| Field                    | Type              | Description                                           |
|--------------------------|-------------------|-------------------------------------------------------|
| `id`                     | Long              | Auto-generated identifier                             |
| `name`                   | String            | Friendly name given by the user                       |
| `app_key`                | String            | The full API key — only shown once at generation time |
| `masked_key`             | String            | Obfuscated key for safe display: `abcd****wxyz`       |
| `status`                 | String            | `"active"` or `"inactive"`                            |
| `subscription_id`        | Long              | ID of the linked TenantSubscription                   |
| `subscription_plan_id`   | Long              | ID of the plan at key generation time                 |
| `subscription_plan_name` | String            | Human-readable plan name (e.g. `Professional`)        |
| `ghl_connection`         | Object \| null    | The GoHighLevel connection linked to this key, if any |
| `created_at`             | String (ISO-8601) | When the key was created                              |
| `updated_at`             | String (ISO-8601) | When the key was last updated                         |

---

## Generate App Key

`POST /api/v1/app-keys/generate`

> Requires authentication (`Authorization: Bearer <token>`).

Creates a new App Key linked to the authenticated user's active (ACTIVE or TRIAL) subscription. The full key value is
returned **only once** in this response. Store it immediately — subsequent list calls only return the masked version.

### Request Body

```json
{
  "name": "Production Key"
}
```

### Request Fields

| Field  | Type   | Required | Description                                                  |
|--------|--------|----------|--------------------------------------------------------------|
| `name` | String | Yes      | A friendly label for this key (e.g. `Production`, `Staging`) |

### Response `201 Created`

```json
{
  "app_key": {
    "id": 1,
    "name": "Production Key",
    "app_key": "lc_a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4",
    "masked_key": "lc_a****3d4",
    "status": "active",
    "subscription_id": 42,
    "subscription_plan_id": 3,
    "subscription_plan_name": "Professional",
    "ghl_connection": null,
    "created_at": "2026-06-25T10:15:00Z",
    "updated_at": "2026-06-25T10:15:00Z"
  }
}
```

> **Save the `app_key` value now.** It will not be shown again. The list endpoint only returns `masked_key`.

### Validations

| Check                  | Error                        | Message                                                      |
|------------------------|------------------------------|--------------------------------------------------------------|
| No active subscription | `422 NO_ACTIVE_SUBSCRIPTION` | `An active subscription is required to generate an App Key.` |
| Not authenticated      | `401`                        | —                                                            |

---

## List My App Keys

`GET /api/v1/app-keys`

> Requires authentication (`Authorization: Bearer <token>`).

Returns all App Keys belonging to the authenticated user that are active and non-deleted.

### Response `200 OK`

```json
{
  "app_keys": [
    {
      "id": 1,
      "name": "Production Key",
      "app_key": null,
      "masked_key": "lc_a****3d4",
      "status": "active",
      "subscription_id": 42,
      "subscription_plan_id": 3,
      "subscription_plan_name": "Professional",
      "ghl_connection": {
        "id": 5,
        "location_id": "abc123",
        "location_name": "My CRM Location"
      },
      "created_at": "2026-06-25T10:15:00Z",
      "updated_at": "2026-06-25T10:15:00Z"
    }
  ]
}
```

> `app_key` is always `null` in list responses. Only the generate endpoint returns the real key.

---

## Assign to GoHighLevel

`PUT /api/v1/app-keys/assign-ghl`

> Requires authentication (`Authorization: Bearer <token>`).

Links an App Key to a GoHighLevel location/account. Once linked, API calls made with this key are forwarded to the
associated GHL account. See the GHL Authorization docs for the full OAuth flow.

---

## Using the App Key

Once generated, include the key in every API gateway request using the `X-App-Key` header:

```
GET /api/v1/ghl/contacts/abc123
X-App-Key: lc_a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4
```

No `Authorization: Bearer` header is needed for gateway calls — the App Key replaces JWT authentication entirely for
the `/api/v1/ghl/**` path family.

### What the gateway checks (order)

```
1. X-App-Key header present?
        No  → 401 Unauthorized

2. App Key valid, active, not deleted?
        No  → 401 Invalid App Key

3. Linked subscription in ACTIVE or TRIAL status?
        No  → 401 Unauthorized (subscription cancelled/expired)

4. [Future] Usage within plan limits?
        No  → 429 Too Many Requests

5. Forward to GoHighLevel
```

---

## Key Lifecycle

```
Generate Key
     │
     ▼
  ACTIVE ──────────────────────────────────────────────────────► INACTIVE
     │                                                               ▲
     │  subscription cancelled / expired                            │
     └──────────────────────────────────────────────────────────────┘
```

| Event                           | Effect on Key                                                        |
|---------------------------------|----------------------------------------------------------------------|
| Subscription is ACTIVE or TRIAL | Key works                                                            |
| Subscription is CANCELLED       | Key stops working (subscription check fails)                         |
| Subscription is EXPIRED         | Key stops working                                                    |
| User upgrades plan              | Old key stops working (old subscription cancelled); generate new key |

> **After upgrading:** The old App Key is linked to the cancelled subscription and will be rejected by the gateway.
> Generate a new App Key after every upgrade.

---

## Error Responses

```json
{
  "request_id": "abc-123",
  "status": 422,
  "error": "NO_ACTIVE_SUBSCRIPTION",
  "message": "An active subscription is required to generate an App Key."
}
```

| HTTP Status | Error Code               | Cause                                                                |
|-------------|--------------------------|----------------------------------------------------------------------|
| `401`       | `INVALID_APP_KEY`        | Key does not exist, is inactive, or subscription is not ACTIVE/TRIAL |
| `401`       | —                        | Missing `Authorization` header on generate/list endpoints            |
| `422`       | `NO_ACTIVE_SUBSCRIPTION` | No ACTIVE or TRIAL subscription found for this user                  |
| `500`       | `INTERNAL_SERVER_ERROR`  | Unexpected server error                                              |

---

## Design Rules

1. **Keys are subscription-scoped, not user-scoped.** A key is tied to a specific `TenantSubscription`. If that
   subscription is cancelled or replaced (via upgrade), the key becomes invalid.

2. **The full key value is shown only once.** Store it immediately after generation. Subsequent calls only return
   `masked_key` (`abcd****wxyz`).

3. **Keys have no permissions of their own.** Access is determined entirely by the linked subscription's status and plan
   limits. The key is an authentication credential only.

4. **Multiple keys per subscription are allowed.** A user can generate more than one App Key against the same
   subscription (e.g. a `Production Key` and a `Staging Key`). All keys share the same subscription limits.

5. **Deactivated keys are never physically deleted.** `is_active = false` and `is_deleted = true` are used for
   revocation. The key record remains for audit purposes.

6. **A GHL connection is optional at generation time.** A key can be generated before connecting it to a GoHighLevel
   account. Connect it later via `PUT /assign-ghl`.
