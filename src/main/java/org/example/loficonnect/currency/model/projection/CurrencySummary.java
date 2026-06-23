package org.example.loficonnect.currency.model.projection;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public interface CurrencySummary {
    Long getId();

    String getCode();

    String getName();

    String getDescription();

    String getSymbol();
}
