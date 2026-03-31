package org.example.loficonnect.commons.controller;

import org.example.loficonnect.commons.dto.request.LimitKeyCreateRequest;
import org.example.loficonnect.commons.dto.request.LimitKeyUpdateRequest;
import org.example.loficonnect.commons.service.LimitKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins/limit-keys")
public class LimitKeyController {
    private final LimitKeyService limitKeyService;

    public LimitKeyController(LimitKeyService limitKeyService) {
        this.limitKeyService = limitKeyService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createLimitKey(@RequestBody LimitKeyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(limitKeyService.createLimitKey(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllLimitKeys() {
        return ResponseEntity.ok(limitKeyService.getAllLimitKeys());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getLimitKeyById(@PathVariable Long id) {
        return ResponseEntity.ok(limitKeyService.getLimitKeyById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLimitKey(@PathVariable Long id, @RequestBody LimitKeyUpdateRequest request) {
        return ResponseEntity.ok(limitKeyService.updateLimitKey(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLimitKey(@PathVariable Long id) {
        return ResponseEntity.ok(limitKeyService.deleteLimitKey(id));
    }
}
