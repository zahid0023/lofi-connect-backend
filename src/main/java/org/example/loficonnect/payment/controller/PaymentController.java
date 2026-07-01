package org.example.loficonnect.payment.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.auth.model.dto.CustomUserDetails;
import org.example.loficonnect.payment.dto.request.CheckoutRequest;
import org.example.loficonnect.payment.dto.response.CheckoutResponse;
import org.example.loficonnect.payment.dto.response.PaymentStatusResponse;
import org.example.loficonnect.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Payment endpoints for initiating Paddle checkout and polling payment status.
 *
 * <p>Checkout flow:
 * <ol>
 *   <li>Frontend fetches plans: {@code GET /api/v1/subscriptions/plans/public}</li>
 *   <li>User selects a plan; frontend calls {@code POST /api/v1/payments/checkout { plan_id }}</li>
 *   <li>Backend creates a Paddle transaction and returns a {@code checkout_url}</li>
 *   <li>Frontend opens {@code checkout_url} — user completes payment on Paddle's hosted page</li>
 *   <li>Paddle sends {@code subscription.created} webhook → backend saves subscription</li>
 *   <li>Paddle redirects browser to the configured success URL → backend redirects to frontend</li>
 *   <li>Frontend polls {@code GET /api/v1/payments/status} until {@code active = true}</li>
 * </ol>
 */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Creates a Paddle-hosted checkout session.
     *
     * <p>Returns a {@code checkout_url} that the frontend opens in the browser.
     * The backend passes {@code custom_data: { user_id, plan_id }} to Paddle so the
     * {@code subscription.created} webhook can link the payment to the correct user and plan.
     *
     * @param userDetails the authenticated user (JWT required)
     * @param request     contains the {@code plan_id} to subscribe to
     * @return {@code 200 OK} with {@code { checkout_url, transaction_id }}
     */
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> createCheckout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CheckoutRequest request) {

        CheckoutResponse response = paymentService.createCheckout(userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns the current subscription status for the authenticated user.
     *
     * <p>Frontend polls this after the Paddle success redirect to confirm the subscription
     * was created and provisioned (via the {@code subscription.created} webhook).
     *
     * @param userDetails the authenticated user (JWT required)
     * @return {@code 200 OK} with status fields; {@code active=false} if no subscription exists yet
     */
    @GetMapping("/status")
    public ResponseEntity<PaymentStatusResponse> getPaymentStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(paymentService.getPaymentStatus(userDetails.getId()));
    }
}
