package org.example.loficonnect.usage.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Lucide-React icon names for usage stat cards.
 * Serializes to the exact icon component name the frontend expects.
 */
public enum StatIcon {
    ACTIVITY("Activity"),
    ALERT_CIRCLE("AlertCircle"),
    TRENDING_UP("TrendingUp"),
    ZAP("Zap");

    private final String iconName;

    StatIcon(String iconName) {
        this.iconName = iconName;
    }

    @JsonValue
    public String getIconName() {
        return iconName;
    }
}
