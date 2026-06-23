# Limit Keys API

Base URL: `/api/v1/subscriptions/limit-keys`

Limit Keys are the **central registry of system constraints**. They define *what* can be limited in the platform — not
the actual values. Values (e.g. 5, 1000) are set in Subscription Plans. This separation makes limits globally reusable
and easy to evolve over time.

---

## Table of Contents

1. [Concept](#concept)
2. [Flow](#flow)
3. [Endpoints](#endpoints)
4. [Data Model](#data-model)
5. [Enumerations](#enumerations)
6. [Create Limit Key](#create-limit-key)
7. [Get Limit Key](#get-limit-key)
8. [List All Limit Keys](#list-all-limit-keys)
9. [Update Limit Key](#update-limit-key)
10. [Delete Limit Key](#delete-limit-key)
11. [Error Responses](#error-responses)
12. [Design Rules](#design-rules)

---

## Concept

A **Limit Key** answers: *"What thing can be limited?"*

A **Subscription Plan** answers: *"How much is allowed?"*

| Concept                 | Responsibility                                     | Example                           |
|-------------------------|----------------------------------------------------|-----------------------------------|
| **Limit Key**           | Defines the type of limit (global, reusable)       | `API_KEYS` — max API keys allowed |
| **Subscription Plan**   | Assigns a value to a limit key for a specific tier | Plan A: `API_KEYS = 5`            |
| **Tenant Subscription** | Snapshots plan limits at the time of subscription  | User X subscribed: `API_KEYS = 5` |

### Why separate Limit Keys from plans?

- The same limit (e.g. `API_KEYS`) can appear in every plan with different values.
- Renaming or redefining a limit in one place updates it everywhere.
- Adding a new constraint to the system requires only one new Limit Key, then assigning it to any number of plans.

---

## Flow

```
[Admin creates Limit Keys]
        ↓
[Admin creates Subscription Plans — assigns values to Limit Keys]
        ↓
[Tenant subscribes to a Plan]
        ↓
[Plan limits are snapshotted onto the Tenant Subscription]
        ↓
[API Gateway enforces limits per request]
```

### End-to-End Example

**Step 1 — Define Limit Keys (one-time setup):**

| Code           | Meaning                             | Data Type | Category | Unit    |
|----------------|-------------------------------------|-----------|----------|---------|
| `API_KEYS`     | Max API keys a user can create      | INTEGER   | RESOURCE | `COUNT` |
| `API_REQUESTS` | Max API requests per billing period | INTEGER   | USAGE    | `COUNT` |
| `CRM_ACCOUNTS` | Max CRM accounts connectable        | INTEGER   | RESOURCE | `COUNT` |

**Step 2 — Assign values when creating plans:**

| Plan         | `API_KEYS` | `API_REQUESTS` | `CRM_ACCOUNTS` |
|--------------|------------|----------------|----------------|
| Basic        | 5          | 1 000          | 1              |
| Professional | 20         | 10 000         | 5              |
| Enterprise   | 100        | 100 000        | Unlimited (-1) |

**Step 3 — Runtime enforcement:**

```
User makes API request
    → Check API Key
    → Find active Tenant Subscription
    → Look up limit for API_REQUESTS
    → Current usage < limit? → Allow : Reject (429)
```

---

## Endpoints

| Method   | Path                                    | Auth  | Description             |
|----------|-----------------------------------------|-------|-------------------------|
| `POST`   | `/api/v1/subscriptions/limit-keys`      | ADMIN | Create a limit key      |
| `GET`    | `/api/v1/subscriptions/limit-keys/{id}` | ADMIN | Get a limit key by ID   |
| `GET`    | `/api/v1/subscriptions/limit-keys`      | ADMIN | List all limit keys     |
| `PUT`    | `/api/v1/subscriptions/limit-keys/{id}` | ADMIN | Update a limit key      |
| `DELETE` | `/api/v1/subscriptions/limit-keys/{id}` | ADMIN | Soft-delete a limit key |

> All endpoints require `ADMIN` role. Limit Keys are internal system configuration — not exposed to regular users.

---

## Data Model

| Field         | Type               | Required | Constraints           | Description                                                  |
|---------------|--------------------|----------|-----------------------|--------------------------------------------------------------|
| `id`          | Long               | —        | read-only             | Auto-generated identifier                                    |
| `code`        | String             | Yes      | unique, max 100 chars | Machine-readable key (e.g. `API_KEYS`). Set once, immutable. |
| `name`        | String             | Yes      | max 150 chars         | Human-readable label (e.g. `API Key Creation Limit`)         |
| `description` | String             | No       | free text             | Explains what this limit controls                            |
| `data_type`   | `LimitKeyDataType` | Yes      | enum                  | The value type: `INTEGER`, `BOOLEAN`, or `DECIMAL`           |
| `category`    | `LimitKeyCategory` | Yes      | enum                  | The kind of limit: `USAGE`, `FEATURE`, or `RESOURCE`         |
| `unit`        | `LimitKeyUnit`     | No       | enum                  | The unit of measurement: `COUNT`, `SIZE`, `NONE`             |

---

## Enumerations

### `data_type` — `LimitKeyDataType`

Defines what kind of value this limit holds when assigned to a plan.

| Value     | Description                     | Example                               |
|-----------|---------------------------------|---------------------------------------|
| `INTEGER` | A whole number limit            | `API_KEYS = 5`, `API_REQUESTS = 1000` |
| `BOOLEAN` | A feature flag (allowed or not) | `EXPORT_CSV = true`                   |
| `DECIMAL` | A decimal number limit          | `STORAGE_GB = 1.5`                    |

> Most limits in a CRM gateway will be `INTEGER`. Use `BOOLEAN` for feature toggles (e.g. access to a premium feature).
> Use `DECIMAL` for continuous measurements like storage.

---

### `unit` — `LimitKeyUnit`

Defines the unit of measurement for the limit value. Use `NONE` for `BOOLEAN` feature flags.

| Value   | Description                                                            | Used With                                                  |
|---------|------------------------------------------------------------------------|------------------------------------------------------------|
| `COUNT` | Any countable integer limit — keys, requests, accounts, members, calls | `API_KEYS`, `API_REQUESTS`, `CRM_ACCOUNTS`, `TEAM_MEMBERS` |
| `SIZE`  | Any size-based limit — MB, GB, or any storage measurement              | `STORAGE_LIMIT`, `FILE_UPLOAD_LIMIT`                       |
| `NONE`  | No unit — used for boolean feature flags                               | `EXPORT_CSV`, `ADVANCED_ANALYTICS`                         |

---

### `category` — `LimitKeyCategory`

Groups limits by their purpose, which helps UI, reporting, and enforcement logic understand what a limit controls.

| Value      | Description                                              | Example Limit Keys                         |
|------------|----------------------------------------------------------|--------------------------------------------|
| `USAGE`    | Rolling consumption limits — reset each billing period   | `API_REQUESTS`, `WEBHOOK_CALLS`            |
| `FEATURE`  | Access to a feature — on/off toggle or feature-level cap | `EXPORT_CSV`, `ADVANCED_ANALYTICS`         |
| `RESOURCE` | Hard caps on resources the user owns — do not reset      | `API_KEYS`, `CRM_ACCOUNTS`, `TEAM_MEMBERS` |

---

## Create Limit Key

`POST /api/v1/subscriptions/limit-keys`

> Requires `ADMIN` role.

Creates a new limit key. The `code` field is unique and cannot be changed after creation.

### Request Body

```json
{
  "code": "API_KEYS",
  "name": "API Key Creation Limit",
  "description": "Maximum number of API keys a user can generate.",
  "data_type": "INTEGER",
  "category": "RESOURCE",
  "unit": "KEYS"
}
```

### Request Fields

| Field         | Type   | Required | Validation                       |
|---------------|--------|----------|----------------------------------|
| `code`        | String | Yes      | Not blank, max 100 chars, unique |
| `name`        | String | Yes      | Not blank, max 150 chars         |
| `description` | String | No       | Free text                        |
| `data_type`   | String | Yes      | `INTEGER`, `BOOLEAN`, `DECIMAL`  |
| `category`    | String | Yes      | `USAGE`, `FEATURE`, `RESOURCE`   |
| `unit`        | String | No       | `COUNT`, `SIZE`, `NONE`          |

> `code` should follow `SCREAMING_SNAKE_CASE` convention and be descriptive (e.g. `API_KEYS`, `API_REQUESTS`). Once a
> limit key is used in production plans, its `code` and semantic meaning should not change.

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Limit Key

`GET /api/v1/subscriptions/limit-keys/{id}`

> Requires `ADMIN` role.

Returns the full details of a single limit key.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the limit key |

### Response `200 OK`

```json
{
  "limit_key": {
    "id": 1,
    "code": "API_KEYS",
    "name": "API Key Creation Limit",
    "description": "Maximum number of API keys a user can generate.",
    "data_type": "INTEGER",
    "category": "RESOURCE",
    "unit": "KEYS"
  }
}
```

---

## List All Limit Keys

`GET /api/v1/subscriptions/limit-keys`

> Requires `ADMIN` role.

Returns a paginated list of all active (non-deleted) limit keys.

### Query Parameters

| Parameter  | Type   | Default | Allowed Values                                | Description           |
|------------|--------|---------|-----------------------------------------------|-----------------------|
| `page`     | int    | `0`     | >= 0                                          | Zero-based page index |
| `size`     | int    | `10`    | 1 – 50                                        | Items per page        |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `category`, `createdAt` | Field to sort by      |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                 | Sort direction        |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "API_KEYS",
      "name": "API Key Creation Limit",
      "category": "RESOURCE",
      "data_type": "INTEGER",
      "unit": "KEYS"
    },
    {
      "id": 2,
      "code": "API_REQUESTS",
      "name": "API Request Limit",
      "category": "USAGE",
      "data_type": "INTEGER",
      "unit": "REQUESTS"
    },
    {
      "id": 3,
      "code": "CRM_ACCOUNTS",
      "name": "CRM Account Limit",
      "category": "RESOURCE",
      "data_type": "INTEGER",
      "unit": "ACCOUNTS"
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

> The list endpoint returns a **summary** projection (no `description` field). Use `GET /{id}` to fetch the full record
> including description.

---

## Update Limit Key

`PUT /api/v1/subscriptions/limit-keys/{id}`

> Requires `ADMIN` role.

Updates the human-readable fields of a limit key. `code` is immutable and cannot be changed through this endpoint.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the limit key |

### Request Body

```json
{
  "name": "API Key Creation Limit",
  "description": "Updated description — controls how many API keys a tenant may have active at once.",
  "data_type": "INTEGER",
  "category": "RESOURCE",
  "unit": "KEYS"
}
```

### Request Fields

| Field         | Type   | Required | Validation                      |
|---------------|--------|----------|---------------------------------|
| `name`        | String | Yes      | Not blank, max 150 chars        |
| `description` | String | No       | Free text                       |
| `data_type`   | String | Yes      | `INTEGER`, `BOOLEAN`, `DECIMAL` |
| `category`    | String | Yes      | `USAGE`, `FEATURE`, `RESOURCE`  |
| `unit`        | String | No       | `COUNT`, `SIZE`, `NONE`         |

> **Warning:** Changing `data_type` or `category` on a limit key that is already assigned to active subscription plans
> may cause inconsistencies in enforcement logic. Only update these fields during initial system setup.

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Limit Key

`DELETE /api/v1/subscriptions/limit-keys/{id}`

> Requires `ADMIN` role.

Soft-deletes the limit key. The record is not removed from the database — it is flagged as `is_deleted = true` and will
no longer appear in any list or lookup response.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the limit key |

> **Warning:** Do not delete a limit key that is currently referenced by active subscription plans or tenant
> subscriptions. Doing so will break plan limit resolution at runtime. Deactivate the plan first, then remove the limit
> key.

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Error Responses

All errors follow a common structure:

```json
{
  "request_id": "abc-123",
  "status": 404,
  "error": "ENTITY_NOT_FOUND",
  "message": "LimitKey not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                              |
|-------------|----------------------------|--------------------------------------------------------------------|
| `400`       | `INVALID_ARGUMENT`         | Missing required fields, invalid enum value, or invalid sort field |
| `403`       | `ACCESS_DENIED`            | Non-admin attempting any operation                                 |
| `404`       | `ENTITY_NOT_FOUND`         | Limit key not found or already soft-deleted                        |
| `409`       | `DATA_INTEGRITY_VIOLATION` | Duplicate `code` (e.g. `API_KEYS` already exists)                  |
| `500`       | `INTERNAL_SERVER_ERROR`    | Unexpected server error                                            |

---

## Design Rules

1. **`code` is immutable.** It is the stable machine identifier used by subscription plans and enforcement logic. Never
   change it after a limit key enters production.

2. **Limit keys define types only — not values.** They do not store numbers. Values are assigned per plan (e.g. Basic =
   5, Pro = 20).

3. **Limit keys are global and reusable.** The same `API_KEYS` key appears in every plan — each plan assigns its own
   value.

4. **Soft delete only.** Records are never physically removed. This preserves audit history and prevents referential
   integrity issues with existing plans.

5. **Use `BOOLEAN` for feature flags.** If a feature is simply enabled or disabled per plan, set `data_type = BOOLEAN`.
   The plan limit value of `1` = enabled, `0` = disabled.

6. **Use `-1` to represent unlimited.** There is no special "unlimited" flag. By convention, a plan limit value of `-1`
   means the gateway skips enforcement for that key.

### Recommended Initial Limit Keys

| Code            | Name                      | Data Type | Category | Unit    |
|-----------------|---------------------------|-----------|----------|---------|
| `API_KEYS`      | API Key Creation Limit    | INTEGER   | RESOURCE | `COUNT` |
| `API_REQUESTS`  | API Request Limit         | INTEGER   | USAGE    | `COUNT` |
| `CRM_ACCOUNTS`  | CRM Account Limit         | INTEGER   | RESOURCE | `COUNT` |
| `WEBHOOK_CALLS` | Webhook Call Limit        | INTEGER   | USAGE    | `COUNT` |
| `EXPORT_CSV`    | CSV Export Feature Access | BOOLEAN   | FEATURE  | `NONE`  |
| `TEAM_MEMBERS`  | Team Member Limit         | INTEGER   | RESOURCE | `COUNT` |
