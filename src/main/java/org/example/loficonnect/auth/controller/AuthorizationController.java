package org.example.loficonnect.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.response.AppKeyResponse;
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
        Map<String, Object> apiResponse = authorizationService.exchangeCodeForToken(code);
        AppKeyResponse appKey = authorizationService.generateAndSaveAppKey(apiResponse);

        String frontendUrl = "http://localhost:5173/api-keys?appKey=" + appKey.getSecretKey();

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, frontendUrl)
                .build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
