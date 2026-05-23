package org.example.loficonnect.commons.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface LimitKeySummary {
    Long getId();

    String getCode();

    String getName();

    String getDataType();

    String getCategory();

    String getUnit();
}
