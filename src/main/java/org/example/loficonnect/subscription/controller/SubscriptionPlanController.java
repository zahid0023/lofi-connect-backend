package org.example.loficonnect.subscription.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.currency.model.entity.CurrencyEntity;
import org.example.loficonnect.currency.service.CurrencyService;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanCreateRequest;
import org.example.loficonnect.subscription.dto.request.plan.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.subscription.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.subscription.service.PaddleProductService;
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
    private final CurrencyService currencyService;
    private final PaddleProductService paddleProductService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService,
                                      CurrencyService currencyService,
                                      PaddleProductService paddleProductService) {
        this.subscriptionPlanService = subscriptionPlanService;
        this.currencyService = currencyService;
        this.paddleProductService = paddleProductService;
    }

    // ── Public endpoints (no auth required) ──────────────────────────────────

    /**
     * Browse all public plans with full limit/feature details.
     */
    @GetMapping("/public")
    public ResponseEntity<?> getPublicPlans() {
        return ResponseEntity.ok(subscriptionPlanService.getPublicPlans());
    }

    /**
     * View a single plan's details before subscribing.
     */
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
        CurrencyEntity currencyEntity = currencyService.getEntityById(request.getCurrencyId());
        String priceId = paddleProductService.provisionPlan(request.getName(),
                request.getBillingCycle(),
                request.getPrice(),
                currencyEntity.getCode(),
                request.getTrialPeriodDays());
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionPlanService.create(request, currencyEntity, priceId));
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
