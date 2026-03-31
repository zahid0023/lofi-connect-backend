package org.example.loficonnect.commons.controller;

import org.example.loficonnect.commons.dto.request.SubscriptionPlanCreateRequest;
import org.example.loficonnect.commons.dto.request.SubscriptionPlanUpdateRequest;
import org.example.loficonnect.commons.service.SubscriptionPlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins/subscription-plans")
public class SubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    public SubscriptionPlanController(SubscriptionPlanService subscriptionPlanService) {
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createSubscriptionPlan(@RequestBody SubscriptionPlanCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionPlanService.createSubscriptionPlan(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllSubscriptionPlans() {
        return ResponseEntity.ok(subscriptionPlanService.getAllSubscriptionPlans());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubscriptionPlanById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionPlanService.getSubscriptionPlanById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(@PathVariable Long id,
                                                    @RequestBody SubscriptionPlanUpdateRequest request) {
        return ResponseEntity.ok(subscriptionPlanService.updateSubscriptionPlan(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubscriptionPlan(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionPlanService.deleteSubscriptionPlan(id));
    }
}
