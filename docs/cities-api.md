# Cities API

Base URL: `/api/v1/countries/{country-id}/cities`

Cities are scoped under a parent country. All endpoints require a valid `country-id` path parameter. All records support
soft-delete — deleted records are hidden from all responses.

---

## Endpoints

| Method | Path                                         | Description     |
|--------|----------------------------------------------|-----------------|
| POST   | `/api/v1/countries/{country-id}/cities`      | Create a city   |
| GET    | `/api/v1/countries/{country-id}/cities`      | List all cities |
| GET    | `/api/v1/countries/{country-id}/cities/{id}` | Get a city      |
| PUT    | `/api/v1/countries/{country-id}/cities/{id}` | Update a city   |
| DELETE | `/api/v1/countries/{country-id}/cities/{id}` | Delete a city   |

---

## Data Model

| Field         | Type    | Required | Constraints           | Description                    |
|---------------|---------|----------|-----------------------|--------------------------------|
| `id`          | Long    | —        | read-only             | Auto-generated identifier      |
| `code`        | String  | No       | max 50 chars          | City code (e.g., `DHK`, `NYC`) |
| `name`        | String  | Yes      | max 255 chars         | Display name of the city       |
| `description` | String  | No       | unlimited             | Description of the city        |
| `sort_order`  | Integer | Yes      | not null, default `0` | Display order                  |

---

## Create City

`POST /api/v1/countries/{country-id}/cities`

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |

### Request Body

```json
{
  "code": "DHK",
  "name": "Dhaka",
  "description": "Capital city of Bangladesh.",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
| `code`        | String  | No       | max 50 chars             |
| `name`        | String  | Yes      | Not blank, max 255 chars |
| `description` | String  | No       | —                        |
| `sort_order`  | Integer | Yes      | Not null                 |

> `code` is set at creation time and cannot be changed via update.

### Response `201 Created`

```json
{
  "success": true,
  "id": 1
}
```

---

## Get City

`GET /api/v1/countries/{country-id}/cities/{id}`

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |
| `id`         | Long | ID of the city    |

### Response `200 OK`

```json
{
  "city": {
    "id": 1,
    "country_id": 5,
    "code": "DHK",
    "name": "Dhaka",
    "description": "Capital city of Bangladesh.",
    "sort_order": 1
  }
}
```

---

## List All Cities

`GET /api/v1/countries/{country-id}/cities`

Returns a paginated list of active (non-deleted) cities belonging to the specified country.

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |

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
      "code": "DHK",
      "name": "Dhaka",
      "description": "Capital city of Bangladesh.",
      "sort_order": 1
    },
    {
      "id": 2,
      "code": "CTG",
      "name": "Chittagong",
      "description": "Port city of Bangladesh.",
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

## Update City

`PUT /api/v1/countries/{country-id}/cities/{id}`

Updates `name`, `description`, and `sort_order`. `code` is set at creation time and cannot be changed.

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |
| `id`         | Long | ID of the city    |

### Request Body

```json
{
  "name": "Dhaka",
  "description": "Updated description.",
  "sort_order": 1
}
```

### Request Fields

| Field         | Type    | Required | Validation               |
|---------------|---------|----------|--------------------------|
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

## Delete City

`DELETE /api/v1/countries/{country-id}/cities/{id}`

Soft-deletes the city. The record is not removed from the database but will no longer appear in any response.

### Path Parameters

| Parameter    | Type | Description       |
|--------------|------|-------------------|
| `country-id` | Long | ID of the country |
| `id`         | Long | ID of the city    |

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
  "message": "City not found with id: 99"
}
```

| HTTP Status | Error Code                 | Cause                                                      |
|-------------|----------------------------|------------------------------------------------------------|
| 400         | `INVALID_ARGUMENT`         | Missing required fields or invalid sort field              |
| 404         | `ENTITY_NOT_FOUND`         | Country not found, city not found, or already soft-deleted |
| 409         | `DATA_INTEGRITY_VIOLATION` | Constraint violation                                       |
