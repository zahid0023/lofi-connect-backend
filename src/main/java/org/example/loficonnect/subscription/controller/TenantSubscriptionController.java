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
 * Manages tenant subscriptions.
 *
 * <p>Checkout flow (Paddle-hosted):
 * <ol>
 *   <li>Frontend fetches plans ({@code GET /api/v1/subscriptions/plans/public}) to get {@code paddle_price_id}.</li>
 *   <li>Frontend opens Paddle checkout (Paddle.js or direct link) with {@code custom_data = { user_id, plan_id }}.</li>
 *   <li>Paddle redirects to {@code GET /api/v1/subscriptions/tenant-subscriptions/success?_ptxn={id}} after payment.</li>
 *   <li>Backend saves the subscription, then redirects the browser to the frontend success page.</li>
 * </ol>
 *
 * <p>Configure the Return URL in your Paddle Dashboard → Checkout Settings to point to
 * {@code {backend-url}/api/v1/subscriptions/tenant-subscriptions/success}.
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

    @GetMapping("/success")
    public ResponseEntity<Void> handlePaymentSuccess() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendUrl + "/subscription/success"))
                .build();
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

    /**
     * Requests cancellation of the user's Paddle subscription at end of the current billing period.
     * The local subscription status is updated immediately; Paddle's {@code subscription.cancelled}
     * webhook will also arrive and is handled idempotently.
     */
    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancel(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        paymentService.cancelUserSubscription(userDetails.getId());
        return ResponseEntity.ok(tenantSubscriptionService.cancel(userDetails.getId()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(tenantSubscriptionService.getAll(request));
    }
}
