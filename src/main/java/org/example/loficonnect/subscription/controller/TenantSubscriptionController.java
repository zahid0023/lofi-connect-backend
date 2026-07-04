package org.example.loficonnect.subscription.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.auth.model.dto.CustomUserDetails;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.payment.service.PaymentService;
import org.example.loficonnect.subscription.dto.request.tenantsubscription.UpgradePlanRequest;
import org.example.loficonnect.subscription.service.TenantSubscriptionService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Manages the tenant subscription lifecycle.
 *
 * <p><b>Subscribe flow (Paddle-hosted):</b>
 * <ol>
 *   <li>Frontend fetches plans: {@code GET /api/v1/subscriptions/plans/public}</li>
 *   <li>Frontend calls {@code POST /api/v1/payments/checkout { plan_id }} → gets {@code checkout_url}</li>
 *   <li>Frontend opens {@code checkout_url} — user pays on Paddle's hosted page</li>
 *   <li>Paddle fires {@code subscription.created} / {@code transaction.completed} webhooks → local subscription created</li>
 *   <li>Paddle redirects browser to the configured success URL → backend redirects to frontend</li>
 *   <li>Frontend polls {@code GET /api/v1/payments/status} until {@code active=true}</li>
 * </ol>
 *
 * <p><b>Upgrade/Downgrade flow:</b>
 * <ol>
 *   <li>Frontend calls {@code POST /api/v1/subscriptions/tenant-subscriptions/upgrade { new_plan_id }}</li>
 *   <li>Backend calls Paddle {@code PATCH /subscriptions/{id}} with the new price ID</li>
 *   <li>Paddle fires {@code subscription.updated} webhook → webhook processor updates local plan</li>
 * </ol>
 *
 * <p><b>Cancel flow (end-of-period):</b>
 * <ol>
 *   <li>Frontend calls {@code DELETE /api/v1/subscriptions/tenant-subscriptions/cancel}</li>
 *   <li>Backend calls Paddle {@code POST /subscriptions/{id}/cancel { effective_from: next_billing_period }}</li>
 *   <li>User retains access until the billing period ends — NO immediate status change</li>
 *   <li>At period end: Paddle fires {@code subscription.cancelled} → webhook sets local status to CANCELLED</li>
 * </ol>
 *
 * <p>Configure the Return URL in your Paddle Dashboard → Checkout Settings to:
 * {@code {backend-url}/api/v1/subscriptions/tenant-subscriptions/success}
 */
@RestController
@RequestMapping("/api/v1/subscriptions/tenant-subscriptions")
public class TenantSubscriptionController {

    private final TenantSubscriptionService tenantSubscriptionService;
    private final PaymentService paymentService;
    private final String frontendUrl;

    public TenantSubscriptionController(TenantSubscriptionService tenantSubscriptionService,
                                        PaymentService paymentService,
                                        @Value("${frontend.url}") String frontendUrl) {
        this.tenantSubscriptionService = tenantSubscriptionService;
        this.paymentService = paymentService;
        this.frontendUrl = frontendUrl;
    }

    /**
     * Paddle success redirect — browser lands here after checkout, no JWT available.
     * Redirects to the frontend success page; frontend then polls /api/v1/payments/status.
     */
    @GetMapping("/success")
    public ResponseEntity<Void> handlePaymentSuccess() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendUrl + "/subscription/success"))
                .build();
    }

    /**
     * Upgrades or downgrades the authenticated user's plan via Paddle.
     *
     * <p>Calls Paddle's subscription update API and returns immediately.
     * The local subscription plan is updated asynchronously via the {@code subscription.updated} webhook.
     */
    @PostMapping("/upgrade")
    public ResponseEntity<?> upgrade(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpgradePlanRequest request) {
        return ResponseEntity.ok(paymentService.upgradePlan(userDetails.getId(), request.getNewPlanId()));
    }

    /** Returns the authenticated user's current active subscription details. */
    @GetMapping("/me")
    public ResponseEntity<?> getMySubscription(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tenantSubscriptionService.getMyActiveSubscription(userDetails.getId()));
    }

    /**
     * Requests cancellation of the user's subscription at end of the current billing period via Paddle.
     *
     * <p>The user retains full access until the period ends.
     * The {@code subscription.cancelled} Paddle webhook drives the local status change.
     */
    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancel(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        paymentService.cancelUserSubscription(userDetails.getId());
        return ResponseEntity.ok(
                java.util.Map.of("success", true,
                        "message", "Cancellation scheduled at end of billing period. "
                                + "You will retain access until then."));
    }

    /** Admin: paginated list of all tenant subscriptions. */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(tenantSubscriptionService.getAll(request));
    }
}
