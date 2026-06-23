package org.example.loficonnect.subscription.model.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum LimitKeySortField {
    ID("id"),
    CODE("code"),
    NAME("name"),
    CATEGORY("category"),
    CREATED_AT("createdAt");

    private final String fieldName;

    LimitKeySortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static Set<String> allowedFields() {
        return Arrays.stream(values())
                .map(LimitKeySortField::getFieldName)
                .collect(Collectors.toSet());
    }
}
