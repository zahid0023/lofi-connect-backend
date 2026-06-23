package org.example.loficonnect.subscription.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum SubscriptionPlanSortField {
    ID("id"),
    CODE("code"),
    NAME("name"),
    PRICE("price"),
    BILLING_CYCLE("billingCycle"),
    SORT_ORDER("sortOrder"),
    CREATED_AT("createdAt");

    private final String fieldName;

    SubscriptionPlanSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(SubscriptionPlanSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
