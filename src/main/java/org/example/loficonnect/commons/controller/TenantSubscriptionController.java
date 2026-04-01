package org.example.loficonnect.commons.controller;

import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.auth.service.UserService;
import org.example.loficonnect.commons.dto.request.TenantSubscriptionCreateRequest;
import org.example.loficonnect.commons.model.entity.SubscriptionPlanEntity;
import org.example.loficonnect.commons.service.SubscriptionPlanService;
import org.example.loficonnect.commons.service.TenantSubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions/tenant-subscriptions")
public class TenantSubscriptionController {

    private final TenantSubscriptionService tenantSubscriptionService;
    private final UserService userService;
    private final SubscriptionPlanService subscriptionPlanService;

    public TenantSubscriptionController(TenantSubscriptionService tenantSubscriptionService,
                                        UserService userService,
                                        SubscriptionPlanService subscriptionPlanService) {
        this.tenantSubscriptionService = tenantSubscriptionService;
        this.userService = userService;
        this.subscriptionPlanService = subscriptionPlanService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> subscribePlan(@RequestBody TenantSubscriptionCreateRequest request) {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        SubscriptionPlanEntity subscriptionPlanEntity = subscriptionPlanService.getSubscriptionPlanEntityById(request.getSubscriptionPlanId());
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantSubscriptionService.subscribePlan(request, userEntity, subscriptionPlanEntity));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upgrade")
    public ResponseEntity<?> upgradePlan(@RequestBody TenantSubscriptionCreateRequest request) {
        UserEntity userEntity = userService.getAuthenticatedUserEntity();
        SubscriptionPlanEntity newPlanEntity = subscriptionPlanService.getSubscriptionPlanEntityById(request.getSubscriptionPlanId());
        return ResponseEntity.ok(tenantSubscriptionService.upgradePlan(request, userEntity, newPlanEntity));
    }
}
