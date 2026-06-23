package org.example.loficonnect.address.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface CitySummary {
    Long getId();
    String getCode();
    String getName();
    String getDescription();
    Integer getSortOrder();
}
