package org.example.loficonnect.subscription.service;

import org.example.loficonnect.subscription.model.enums.BillingCycle;

import java.math.BigDecimal;

public interface PaddleProductService {

    /**
     * Creates a product and price in Paddle for a new subscription plan.
     *
     * @param name            plan display name
     * @param billingCycle    billing frequency
     * @param price           plan price (e.g. 29.99)
     * @param currencyCode    ISO 4217 currency code (e.g. "USD")
     * @param trialPeriodDays number of free trial days; 0 means no trial
     * @return Paddle price ID (e.g. "pri_01h...") to store on the plan
     */
    String provisionPlan(String name,
                         BillingCycle billingCycle,
                         BigDecimal price,
                         String currencyCode,
                         int trialPeriodDays);
}
