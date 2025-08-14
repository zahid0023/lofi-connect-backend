package org.example.loficonnect.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.service.AuthorizationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/authorization")
@Slf4j
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/init")
    public ResponseEntity<Void> initAuthorization() {
        String url = authorizationService.generateAuthorizationUrl();
        log.info("Redirecting user to authorization URL: {}", url);
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> handleRedirect(@RequestParam("code") String code) {
        log.info("Redirecting user to redirect URL with code: {}", code);
        Map<String, Object> apiResponse = authorizationService.exchangeCodeForToken(code);
        log.info("Response: {}", apiResponse);
        return ResponseEntity.ok().build();
    }

}
