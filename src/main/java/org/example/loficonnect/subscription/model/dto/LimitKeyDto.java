package org.example.loficonnect.subscription.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.loficonnect.subscription.model.enums.LimitKeyCategory;
import org.example.loficonnect.subscription.model.enums.LimitKeyDataType;
import org.example.loficonnect.subscription.model.enums.LimitKeyUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LimitKeyDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private LimitKeyDataType dataType;
    private LimitKeyCategory category;
    private LimitKeyUnit unit;
}
