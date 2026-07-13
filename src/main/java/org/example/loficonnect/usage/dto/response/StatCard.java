package org.example.loficonnect.usage.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.example.loficonnect.usage.model.enums.StatIcon;
import org.example.loficonnect.usage.model.enums.StatTrend;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StatCard {
    private double value;
    /** Null when the previous period had 0 (no meaningful comparison baseline). */
    private Double changePercentage;
    private StatTrend trend;
    private StatIcon icon;
}
