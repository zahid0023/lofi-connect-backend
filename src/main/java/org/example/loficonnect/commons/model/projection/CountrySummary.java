package org.example.loficonnect.commons.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface CountrySummary {
    Long getId();

    String getCode();

    String getIso3Code();

    String getPhoneCode();

    String getName();

    String getDescription();

    Integer getSortOrder();
}
