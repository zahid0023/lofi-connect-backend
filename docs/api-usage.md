# API Usage Documentation

**Base URL:** `https://{host}/api/v1`
**Auth:** All endpoints require `Authorization: Bearer <access_token>` (JWT)

---

## Endpoints

| Method | Path | Role | Description |
|---|---|---|---|
| GET | `/usage/summary` | User | Full usage page — stats, charts, breakdowns |
| GET | `/usage/me` | User | Paginated raw call history |
| GET | `/admin/usage` | Admin | All users' call history |
| GET | `/admin/usage/users/{userId}` | Admin | Specific user's call history |

---

## GET `/usage/summary`

Returns the full usage page for the authenticated user. Covers:
- Metric cards with month-over-month comparison (current month vs previous month)
- 30-day daily chart data
- Per-GHL-connection breakdown
- Top endpoints by call volume

**Auth:** JWT (user)

**Response `200 OK`:**

```json
{
  "period_start": "2026-07-01T00:00:00Z",
  "period_end": "2026-07-04T14:32:00Z",

  "total_api_calls": 1240,
  "total_api_calls_last_month": 980,
  "total_api_calls_change_percent": 26.5,
  "total_api_calls_trend": "UP",

  "success_rate": 98.3,
  "success_rate_last_month": 97.1,
  "success_rate_change_pct": 1.2,
  "success_rate_trend": "UP",

  "error_count": 21,
  "error_count_last_month": 28,
  "error_count_change_percent": -25.0,
  "error_count_trend": "DOWN",

  "avg_calls_per_day": 310.0,

  "monthly_operation_limit": 5000,
  "usage_percentage": 24.8,

  "connected_crm_accounts": 3,

  "api_calls_over_time": [
    {
      "date": "2026-06-05",
      "total_calls": 120,
      "error_calls": 3,
      "success_calls": 117
    },
    {
      "date": "2026-06-06",
      "total_calls": 95,
      "error_calls": 1,
      "success_calls": 94
    }
  ],

  "usage_by_connection": [
    {
      "app_key_id": 7,
      "location_id": "abc123xyz",
      "subaccount_name": "Acme Marketing",
      "call_count": 820
    },
    {
      "app_key_id": 9,
      "location_id": "def456uvw",
      "subaccount_name": "Beta Agency",
      "call_count": 420
    }
  ],

  "top_endpoints": [
    {
      "http_method": "GET",
      "endpoint": "/api/v1/ghl/contacts/abc123",
      "call_count": 340,
      "avg_response_time_ms": 185.4
    },
    {
      "http_method": "POST",
      "endpoint": "/api/v1/ghl/contacts",
      "call_count": 210,
      "avg_response_time_ms": 240.1
    }
  ]
}
```

---

### Response Fields

#### Period

| Field | Type | Description |
|---|---|---|
| `period_start` | ISO-8601 | First instant of the current calendar month (UTC) |
| `period_end` | ISO-8601 | Current instant (end of reporting window) |

---

#### Total API Calls

| Field | Type | Description |
|---|---|---|
| `total_api_calls` | Long | Total GHL API calls made this month |
| `total_api_calls_last_month` | Long | Same count for last month |
| `total_api_calls_change_percent` | Double \| null | `((this - last) / last) × 100`, 1 decimal. `null` if last month = 0 |
| `total_api_calls_trend` | String | `"UP"`, `"DOWN"`, or `"UNCHANGED"` |

**Frontend note:** Display `total_api_calls_change_percent` as a badge next to the card total:
- Green arrow up → `"UP"`
- Red arrow down → `"DOWN"`
- Dash → `"UNCHANGED"` or `null`

---

#### Success Rate

| Field | Type | Description |
|---|---|---|
| `success_rate` | Double | `(total - errors) / total × 100`, 1 decimal. `100.0` when no calls |
| `success_rate_last_month` | Double | Same for last month |
| `success_rate_change_pct` | Double | Absolute percentage-point change (e.g. `+1.2` pp). NOT a relative % |
| `success_rate_trend` | String | `"UP"`, `"DOWN"`, or `"UNCHANGED"` |

**Example note to show:** `"+1.2pp from last month"` (use `success_rate_change_pct`)

---

#### Errors

| Field | Type | Description |
|---|---|---|
| `error_count` | Long | HTTP 4xx + 5xx calls this month |
| `error_count_last_month` | Long | Same for last month |
| `error_count_change_percent` | Double \| null | Percentage change. `null` if last month had 0 errors |
| `error_count_trend` | String | `"UP"`, `"DOWN"`, or `"UNCHANGED"` |

**Frontend note:** For errors, `"DOWN"` is good (fewer errors). Show green for DOWN, red for UP.

---

#### Averages & Limits

| Field | Type | Description |
|---|---|---|
| `avg_calls_per_day` | Double | `total_api_calls / days_elapsed_this_month`, 1 decimal |
| `monthly_operation_limit` | Long \| null | Plan cap. `null` = unlimited plan |
| `usage_percentage` | Double \| null | `total / limit × 100`, 1 decimal. `null` when unlimited |
| `connected_crm_accounts` | Long | Active GHL subaccounts linked to this user's app keys |

**Frontend note:** When `monthly_operation_limit` is `null`, show `"Unlimited"` instead of a progress bar.

---

#### API Calls Over Time (`api_calls_over_time`)

Daily breakdown for the **last 30 days**. Only days that have at least one call are included — fill missing days with zero on the frontend.

| Field | Type | Description |
|---|---|---|
| `date` | String | Calendar date: `YYYY-MM-DD` (UTC) |
| `total_calls` | Long | Total calls on this day |
| `error_calls` | Long | Calls that returned HTTP ≥ 400 |
| `success_calls` | Long | `total_calls - error_calls` |

**Ordered:** Oldest → newest (ascending by date).

**Frontend chart:** Use `date` as X axis, plot two series: `total_calls` and `error_calls`.

---

#### Usage by Connection (`usage_by_connection`)

Per-GHL-subaccount call counts for the **last 30 days**, sorted by `call_count` descending.

| Field | Type | Description |
|---|---|---|
| `app_key_id` | Long | The Lofi Connect app key used |
| `location_id` | String | GHL location/subaccount ID |
| `subaccount_name` | String | Display name of the GHL subaccount |
| `call_count` | Long | Total calls through this connection in the last 30 days |

**Frontend:** Show as a table or bar chart. `subaccount_name` is the human-readable label.

---

#### Top Endpoints (`top_endpoints`)

Most-called GHL API endpoints for the **current month**, sorted by `call_count` descending.

| Field | Type | Description |
|---|---|---|
| `http_method` | String | `GET`, `POST`, `PUT`, `DELETE`, etc. |
| `endpoint` | String | Full request path (e.g. `/api/v1/ghl/contacts/abc123`) |
| `call_count` | Long | Number of times this endpoint was called |
| `avg_response_time_ms` | Double | Average latency in milliseconds |

---

### Null / Edge Case Behaviour

| Scenario | Behaviour |
|---|---|
| User has no calls this month | `total_api_calls = 0`, `success_rate = 100.0`, `error_count = 0`, `avg_calls_per_day = 0.0`, all lists are empty |
| User has no calls last month | `*_change_percent = null` for total and errors (avoid division by zero). `success_rate_change_pct` is still `0.0` (100.0 - 100.0) |
| User's plan has no limit | `monthly_operation_limit = null`, `usage_percentage = null` |
| App key not connected to GHL | `location_id = "unknown"`, `subaccount_name = "Unknown Account"` in `usage_by_connection` |

---

## GET `/usage/me`

Paginated raw API call log for the authenticated user.

**Auth:** JWT (user)

**Query Parameters:**

| Param | Type | Required | Default | Description |
|---|---|---|---|---|
| `app_key_id` | Long | No | — | Filter to a specific app key |
| `from` | ISO-8601 | No | Epoch | Start of time range |
| `to` | ISO-8601 | No | Now | End of time range |
| `page` | Int | No | `0` | Zero-based page number |
| `size` | Int | No | `20` | Results per page (max `100`) |
| `sort` | String | No | `requestedAt,desc` | Sort field and direction |

**Example request:**
```
GET /api/v1/usage/me?from=2026-07-01T00:00:00Z&to=2026-07-04T23:59:59Z&app_key_id=7&size=50
```

**Response `200 OK`:**
```json
{
  "content": [
    {
      "id": 1001,
      "app_key_id": 7,
      "user_id": 42,
      "subscription_id": 15,
      "http_method": "GET",
      "endpoint": "/api/v1/ghl/contacts/abc123",
      "response_status": 200,
      "response_time_ms": 182,
      "requested_at": "2026-07-04T10:15:30Z"
    },
    {
      "id": 1000,
      "app_key_id": 7,
      "user_id": 42,
      "subscription_id": 15,
      "http_method": "POST",
      "endpoint": "/api/v1/ghl/contacts",
      "response_status": 404,
      "response_time_ms": 95,
      "requested_at": "2026-07-04T10:12:45Z"
    }
  ],
  "pageable": {
    "page_number": 0,
    "page_size": 50
  },
  "total_elements": 1240,
  "total_pages": 25,
  "first": true,
  "last": false
}
```

**Log Entry Fields:**

| Field | Type | Description |
|---|---|---|
| `id` | Long | Log entry ID |
| `app_key_id` | Long | App key used for this call |
| `user_id` | Long | The user who made the call |
| `subscription_id` | Long | Subscription active at call time |
| `http_method` | String | `GET`, `POST`, `PUT`, `DELETE`, etc. |
| `endpoint` | String | Full request path |
| `response_status` | Int | HTTP status code returned (200, 404, 500, etc.) |
| `response_time_ms` | Long | Total latency in milliseconds |
| `requested_at` | ISO-8601 | Exact timestamp the call was received |

---

## GET `/admin/usage`

All API call logs across every user. Supports filtering by user, key, and date range.

**Auth:** JWT + ADMIN role

**Query Parameters:**

| Param | Type | Required | Default | Description |
|---|---|---|---|---|
| `user_id` | Long | No | — | Filter to a specific user |
| `app_key_id` | Long | No | — | Filter to a specific app key |
| `from` | ISO-8601 | No | Epoch | Start of time range |
| `to` | ISO-8601 | No | Now | End of time range |
| `page` | Int | No | `0` | Page number |
| `size` | Int | No | `20` | Page size |
| `sort` | String | No | `requestedAt,desc` | Sort field |

**Example request:**
```
GET /api/v1/admin/usage?user_id=42&from=2026-07-01T00:00:00Z
```

**Response `200 OK`:** Same paginated format as `/usage/me`.

---

## GET `/admin/usage/users/{userId}`

All call logs for a specific user. Shorthand for `/admin/usage?user_id={userId}`.

**Auth:** JWT + ADMIN role

**Path Parameters:**

| Param | Type | Description |
|---|---|---|
| `userId` | Long | Target user ID |

**Query Parameters:** `app_key_id`, `from`, `to`, `page`, `size`, `sort` (same as above)

**Example request:**
```
GET /api/v1/admin/usage/users/42?from=2026-07-01T00:00:00Z&size=100
```

**Response `200 OK`:** Same paginated format as `/usage/me`.

---

## How Usage is Tracked

Every call to a GHL proxy endpoint (`/api/v1/ghl/**`) is automatically recorded:

1. Request enters `AppKeyInterceptor` → validates the app key, loads the subscription, stores `appKeyId`, `userId`, `subscriptionId` in thread-local context
2. `ApiUsageInterceptor` captures the start timestamp
3. After the GHL response is returned: latency is computed, a log entry is written **asynchronously** (non-blocking — zero added latency)
4. The entry is available in usage queries within seconds

**What is logged per call:**

| Field | Source |
|---|---|
| `app_key_id` | App key used in the request header |
| `user_id` | Owner of the app key |
| `subscription_id` | User's active subscription at call time |
| `http_method` | HTTP verb of the request |
| `endpoint` | Full URI path |
| `response_status` | HTTP status returned to the caller |
| `response_time_ms` | Time from interceptor entry to response |
| `requested_at` | Exact UTC instant the request was received |

> Usage logs are **immutable audit records**. They are never modified after creation.
