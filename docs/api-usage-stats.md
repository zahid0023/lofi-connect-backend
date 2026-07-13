# API Usage Stats

**Endpoint:** `GET /api/v1/usage-logs/stats/me`
**Auth:** `Authorization: Bearer <access_token>` (JWT)
**Access:** Any authenticated user.

---

Returns **4 stat cards** comparing the **current period** (`[now − range days, now]`) against the **previous period** (
`[now − 2×range, now − range]`) of equal length. Use this to drive the dashboard stat cards on the frontend.

---

## Query Parameters

| Parameter | Type    | Required | Default | Description                                                        |
|-----------|---------|----------|---------|--------------------------------------------------------------------|
| `range`   | integer | No       | `7`     | Look-back window in **days**. Common values: `1` (24 h), `7`, `30` |

---

## Response `200 OK`

```json
{
  "total_calls": {
    "value": 142.0,
    "change_percentage": 18.5,
    "trend": "UP",
    "icon": "Zap"
  },
  "success_rate": {
    "value": 96.48,
    "change_percentage": 2.1,
    "trend": "UP",
    "icon": "TrendingUp"
  },
  "errors": {
    "value": 5.0,
    "change_percentage": -16.67,
    "trend": "DOWN",
    "icon": "AlertCircle"
  },
  "avg_calls_per_day": {
    "value": 20.29,
    "change_percentage": 18.5,
    "trend": "UP",
    "icon": "Activity"
  }
}
```

---

## `UsageStatsResponse` Card Reference

| Card field          | Description                           | Formula                          |
|---------------------|---------------------------------------|----------------------------------|
| `total_calls`       | Total API calls in the current period | `COUNT(logs)`                    |
| `success_rate`      | Percentage of non-error calls         | `(total - errors) / total × 100` |
| `errors`            | Count of calls where `error = true`   | `COUNT(logs WHERE error = true)` |
| `avg_calls_per_day` | Average daily call volume             | `total_calls / range`            |

---

## `StatCard` Field Reference

| Field               | Type           | Description                                                                                                                                                          |
|---------------------|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `value`             | double         | Current-period value, rounded to 2 decimal places                                                                                                                    |
| `change_percentage` | double \| null | `((current − previous) / previous) × 100`, rounded to 2 dp. `null` when the previous period had **zero** activity (no meaningful baseline — avoids division by zero) |
| `trend`             | enum           | Direction of change relative to the previous period (see below)                                                                                                      |
| `icon`              | string         | Lucide-React icon component name to use on the card                                                                                                                  |

---

## `trend` Enum Values

| Value     | Meaning                                                                   |
|-----------|---------------------------------------------------------------------------|
| `UP`      | Current value is higher than the previous period                          |
| `DOWN`    | Current value is lower than the previous period                           |
| `NEUTRAL` | No change, or no previous baseline (`change_percentage` is `null` or `0`) |

---

## `icon` Values per Card

| Card                | `icon`          | Lucide component  |
|---------------------|-----------------|-------------------|
| `total_calls`       | `"Zap"`         | `<Zap />`         |
| `success_rate`      | `"TrendingUp"`  | `<TrendingUp />`  |
| `errors`            | `"AlertCircle"` | `<AlertCircle />` |
| `avg_calls_per_day` | `"Activity"`    | `<Activity />`    |

---

## `change_percentage` / `trend` Behaviour

| Scenario                                      | `change_percentage` | `trend`   |
|-----------------------------------------------|---------------------|-----------|
| Previous period had activity, value increased | `+18.5`             | `UP`      |
| Previous period had activity, value decreased | `-16.67`            | `DOWN`    |
| Previous period had activity, no change       | `0.0`               | `NEUTRAL` |
| Previous period had **zero** calls            | `null`              | `NEUTRAL` |

> **Errors card:** `change_percentage` is `null` when the previous period had both zero errors **and** zero total calls.
> If the previous period had calls but zero errors, `null` is still returned to avoid a misleading `+∞` percentage.

---

## Error Responses

| HTTP Status        | `error_code`   | Cause                         |
|--------------------|----------------|-------------------------------|
| `401 Unauthorized` | `UNAUTHORIZED` | Missing or invalid JWT        |
| `403 Forbidden`    | `FORBIDDEN`    | Authenticated but not allowed |
