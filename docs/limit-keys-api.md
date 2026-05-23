~~# Limit Keys API

Base URL: `/api/v1/limit-keys`

Limit Keys define the measurable resource limits used in subscription plans (e.g., number of contacts, campaigns, users). Read operations are publicly accessible. Write operations (create, update, delete) are restricted to `ADMIN` role only. All records support soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                        | Auth  | Description           |
|--------|-----------------------------|-------|-----------------------|
| POST   | `/api/v1/limit-keys`        | ADMIN | Create a limit key    |
| GET    | `/api/v1/limit-keys`        | —     | List all limit keys   |
| GET    | `/api/v1/limit-keys/{id}`   | —     | Get a limit key       |
| PUT    | `/api/v1/limit-keys/{id}`   | ADMIN | Update a limit key    |
| DELETE | `/api/v1/limit-keys/{id}`   | ADMIN | Delete a limit key    |

---

## Data Model

| Field         | Type   | Required | Constraints  | Description                                          |
|---------------|--------|----------|--------------|------------------------------------------------------|
| `id`          | Long   | —        | read-only    | Auto-generated identifier                            |
| `code`        | String | Yes      | max 100 chars | Unique machine-readable key (e.g., `MAX_CONTACTS`)  |
| `name`        | String | Yes      | max 150 chars | Human-readable name (e.g., `Max Contacts`)          |
| `description` | String | No       | unlimited    | Description of what the limit controls               |
| `data_type`   | String | Yes      | max 50 chars | Value type for the limit (e.g., `INTEGER`, `BOOLEAN`) |
| `category`    | String | Yes      | max 50 chars | Grouping category (e.g., `CRM`, `MARKETING`)        |
| `unit`        | String | No       | max 50 chars | Unit of measurement (e.g., `contacts`, `emails/mo`) |

---

## Create Limit Key

`POST /api/v1/limit-keys`

> Requires `ADMIN` role.

### Request Body

```json
{
  "code": "MAX_CONTACTS",
  "name": "Max Contacts",
  "description": "Maximum number of contacts allowed.",
  "data_type": "INTEGER",
  "category": "CRM",
  "unit": "contacts"
}
```

### Request Fields

| Field         | Type   | Required | Validation                |
|---------------|--------|----------|---------------------------|
| `code`        | String | Yes      | Not blank, max 100 chars  |
| `name`        | String | Yes      | Not blank, max 150 chars  |
| `description` | String | No       | —                         |
| `data_type`   | String | Yes      | Not blank, max 50 chars   |
| `category`    | String | Yes      | Not blank, max 50 chars   |
| `unit`        | String | No       | max 50 chars              |

> `code` is set at creation time and cannot be changed via update.

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Limit Key

`GET /api/v1/limit-keys/{id}`

### Path Parameters

| Parameter | Type | Description          |
|-----------|------|----------------------|
| `id`      | Long | ID of the limit key  |

### Response `200 OK`

```json
{
  "data": {
    "id": 1,
    "code": "MAX_CONTACTS",
    "name": "Max Contacts",
    "description": "Maximum number of contacts allowed.",
    "data_type": "INTEGER",
    "category": "CRM",
    "unit": "contacts"
  }
}
```

---

## List All Limit Keys

`GET /api/v1/limit-keys`

Returns a paginated list of active (non-deleted) limit keys.

### Query Parameters

| Parameter  | Type   | Default | Constraints                                              | Description              |
|------------|--------|---------|----------------------------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                                     | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                                                   | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `category`, `dataType`, `createdAt` | Field to sort by        |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                            | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "MAX_CONTACTS",
      "name": "Max Contacts",
      "data_type": "INTEGER",
      "category": "CRM",
      "unit": "contacts"
    },
    {
      "id": 2,
      "code": "MAX_CAMPAIGNS",
      "name": "Max Campaigns",
      "data_type": "INTEGER",
      "category": "MARKETING",
      "unit": "campaigns"
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

> The list response omits `description` for brevity. Use `GET /api/v1/limit-keys/{id}` to retrieve the full record.

---

## Update Limit Key

`PUT /api/v1/limit-keys/{id}`

> Requires `ADMIN` role.

Updates all fields except `code`, which is set at creation time and cannot be changed.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the limit key |

### Request Body

```json
{
  "name": "Max Contacts",
  "description": "Updated description.",
  "data_type": "INTEGER",
  "category": "CRM",
  "unit": "contacts"
}
```

### Request Fields

| Field         | Type   | Required | Validation               |
|---------------|--------|----------|--------------------------|
| `name`        | String | Yes      | Not blank, max 150 chars |
| `description` | String | No       | —                        |
| `data_type`   | String | Yes      | Not blank, max 50 chars  |
| `category`    | String | Yes      | Not blank, max 50 chars  |
| `unit`        | String | No       | max 50 chars             |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Limit Key

`DELETE /api/v1/limit-keys/{id}`

> Requires `ADMIN` role.

Soft-deletes the limit key. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description         |
|-----------|------|---------------------|
| `id`      | Long | ID of the limit key |

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

| HTTP Status | Error Code                 | Cause                                              |
|-------------|----------------------------|----------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field      |
| 403         | `ACCESS_DENIED`            | Non-admin attempting a write operation             |
| 404         | `ENTITY_NOT_FOUND`         | Limit key not found, or already soft-deleted       |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate `code`)       |~~
