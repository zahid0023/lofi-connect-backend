package org.example.loficonnect.commons.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum SubscriptionPlanSortField {
    ID("id"),
    NAME("name"),
    PRICE("price"),
    BILLING_CYCLE("billingCycle"),
    DURATION_IN_DAYS("durationInDays"),
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
