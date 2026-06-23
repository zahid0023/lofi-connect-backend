package org.example.loficonnect.currency.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrencyRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String description;

    @NotBlank
    @Size(max = 10)
    private String symbol;
}
