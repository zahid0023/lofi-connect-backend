# API Usage — By Connection (Per App Key)

**Endpoint:** `GET /api/v1/usage-logs/by-connection/me`
**Auth:** `Authorization: Bearer <access_token>` (JWT)
**Access:** Any authenticated user (scoped to the caller's own app keys).

---

Returns **usage stats broken down per app key**, each annotated with its live CRM connection
details (GHL agency or sub-account). This is the "usage by connection" view — every app key
the user owns appears in the response, regardless of whether it has activity or a connection.

Use this to drive a per-connection usage table or chart on the dashboard.

---

## Query Parameters

| Parameter | Type    | Required | Default | Allowed values | Description                                                             |
|-----------|---------|----------|---------|----------------|-------------------------------------------------------------------------|
| `range`   | integer | No       | `7`     | `1`, `7`, `30` | Look-back window in **days**. Any other value silently defaults to `7`. |

---

## Response `200 OK`

```json
{
  "range_days": 7,
  "data": [
    {
      "app_key_id": 1,
      "app_key_name": "Main Agency Key",
      "connected": true,
      "connection": {
        "platform": "GHL",
        "company_id": "abc123xyz",
        "subaccount_name": "Acme Marketing Agency",
        "location_id": "loc_0000",
        "user_type": "Agency"
      },
      "total_calls": 127,
      "errors": 11,
      "error_rate": 8.66,
      "avg_response_time_ms": 243.5
    },
    {
      "app_key_id": 2,
      "app_key_name": "Sub-Account Key",
      "connected": true,
      "connection": {
        "platform": "GHL",
        "company_id": "abc123xyz",
        "subaccount_name": "Client — Smith Dental",
        "location_id": "loc_9911",
        "user_type": "Location"
      },
      "total_calls": 54,
      "errors": 2,
      "error_rate": 3.7,
      "avg_response_time_ms": 180.0
    },
    {
      "app_key_id": 3,
      "app_key_name": "Unconnected Key",
      "connected": false,
      "connection": null,
      "total_calls": 0,
      "errors": 0,
      "error_rate": 0.0,
      "avg_response_time_ms": null
    }
  ]
}
```

---

## Response Body Fields

### Top-level (`UsageByConnectionResponse`)

| Field        | Type                           | Description                                     |
|--------------|--------------------------------|-------------------------------------------------|
| `range_days` | integer                        | The effective range used (1, 7, or 30)          |
| `data`       | array of `ConnectionUsageItem` | One entry per app key, ordered by creation date |

---

### `ConnectionUsageItem`

| Field                  | Type                     | Description                                                                                    |
|------------------------|--------------------------|------------------------------------------------------------------------------------------------|
| `app_key_id`           | integer                  | ID of the app key                                                                              |
| `app_key_name`         | string                   | Human-readable name given to this app key                                                      |
| `connected`            | boolean                  | `true` if this app key has an active GHL connection; `false` if not yet connected              |
| `connection`           | `ConnectionInfo` \| null | Connection details. `null` when `connected: false`                                             |
| `total_calls`          | integer                  | Total GHL API calls made via this key in the range                                             |
| `errors`               | integer                  | Count of calls where `is_error = true` (upstream 4xx / 5xx)                                    |
| `error_rate`           | double                   | `errors / total_calls × 100`, rounded to 2 dp. `0.0` when `total_calls = 0`                    |
| `avg_response_time_ms` | double \| null           | Average upstream response time in milliseconds, rounded to 2 dp. `null` when `total_calls = 0` |

---

### `ConnectionInfo`

Populated only when `connected: true`. Describes the active GHL token linked to this app key.

| Field             | Type   | Description                                                                                                |
|-------------------|--------|------------------------------------------------------------------------------------------------------------|
| `platform`        | string | CRM platform name. Currently always `"GHL"` (GoHighLevel)                                                  |
| `company_id`      | string | GHL agency company ID — identifies the top-level agency account                                            |
| `subaccount_name` | string | Human-readable name of the connected GHL sub-account or agency                                             |
| `location_id`     | string | GHL location ID. `"0"` for agency-level tokens; sub-account ID for location tokens                         |
| `user_type`       | string | `"Agency"` — connected at the agency (main account) level. `"Location"` — connected at a sub-account level |

---

## `user_type` values

| Value        | Meaning                                                                 |
|--------------|-------------------------------------------------------------------------|
| `"Agency"`   | The app key is connected to the **main agency account** (company-level) |
| `"Location"` | The app key is connected to a specific **sub-account / location**       |

---

## Key behaviours

### App keys with no activity

App keys that have zero calls in the selected range are always included with `total_calls: 0`,
`errors: 0`, `error_rate: 0.0`, and `avg_response_time_ms: null`.

### App keys with no connection

App keys that have never been assigned a GHL token appear with `connected: false` and
`connection: null`. Their usage stats (if any) are still reported normally.

### No app keys at all

If the user has no active app keys, the response is:

```json
{
  "range_days": 7,
  "data": []
}
```

---

## Examples

### `range=1` — last 24 hours

```
GET /api/v1/usage-logs/by-connection/me?range=1
```

```json
{
  "range_days": 1,
  "data": [
    {
      "app_key_id": 1,
      "app_key_name": "Main Agency Key",
      "connected": true,
      "connection": {
        "platform": "GHL",
        "company_id": "abc123xyz",
        "subaccount_name": "Acme Marketing Agency",
        "location_id": "loc_0000",
        "user_type": "Agency"
      },
      "total_calls": 22,
      "errors": 1,
      "error_rate": 4.55,
      "avg_response_time_ms": 198.3
    }
  ]
}
```

### `range=30` — last 30 days

```
GET /api/v1/usage-logs/by-connection/me?range=30
```

Response shape is identical — each item covers the full 30-day window aggregated.

---

## Invalid `range` handling

| Supplied `range` | Effective `range`    |
|------------------|----------------------|
| `1`              | `1`                  |
| `7`              | `7`                  |
| `30`             | `30`                 |
| Any other value  | `7` (silent default) |

---

## Error Responses

| HTTP Status        | `error_code`     | Cause                               |
|--------------------|------------------|-------------------------------------|
| `401 Unauthorized` | `UNAUTHORIZED`   | Missing or invalid JWT access token |
| `403 Forbidden`    | `FORBIDDEN`      | Authenticated but not authorized    |
| `500 Internal`     | `INTERNAL_ERROR` | Unexpected server error             |

---

## Related Endpoints

| Endpoint                          | Description                                          |
|-----------------------------------|------------------------------------------------------|
| `GET /api/v1/usage-logs/daily/me` | Per-day breakdown of calls and errors over a range   |
| `GET /api/v1/usage-logs/stats/me` | 4-card KPI summary with % change vs previous period  |
| `GET /api/v1/usage-logs/me`       | Paginated raw log list for the caller's own app keys |
