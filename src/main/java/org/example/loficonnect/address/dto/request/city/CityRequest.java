package org.example.loficonnect.address.dto.request.city;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CityRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    @NotNull
    private Integer sortOrder;
}
