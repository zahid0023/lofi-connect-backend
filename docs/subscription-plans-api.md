# Subscription Plans API

Base URL: `/api/v1/subscription-plans`

Subscription Plans define the pricing tiers available on the platform. Each plan includes a currency, pricing, billing cycle, and a set of resource limits tied to limit keys. Read operations are publicly accessible. Write operations (create, update, delete) are restricted to `ADMIN` role only. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                 | Auth  | Description                  |
|--------|--------------------------------------|-------|------------------------------|
| POST   | `/api/v1/subscription-plans`         | ADMIN | Create a subscription plan   |
| GET    | `/api/v1/subscription-plans`         | —     | List all subscription plans  |
| GET    | `/api/v1/subscription-plans/{id}`    | —     | Get a subscription plan      |
| PUT    | `/api/v1/subscription-plans/{id}`    | ADMIN | Update a subscription plan   |
| DELETE | `/api/v1/subscription-plans/{id}`    | ADMIN | Delete a subscription plan   |

---

## Data Model

### Plan Fields

| Field              | Type          | Required | Constraints          | Description                                        |
|--------------------|---------------|----------|----------------------|----------------------------------------------------|
| `id`               | Long          | —        | read-only            | Auto-generated identifier                          |
| `code`             | String        | Yes      | max 100 chars        | Unique machine-readable plan code (e.g., `BASIC`)  |
| `name`             | String        | Yes      | max 100 chars        | Display name (e.g., `Basic Plan`)                  |
| `price`            | Decimal(10,2) | No       | default `0`          | Plan price                                         |
| `description`      | String[]      | Yes      | array of strings     | List of plan features or bullet points             |
| `billing_cycle`    | String        | Yes      | max 20 chars         | Billing frequency (e.g., `MONTHLY`, `YEARLY`)      |
| `duration_in_days` | Integer       | Yes      | not null, default 30 | Plan validity in days                              |
| `sort_order`       | Integer       | No       | default `0`          | Display order                                      |
| `currency_id`      | Long          | Yes      | not null             | ID of the associated currency                      |
| `limits`           | Object[]      | No       | —                    | Resource limits attached to the plan               |

### Limit Object (in request)

| Field           | Type | Required | Description                  |
|-----------------|------|----------|------------------------------|
| `limit_key_id`  | Long | Yes      | ID of the limit key          |
| `limit_value`   | Long | Yes      | Value for the limit          |

---

## Create Subscription Plan

`POST /api/v1/subscription-plans`

> Requires `ADMIN` role.

### Request Body

```json
{
  "code": "BASIC",
  "name": "Basic Plan",
  "currency_id": 1,
  "price": 29.99,
  "description": [
    "Up to 500 contacts",
    "5 campaigns per month",
    "Email support"
  ],
  "billing_cycle": "MONTHLY",
  "duration_in_days": 30,
  "sort_order": 1,
  "limits": [
    { "limit_key_id": 1, "limit_value": 500 },
    { "limit_key_id": 2, "limit_value": 5 }
  ]
}
```

### Request Fields

| Field              | Type          | Required | Validation               |
|--------------------|---------------|----------|--------------------------|
| `code`             | String        | Yes      | max 100 chars            |
| `currency_id`      | Long          | Yes      | Not null                 |
| `name`             | String        | Yes      | Not blank, max 100 chars |
| `price`            | Decimal       | No       | —                        |
| `description`      | String[]      | Yes      | Not null                 |
| `billing_cycle`    | String        | Yes      | Not blank, max 20 chars  |
| `duration_in_days` | Integer       | Yes      | Not null                 |
| `sort_order`       | Integer       | No       | —                        |
| `limits`           | Object[]      | No       | —                        |

> `code` is set at creation time and cannot be changed via update.

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Subscription Plan

`GET /api/v1/subscription-plans/{id}`

Returns the full plan including currency details and all attached limits.

### Path Parameters

| Parameter | Type | Description                  |
|-----------|------|------------------------------|
| `id`      | Long | ID of the subscription plan  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "name": "Basic Plan",
    "price": 29.99,
    "description": [
      "Up to 500 contacts",
      "5 campaigns per month",
      "Email support"
    ],
    "billing_cycle": "MONTHLY",
    "duration_in_days": 30,
    "currency": {
      "id": 1,
      "code": "USD",
      "name": "US Dollar",
      "description": "United States Dollar.",
      "symbol": "$"
    },
    "limits": [
      {
        "id": 1,
        "limit_key": {
          "id": 1,
          "code": "MAX_CONTACTS",
          "name": "Max Contacts",
          "description": "Maximum number of contacts allowed.",
          "data_type": "INTEGER",
          "category": "CRM",
          "unit": "contacts"
        },
        "limit_value": 500
      },
      {
        "id": 2,
        "limit_key": {
          "id": 2,
          "code": "MAX_CAMPAIGNS",
          "name": "Max Campaigns",
          "description": "Maximum number of campaigns per month.",
          "data_type": "INTEGER",
          "category": "MARKETING",
          "unit": "campaigns"
        },
        "limit_value": 5
      }
    ]
  }
}
```

---

## List All Subscription Plans

`GET /api/v1/subscription-plans`

Returns a paginated summary list of active (non-deleted) subscription plans.

### Query Parameters

| Parameter  | Type   | Default | Constraints                                                        | Description              |
|------------|--------|---------|--------------------------------------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                                               | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                                                             | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `name`, `price`, `billingCycle`, `durationInDays`, `createdAt` | Field to sort by       |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                                      | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "name": "Basic Plan",
      "price": 29.99,
      "billing_cycle": "MONTHLY",
      "duration_in_days": 30
    },
    {
      "id": 2,
      "name": "Pro Plan",
      "price": 79.99,
      "billing_cycle": "MONTHLY",
      "duration_in_days": 30
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

> The list response returns a summary only. Use `GET /api/v1/subscription-plans/{id}` to retrieve the full plan with currency and limits.

---

## Update Subscription Plan

`PUT /api/v1/subscription-plans/{id}`

> Requires `ADMIN` role.

Replaces all plan fields and limits. `code` is set at creation time and cannot be changed. Existing limits are deleted and replaced with the new set.

### Path Parameters

| Parameter | Type | Description                 |
|-----------|------|-----------------------------|
| `id`      | Long | ID of the subscription plan |

### Request Body

```json
{
  "currency_id": 1,
  "name": "Basic Plan",
  "price": 34.99,
  "description": [
    "Up to 500 contacts",
    "5 campaigns per month",
    "Priority email support"
  ],
  "billing_cycle": "MONTHLY",
  "duration_in_days": 30,
  "sort_order": 1,
  "limits": [
    { "limit_key_id": 1, "limit_value": 500 },
    { "limit_key_id": 2, "limit_value": 5 }
  ]
}
```

### Request Fields

| Field              | Type     | Required | Validation               |
|--------------------|----------|----------|--------------------------|
| `currency_id`      | Long     | Yes      | Not null                 |
| `name`             | String   | Yes      | Not blank, max 100 chars |
| `price`            | Decimal  | No       | —                        |
| `description`      | String[] | Yes      | Not null                 |
| `billing_cycle`    | String   | Yes      | Not blank, max 20 chars  |
| `duration_in_days` | Integer  | Yes      | Not null                 |
| `sort_order`       | Integer  | No       | —                        |
| `limits`           | Object[] | No       | —                        |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Subscription Plan

`DELETE /api/v1/subscription-plans/{id}`

> Requires `ADMIN` role.

Soft-deletes the subscription plan. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description                 |
|-----------|------|-----------------------------|
| `id`      | Long | ID of the subscription plan |

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
  "message": "SubscriptionPlan not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                       |
|-------------|----------------------------|-------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field               |
| 403         | `ACCESS_DENIED`            | Non-admin attempting a write operation                      |
| 404         | `ENTITY_NOT_FOUND`         | Plan not found, currency not found, limit key not found, or already soft-deleted |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate `code`)                |
