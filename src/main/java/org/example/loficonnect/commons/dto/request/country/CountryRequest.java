package org.example.loficonnect.commons.dto.request.country;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CountryRequest {
    @Size(max = 10)
    private String iso3Code;

    @Size(max = 10)
    private String phoneCode;

    private String name;

    private String description;

    @NotNull
    private Integer sortOrder;
}
