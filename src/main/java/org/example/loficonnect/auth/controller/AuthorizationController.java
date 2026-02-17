package org.example.loficonnect.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.auth.service.AppKeyService;
import org.example.loficonnect.auth.service.ScopeService;
import org.example.loficonnect.auth.model.enitty.LofiConnectAppKeyEntity;
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
    private final AppKeyService appKeyService;
    private final String frontendUrl;

    public AuthorizationController(AuthorizationService authorizationService,
                                   ScopeService scopeService,
                                   AppKeyService appKeyService,
                                   @Value("${FRONT_END_URL}") String frontendUrl) {
        this.authorizationService = authorizationService;
        this.scopeService = scopeService;
        this.appKeyService = appKeyService;
        this.frontendUrl = frontendUrl;
    }

    @GetMapping("/init")
    public ResponseEntity<Void> initAuthorization(@RequestParam("app-key-id") Long appKeyId) {
        List<String> scopes = scopeService.getAllScopesNames();

        String url = authorizationService.generateAuthorizationUrl(scopes, appKeyId);

        log.info("Redirecting user to authorization URL: {}", url);
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    @GetMapping("/redirect")
    public ResponseEntity<?> handleRedirect(
            @RequestParam("code") String code,
            @RequestParam("state") String state) {

        Map<String, Object> apiResponse = authorizationService.exchangeCodeForToken(code);

        LofiConnectAppKeyEntity entity = appKeyService.getAppKeyEntityById(Long.valueOf(state));
        authorizationService.saveGoHighLevelToken(entity, apiResponse);

        String redirectUrl = frontendUrl + "/portal/connections";

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
