# Countries API

Base URL: `/api/v1/countries`

Countries represent geographic countries used across the platform. All records support soft-delete — deleted records are
hidden from all responses.

---

## Authentication

All endpoints require a valid JWT bearer token.

```
Authorization: Bearer <token>
```

Requests without a valid token receive `401 Unauthorized`.

---

## Endpoints

| Method | Path                     | Description        |
|--------|--------------------------|--------------------|
| POST   | `/api/v1/countries`      | Create a country   |
| GET    | `/api/v1/countries`      | List all countries |
| GET    | `/api/v1/countries/{id}` | Get a country      |
| PUT    | `/api/v1/countries/{id}` | Update a country   |
| DELETE | `/api/v1/countries/{id}` | Delete a country   |

---

## Data Model

| Field         | Type    | Required | Constraints              | Description                                  |
|---------------|---------|----------|--------------------------|----------------------------------------------|
| `id`          | Long    | —        | read-only                | Auto-generated identifier                    |
| `code`        | String  | Yes      | max 10 chars             | ISO 3166-1 alpha-2 code (e.g., `BD`, `US`)   |
| `iso3_code`   | String  | No       | max 10 chars             | ISO 3166-1 alpha-3 code (e.g., `BGD`, `USA`) |
| `phone_code`  | String  | No       | max 10 chars             | International dialing code (e.g., `+880`)    |
| `name`        | String  | Yes      | not blank, max 255 chars | Display name of the country                  |
| `description` | String  | No       | unlimited                | Description of the country                   |
| `sort_order`  | Integer | Yes      | not null, default `0`    | Display order                                |

---

## Create Country

`POST /api/v1/countries`

### Request Body

```json
{
  "code": "BD",
  "iso3_code": "BGD",
  "phone_code": "+880",
  "name": "Bangladesh",
  "description": "A country in South Asia.",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `code`        | String  | Yes      | Not blank, max 10 chars  |
| `iso3_code`   | String  | No       | max 10 chars             |
| `phone_code`  | String  | No       | max 10 chars             |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get Country

`GET /api/v1/countries/{id}`

### Path Parameters

| Parameter | Type | Description       |
|-----------|------|-------------------|
| `id`      | Long | ID of the country |

### Response `200 OK`

```json
{
  "country": {
    "id": 1,
    "code": "BD",
    "iso3_code": "BGD",
    "phone_code": "+880",
    "name": "Bangladesh",
    "description": "A country in South Asia.",
    "sort_order": 1
  }
}
```

---

## List All Countries

`GET /api/v1/countries`

Returns a paginated list of active (non-deleted) countries.

### Query Parameters

| Parameter  | Type   | Default | Constraints                                    | Description              |
|------------|--------|---------|------------------------------------------------|--------------------------|
| `page`     | int    | `0`     | >= 0                                           | Zero-based page index    |
| `size`     | int    | `10`    | 1 – 50                                         | Number of items per page |
| `sort_by`  | String | `id`    | `id`, `code`, `name`, `sortOrder`, `createdAt` | Field to sort by         |
| `sort_dir` | String | `ASC`   | `ASC`, `DESC`                                  | Sort direction           |

### Response `200 OK`

```json
{
  "data": [
    {
      "id": 1,
      "code": "BD",
      "iso3_code": "BGD",
      "phone_code": "+880",
      "name": "Bangladesh",
      "description": "A country in South Asia.",
      "sort_order": 1
    },
    {
      "id": 2,
      "code": "US",
      "iso3_code": "USA",
      "phone_code": "+1",
      "name": "United States",
      "description": "A country in North America.",
      "sort_order": 2
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

## Update Country

`PUT /api/v1/countries/{id}`

Updates all fields except `code`, which is set at creation time and cannot be changed.

### Path Parameters

| Parameter | Type | Description       |
|-----------|------|-------------------|
| `id`      | Long | ID of the country |

### Request Body

```json
{
  "name": "Bangladesh",
  "iso3_code": "BGD",
  "phone_code": "+880",
  "description": "Updated description.",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `iso3_code`   | String  | No       | max 10 chars             |
| `phone_code`  | String  | No       | max 10 chars             |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

### Response `200 OK`

```json
{
  "success": true,
  "id": 1
}
```

---

## Delete Country

`DELETE /api/v1/countries/{id}`

Soft-deletes the country. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter | Type | Description       |
|-----------|------|-------------------|
| `id`      | Long | ID of the country |

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
  "message": "Country not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                         |
|-------------|----------------------------|-----------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field |
| 401         | `UNAUTHORIZED`             | Missing or invalid JWT token                  |
| 404         | `ENTITY_NOT_FOUND`         | Country not found, or already soft-deleted    |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation (e.g. duplicate `code`)  |
