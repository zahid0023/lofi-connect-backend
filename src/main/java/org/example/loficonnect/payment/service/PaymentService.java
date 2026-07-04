package org.example.loficonnect.payment.service;

import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.payment.dto.request.CheckoutRequest;
import org.example.loficonnect.payment.dto.response.CheckoutResponse;
import org.example.loficonnect.payment.dto.response.PaymentStatusResponse;

public interface PaymentService {

    /**
     * Creates a Paddle-hosted checkout session for the given plan.
     * Returns a {@code checkout_url} that the frontend opens in the browser.
     *
     * <p>Flow:
     * <ol>
     *   <li>Validate plan has a Paddle price ID.</li>
     *   <li>Create Paddle transaction via POST /transactions.</li>
     *   <li>Return checkout URL — user completes payment on Paddle's hosted page.</li>
     *   <li>Paddle fires subscription.created / transaction.completed webhook → local subscription created.</li>
     * </ol>
     */
    CheckoutResponse createCheckout(Long userId, CheckoutRequest request);

    /**
     * Returns the user's latest subscription status and provisioning status.
     * The frontend polls this after the Paddle success redirect until {@code active=true}.
     */
    PaymentStatusResponse getPaymentStatus(Long userId);

    /**
     * Upgrades or downgrades the user's active subscription to a different Paddle plan.
     *
     * <p>Flow:
     * <ol>
     *   <li>Look up the user's active subscription and its Paddle subscription ID.</li>
     *   <li>Call Paddle PATCH /subscriptions/{id} with the new price ID.</li>
     *   <li>Return success immediately — Paddle fires subscription.updated webhook.</li>
     *   <li>Webhook processor detects plan change and updates local state.</li>
     * </ol>
     *
     * @param userId the authenticated user
     * @param newPlanId the local plan ID to switch to
     * @throws org.example.loficonnect.subscription.exception.NoActiveSubscriptionException if no active subscription
     * @throws jakarta.persistence.EntityNotFoundException if the new plan does not exist
     * @throws org.example.loficonnect.payment.exception.PaymentException if the Paddle API call fails
     */
    SuccessResponse upgradePlan(Long userId, Long newPlanId);

    /**
     * Requests cancellation of the user's Paddle subscription at end of the current billing period.
     *
     * <p>This does NOT change the local subscription status — the user keeps full access
     * until the period ends. The {@code subscription.cancelled} Paddle webhook fires
     * at period end and drives the local status to CANCELLED.
     *
     * <p>No-op if the subscription has no Paddle subscription ID (manually-provisioned BUNDLED plan).
     *
     * @throws org.example.loficonnect.subscription.exception.NoActiveSubscriptionException if no active subscription
     */
    void cancelUserSubscription(Long userId);
}
