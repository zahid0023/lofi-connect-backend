package org.example.loficonnect.auth.controller;

import org.example.loficonnect.auth.dto.request.appkey.CreateAppKeyRequest;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.auth.service.AppKeyService;
import org.example.loficonnect.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/app-keys")
public class AppKeyController {
    private final AppKeyService appKeyService;
    private final UserService userService;

    public AppKeyController(AppKeyService appKeyService,
                            UserService userService) {
        this.appKeyService = appKeyService;
        this.userService = userService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateAppKey(@RequestBody CreateAppKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appKeyService.generateAppKey(request));
    }

    @GetMapping
    public ResponseEntity<?> getAppKeys() {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        return ResponseEntity.ok(appKeyService.getAllAppKeys(userEntity.getId()));
    }

    @PutMapping("/assign-ghl")
    public ResponseEntity<?> assignAppKeyToGHL() {
        return ResponseEntity.ok().build();
    }
}
