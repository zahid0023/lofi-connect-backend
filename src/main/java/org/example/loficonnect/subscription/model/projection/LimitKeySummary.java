package org.example.loficonnect.subscription.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.example.loficonnect.subscription.model.enums.LimitKeyCategory;
import org.example.loficonnect.subscription.model.enums.LimitKeyDataType;
import org.example.loficonnect.subscription.model.enums.LimitKeyUnit;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface LimitKeySummary {
    Long getId();
    String getCode();
    String getName();
    LimitKeyCategory getCategory();
    LimitKeyDataType getDataType();
    LimitKeyUnit getUnit();
}
