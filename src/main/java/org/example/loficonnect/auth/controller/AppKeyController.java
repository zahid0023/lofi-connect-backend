package org.example.loficonnect.auth.controller;

import org.example.loficonnect.auth.dto.request.appkey.CreateAppKeyRequest;
import org.example.loficonnect.auth.model.dto.CustomUserDetails;
import org.example.loficonnect.auth.service.AppKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/app-keys")
public class AppKeyController {
    private final AppKeyService appKeyService;

    public AppKeyController(AppKeyService appKeyService) {
        this.appKeyService = appKeyService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateAppKey(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateAppKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appKeyService.generateAppKey(userDetails.getId(), request));
    }

    @GetMapping
    public ResponseEntity<?> getAppKeys(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(appKeyService.getAllAppKeys(userDetails.getId()));
    }

    @PutMapping("/assign-ghl")
    public ResponseEntity<?> assignAppKeyToGHL() {
        return ResponseEntity.ok().build();
    }
}
