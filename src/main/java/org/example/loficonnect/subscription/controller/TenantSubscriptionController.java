package org.example.loficonnect.subscription.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.auth.model.dto.CustomUserDetails;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.SubscribeRequest;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.UpgradePlanRequest;
import org.example.loficonnect.subscription.service.TenantSubscriptionService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions/tenant-subscriptions")
public class TenantSubscriptionController {

    private final TenantSubscriptionService tenantSubscriptionService;

    public TenantSubscriptionController(TenantSubscriptionService tenantSubscriptionService) {
        this.tenantSubscriptionService = tenantSubscriptionService;
    }

    @PostMapping
    public ResponseEntity<?> subscribe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SubscribeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tenantSubscriptionService.subscribe(userDetails.getId(), request));
    }

    @PostMapping("/upgrade")
    public ResponseEntity<?> upgrade(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpgradePlanRequest request) {
        return ResponseEntity.ok(tenantSubscriptionService.upgrade(userDetails.getId(), request));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMySubscription(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tenantSubscriptionService.getMyActiveSubscription(userDetails.getId()));
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancel(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tenantSubscriptionService.cancel(userDetails.getId()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(tenantSubscriptionService.getAll(request));
    }
}
