1️⃣ User Security (Dashboard Layer)

Users:

Register or log in (username/password).

Get authenticated using Spring Security (with JWT or session).

Access their dashboard UI where they can:

View connected GHL accounts.

Generate or revoke app keys.

See usage statistics.

This part uses standard Spring Security authentication with UserDetailsService, password hashing, login endpoints, etc.

The Security Stack Will Look Like This      
+-------------------------------------------------------------+
| Dashboard UI |
| - Login/Register (Spring Security + JWT)                   |
| - Generate / Revoke App Keys (Backend APIs secured by JWT) |
+-------------------------------------------------------------+
|
↓
+-------------------------------------------------------------+
| API Gateway Layer |
| - Clients hit your APIs using "Authorization: Bearer <AppKey>" |
| - AppKeyInterceptor validates and maps to GHL tokens |
| - Calls GHL APIs with proper bearer tokens |
+-------------------------------------------------------------+


| Endpoint                   | Method   | Description                   | Security     |
|----------------------------| -------- | ----------------------------- | ------------ |
| `/authorization/register`  | `POST`   | Register new user             | Public       |
| `/authorization/login`     | `POST`   | Authenticate user, return JWT | Public       |
| `/dashboard/ghl/accounts`  | `GET`    | View connected GHL accounts   | JWT required |
| `/dashboard/app-keys`      | `GET`    | View app keys                 | JWT required |
| `/dashboard/app-keys`      | `POST`   | Generate app key              | JWT required |
| `/dashboard/app-keys/{id}` | `DELETE` | Revoke app key                | JWT required |
| `/dashboard/usage`         | `GET`    | View usage statistics         | JWT required |