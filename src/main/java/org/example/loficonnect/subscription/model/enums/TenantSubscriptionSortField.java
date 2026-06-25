package org.example.loficonnect.subscription.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TenantSubscriptionSortField {
    ID("id"),
    STATUS("status"),
    START_DATE("startDate"),
    END_DATE("endDate"),
    CREATED_AT("createdAt");

    private final String fieldName;

    TenantSubscriptionSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(TenantSubscriptionSortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
