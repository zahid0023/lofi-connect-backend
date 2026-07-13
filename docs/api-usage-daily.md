# API Usage — Daily Breakdown

**Endpoint:** `GET /api/v1/usage-logs/daily/me`
**Auth:** `Authorization: Bearer <access_token>` (JWT)
**Access:** Any authenticated user (scoped to the caller's own app keys).

---

Returns a **per-day breakdown** of API calls and errors for the requested time range.
Every calendar day in the range is always present in the response — days with no activity
are returned with `api_calls: 0` and `errors: 0` so the frontend can render a complete chart
without extra null-handling.

All dates are calculated in **UTC**.

---

## Query Parameters

| Parameter | Type    | Required | Default | Allowed values | Description                                                             |
|-----------|---------|----------|---------|----------------|-------------------------------------------------------------------------|
| `range`   | integer | No       | `7`     | `1`, `7`, `30` | Look-back window in **days**. Any other value silently defaults to `7`. |

| `range` | What it covers                       | Data points returned |
|---------|--------------------------------------|----------------------|
| `1`     | Today only (UTC midnight → now)      | 1                    |
| `7`     | Last 7 calendar days (oldest first)  | 7                    |
| `30`    | Last 30 calendar days (oldest first) | 30                   |

---

## Response `200 OK`

```json
{
  "range_days": 7,
  "data": [
    {
      "date": "Jun 7",
      "api_calls": 54,
      "errors": 3
    },
    {
      "date": "Jun 8",
      "api_calls": 118,
      "errors": 9
    },
    {
      "date": "Jun 9",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "Jun 10",
      "api_calls": 76,
      "errors": 2
    },
    {
      "date": "Jun 11",
      "api_calls": 201,
      "errors": 18
    },
    {
      "date": "Jun 12",
      "api_calls": 93,
      "errors": 5
    },
    {
      "date": "Jun 13",
      "api_calls": 127,
      "errors": 11
    }
  ]
}
```

---

## Response Body Fields

### Top-level (`DailyUsageResponse`)

| Field        | Type                       | Description                                        |
|--------------|----------------------------|----------------------------------------------------|
| `range_days` | integer                    | The effective range used (1, 7, or 30)             |
| `data`       | array of `DailyUsagePoint` | One entry per calendar day, sorted oldest → newest |

### `DailyUsagePoint`

| Field       | Type    | Description                                                                          |
|-------------|---------|--------------------------------------------------------------------------------------|
| `date`      | string  | Human-readable label in `"MMM d"` format (e.g. `"Jun 14"`, `"Dec 3"`)                |
| `api_calls` | integer | Total number of GHL API calls made on this day across **all** of the user's app keys |
| `errors`    | integer | Number of those calls where `is_error = true` (HTTP 4xx/5xx from upstream)           |

---

## Examples

### 24-hour view (`range=1`)

```
GET /api/v1/usage-logs/daily/me?range=1
```

```json
{
  "range_days": 1,
  "data": [
    {
      "date": "Jun 13",
      "api_calls": 127,
      "errors": 11
    }
  ]
}
```

---

### 7-day view (`range=7`, default)

```
GET /api/v1/usage-logs/daily/me
GET /api/v1/usage-logs/daily/me?range=7
```

```json
{
  "range_days": 7,
  "data": [
    {
      "date": "Jun 7",
      "api_calls": 54,
      "errors": 3
    },
    {
      "date": "Jun 8",
      "api_calls": 118,
      "errors": 9
    },
    {
      "date": "Jun 9",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "Jun 10",
      "api_calls": 76,
      "errors": 2
    },
    {
      "date": "Jun 11",
      "api_calls": 201,
      "errors": 18
    },
    {
      "date": "Jun 12",
      "api_calls": 93,
      "errors": 5
    },
    {
      "date": "Jun 13",
      "api_calls": 127,
      "errors": 11
    }
  ]
}
```

---

### 30-day view (`range=30`)

```
GET /api/v1/usage-logs/daily/me?range=30
```

```json
{
  "range_days": 30,
  "data": [
    {
      "date": "May 15",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "May 16",
      "api_calls": 34,
      "errors": 1
    },
    {
      "date": "May 17",
      "api_calls": 87,
      "errors": 6
    },
    ...
    {
      "date": "Jun 13",
      "api_calls": 127,
      "errors": 11
    }
  ]
}
```

---

## User with no app keys

If the authenticated user has no active app keys, every day in the range is returned with
zeroes — the structure is always identical.

```json
{
  "range_days": 7,
  "data": [
    {
      "date": "Jun 7",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "Jun 8",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "Jun 9",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "Jun 10",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "Jun 11",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "Jun 12",
      "api_calls": 0,
      "errors": 0
    },
    {
      "date": "Jun 13",
      "api_calls": 0,
      "errors": 0
    }
  ]
}
```

---

## Deriving the error rate per day (frontend)

The response intentionally omits a pre-calculated `error_rate` field so the frontend can
compute it flexibly. Use:

```
error_rate = api_calls > 0 ? (errors / api_calls) * 100 : 0
```

Example for `{ "api_calls": 127, "errors": 11 }`:

```
error_rate = (11 / 127) * 100 = 8.66 %
```

---

## Data scope

- Counts calls in `api_usage_logs` where `app_key_id` belongs to the authenticated user
  (active, non-deleted app keys only).
- The time window is `[now − range days, now]` as UTC instants.
- Each day bucket corresponds to a UTC calendar day (`EXTRACT(DAY FROM requested_at)`).
- Days are ordered **oldest to newest** — index `0` is always the earliest day in the range.

---

## Invalid `range` handling

| Supplied `range` | Effective `range`    |
|------------------|----------------------|
| `1`              | `1`                  |
| `7`              | `7`                  |
| `30`             | `30`                 |
| Any other value  | `7` (silent default) |

No error is returned for an unsupported range — the API silently falls back to `7`.

---

## Error Responses

| HTTP Status        | `error_code`     | Cause                                   |
|--------------------|------------------|-----------------------------------------|
| `401 Unauthorized` | `UNAUTHORIZED`   | Missing or invalid JWT access token     |
| `403 Forbidden`    | `FORBIDDEN`      | Authenticated but not authorized (role) |
| `500 Internal`     | `INTERNAL_ERROR` | Unexpected server error                 |

---

## Related Endpoints

| Endpoint                          | Description                                                    |
|-----------------------------------|----------------------------------------------------------------|
| `GET /api/v1/usage-logs/stats/me` | 4-card KPI summary (totals + % change vs previous period)      |
| `GET /api/v1/usage-logs/me`       | Paginated raw log list for the caller's own app keys           |
| `GET /api/v1/usage-logs`          | Admin: paginated log list across all tenants (ADMIN role only) |
