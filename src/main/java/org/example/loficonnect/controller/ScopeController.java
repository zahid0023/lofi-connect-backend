package org.example.loficonnect.controller;

import org.example.loficonnect.dto.request.scope.CreateScopeRequest;
import org.example.loficonnect.service.ScopeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins/scopes")
public class ScopeController {
    private final ScopeService scopeService;

    public ScopeController(ScopeService scopeService) {
        this.scopeService = scopeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createScope(@RequestBody CreateScopeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scopeService.createScope(request));
    }
}
