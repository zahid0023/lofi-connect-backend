We want to build a brokered OAuth2 flow where our backend is the only one that ever talks directly to GoHighLevel’s
OAuth endpoints, and the client only ever sees our own secret key.

This means:

First time → User hits out /authorization/init endpoint → we handle OAuth → store tokens in DB → issue our own app_key
to
the user for the sub account.

Subsequent calls → User sends only app_key → our backend looks up stored GHL tokens for this app_key→ refreshes if
needed → calls
GHL API → returns response.

1. Flow Overview

   ```
   [Client] --> [Our API: /authorization/init] --> Redirect to GHL login
   ^ |
   | v
   Our API <--(code)-- GHL OAuth redirect <--------+
   |
   v
   Generate app_key & store (mapped to GHL tokens)
   |
   +--> Return app_key to client
   
   For subsequent requests:
   
   [Client] --> [Our API: /contacts?app_key=1234] --> Fetch tokens from DB --> Call GHL API --> Return response
   ```

2. First-Time Auth Flow

   Step 1: Initiate Authentication (redirect to GHL)

   ```html
    GET /authorization/init
   ```

   ```java
   @GetMapping("/init")
   public ResponseEntity<Void> initAuthorization() {
        String url = authorizationService.generateAuthorizationUrl();
        log.info("Redirecting user to authorization URL: {}", url);
        return ResponseEntity.status(302)
            .header(HttpHeaders.LOCATION, url)
            .build();
      }
   ```

   Step 2: GHL redirects back to our /authorization/redirect with code + exchange the code with token + send user
   app_key:

      ```java
      
      @GetMapping("/redirect")
      public ResponseEntity<?> handleRedirect(@RequestParam("code") String code) {
          log.info("Redirecting user to redirect URL with code: {}", code);
          Map<String, Object> apiResponse = authorizationService.exchangeCodeForToken(code);
          log.info("Response: {}", apiResponse);
          return ResponseEntity.ok().build();
      }
      ```

3. Subsequent Requests
   ```html
   GET /contacts
   Authorization: Bearer <app_key>

   ```

4. Key Points

   ```
   Clients never see GHL tokens.
   
   We manage OAuth flow + token refresh.
   
   Our app_key is permanent until revoked.
   
   We can implement rate limiting & logging on our gateway easily.
   
   If a client loses the secret key → we can regenerate and remap.
   ```
