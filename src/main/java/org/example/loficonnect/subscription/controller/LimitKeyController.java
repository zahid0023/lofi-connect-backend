package org.example.loficonnect.subscription.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.subscription.dto.request.limitkey.CreateLimitKeyRequest;
import org.example.loficonnect.subscription.dto.request.limitkey.UpdateLimitKeyRequest;
import org.example.loficonnect.subscription.model.entity.LimitKeyEntity;
import org.example.loficonnect.subscription.service.LimitKeyService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions/limit-keys")
@PreAuthorize("hasRole('ADMIN')")
public class LimitKeyController {

    private final LimitKeyService limitKeyService;

    public LimitKeyController(LimitKeyService limitKeyService) {
        this.limitKeyService = limitKeyService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateLimitKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(limitKeyService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(limitKeyService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(limitKeyService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLimitKeyRequest request) {
        LimitKeyEntity entity = limitKeyService.getEntityById(id);
        return ResponseEntity.ok(limitKeyService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        LimitKeyEntity entity = limitKeyService.getEntityById(id);
        return ResponseEntity.ok(limitKeyService.delete(entity));
    }
}
