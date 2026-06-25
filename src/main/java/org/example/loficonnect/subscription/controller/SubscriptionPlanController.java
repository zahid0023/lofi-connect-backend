package org.example.loficonnect.subscription.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanCreateRequest;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.service.SubscriptionPlanService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions/plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    // ── Public endpoints (no auth required) ──────────────────────────────────

    /** Browse all public plans with full limit/feature details. */
    @GetMapping("/public")
    public ResponseEntity<?> getPublicPlans() {
        return ResponseEntity.ok(subscriptionPlanService.getPublicPlans());
    }

    /** View a single plan's details before subscribing. */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionPlanService.getById(id));
    }

    // ── Admin-only endpoints ──────────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(subscriptionPlanService.getAll(request));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody SubscriptionPlanCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionPlanService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionPlanUpdateRequest request) {
        SubscriptionPlanEntity entity = subscriptionPlanService.getEntityById(id);
        return ResponseEntity.ok(subscriptionPlanService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        SubscriptionPlanEntity entity = subscriptionPlanService.getEntityById(id);
        return ResponseEntity.ok(subscriptionPlanService.delete(entity));
    }
}
