package org.example.loficonnect.payment.service;

import org.example.loficonnect.payment.dto.request.CheckoutRequest;
import org.example.loficonnect.payment.dto.response.CheckoutResponse;
import org.example.loficonnect.payment.dto.response.PaymentStatusResponse;

public interface PaymentService {

    /**
     * Creates a Paddle-hosted checkout session for the given plan.
     * Returns a {@code checkout_url} that the frontend opens in the browser.
     *
     * @param userId the authenticated user
     * @param request contains the plan ID to subscribe to
     * @return checkout URL and Paddle transaction ID
     * @throws jakarta.persistence.EntityNotFoundException if the plan does not exist
     * @throws org.example.loficonnect.payment.exception.PaymentException if the plan has no Paddle price
     *         or the Paddle API call fails
     */
    CheckoutResponse createCheckout(Long userId, CheckoutRequest request);

    /**
     * Returns the user's latest subscription status and provisioning status.
     * Used by the frontend to poll after the Paddle success redirect.
     *
     * @param userId the authenticated user
     * @return status snapshot; {@code active=false} if no subscription exists
     */
    PaymentStatusResponse getPaymentStatus(Long userId);

    /**
     * Looks up the user's active subscription's Paddle subscription ID and
     * requests cancellation at {@code effective_from=next_billing_period}.
     * No-ops silently if the subscription has no associated Paddle subscription
     * (e.g. manually-provisioned BUNDLED plans).
     *
     * @throws org.example.loficonnect.subscription.exception.NoActiveSubscriptionException
     *         if the user has no active or trialing subscription
     */
    void cancelUserSubscription(Long userId);
}
