package org.example.loficonnect.subscription.dto.request.limitkey;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.loficonnect.subscription.model.enums.LimitKeyCategory;
import org.example.loficonnect.subscription.model.enums.LimitKeyDataType;
import org.example.loficonnect.subscription.model.enums.LimitKeyUnit;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LimitKeyRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    private String description;

    @NotNull
    private LimitKeyDataType dataType;

    @NotNull
    private LimitKeyCategory category;

    private LimitKeyUnit unit;
}
