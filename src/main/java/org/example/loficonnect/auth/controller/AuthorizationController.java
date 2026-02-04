package org.example.loficonnect.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.service.ScopeService;
import org.example.loficonnect.dto.response.AppKeyResponse;
import org.example.loficonnect.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/authorization")
@Slf4j
public class AuthorizationController {
    private final AuthorizationService authorizationService;
    private final ScopeService scopeService;
    private final String frontendUrl;

    public AuthorizationController(AuthorizationService authorizationService,
                                   ScopeService scopeService,
                                   @Value("${FRONT_END_URL}") String frontendUrl) {
        this.authorizationService = authorizationService;
        this.scopeService = scopeService;
        this.frontendUrl = frontendUrl;
    }

    @GetMapping("/init")
    public ResponseEntity<Void> initAuthorization() {
        List<String> scopes = scopeService.getAllScopesNames();
        String url = authorizationService.generateAuthorizationUrl(scopes);
        log.info("Redirecting user to authorization URL: {}", url);
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> handleRedirect(@RequestParam("code") String code) {
        Map<String, Object> apiResponse = authorizationService.exchangeCodeForToken(code);
        AppKeyResponse appKey = authorizationService.generateAndSaveAppKey(apiResponse, code);

        String redirectUrl = frontendUrl + appKey.getSecretKey() + "&code=" + code;

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestParam("code") String code, @RequestParam("connection-name") String connectionName) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
