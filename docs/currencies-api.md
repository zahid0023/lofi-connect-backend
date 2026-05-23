# Currencies API

Base URL: `/api/v1/currencies`

Currencies represent monetary units used across the platform. Read operations are publicly accessible. Write operations (create, update, delete) are restricted to `ADMIN` role only. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                          | Auth  | Description         |
|--------|-------------------------------|-------|---------------------|
| POST   | `/api/v1/currencies`          | ADMIN | Create a currency   |
| GET    | `/api/v1/currencies`          | —     | List all currencies |
| GET    | `/api/v1/currencies/{id}`     | —     | Get a currency      |
| PUT    | `/api/v1/currencies/{id}`     | ADMIN | Update a currency   |
| DELETE | `/api/v1/currencies/{id}`     | ADMIN | Delete a currency   |

---

## Data Model

| Field         | Type   | Required | Constraints           | Description                          |
|---------------|--------|----------|-----------------------|--------------------------------------|
| `id`          | Long   | —        | read-only             | Auto-generated identifier            |
| `code`        | String | Yes      | max 10 chars          | Currency code (e.g., `USD`, `BDT`)   |
| `name`        | String | Yes      | max 100 chars         | Display name (e.g., `US Dollar`)     |
| `description` | String | No       | max 100 chars         | Description of the currency          |
| `symbol`      | String | Yes      | max 10 chars          | Currency symbol (e.g., `$`, `৳`)     |

---

## Create Currency

`POST /api/v1/currencies`

> Requires `ADMIN` role.

### Request Body

```json
{
  "code": "USD",
  "name": "US Dollar",
  "description": "United States Dollar.",
  "symbol": "$"
}
```

### Request Fields

| Field         | Type   | Required | Validation               |
|---------------|--------|----------|--------------------------|
| `code`        | String | Yes      | Not blank, max 10 chars  |
| `name`        | String | Yes      | Not blank, max 100 chars |
| `description` | String | No       | max 100 chars            |
| `symbol`      | String | Yes      | Not blank, max 10 chars  |

> `code` is set at creation time and cannot be changed via update.

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Currency

`GET /api/v1/currencies/{id}`

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the currency  |

### Response `200 OK`

```json
{
  "currency": {
    "id": 1,
    "code": "USD",
    "name": "US Dollar",
    "description": "United States Dollar.",
    "symbol": "$"
  }
}
```

---

## List All Currencies

`GET /api/v1/currencies`

Returns a paginated list of active (non-deleted) currencies.

### Query Parameters

| Parameter  | Type   | Default | Constraints                     | Description              |
|------------|--------|---------|---------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                            | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                          | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `createdAt` | Field to sort by       |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                   | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "USD",
      "name": "US Dollar",
      "description": "United States Dollar.",
      "symbol": "$"
    },
    {
      "id": 2,
      "code": "BDT",
      "name": "Bangladeshi Taka",
      "description": "Bangladesh national currency.",
      "symbol": "৳"
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

---

## Update Currency

`PUT /api/v1/currencies/{id}`

> Requires `ADMIN` role.

Updates `name`, `description`, and `symbol`. `code` is set at creation time and cannot be changed.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the currency |

### Request Body

```json
{
  "name": "US Dollar",
  "description": "Updated description.",
  "symbol": "$"
}
```

### Request Fields

| Field         | Type   | Required | Validation               |
|---------------|--------|----------|--------------------------|
| `name`        | String | Yes      | Not blank, max 100 chars |
| `description` | String | No       | max 100 chars            |
| `symbol`      | String | Yes      | Not blank, max 10 chars  |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Currency

`DELETE /api/v1/currencies/{id}`

> Requires `ADMIN` role.

Soft-deletes the currency. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description        |
|-----------|------|--------------------|
| `id`      | Long | ID of the currency |

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
  "message": "Currency not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                              |
|-------------|----------------------------|----------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field      |
| 403         | `ACCESS_DENIED`            | Non-admin attempting a write operation             |
| 404         | `ENTITY_NOT_FOUND`         | Currency not found, or already soft-deleted        |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate `code`)       |
