package org.example.loficonnect.commons.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LimitKeyRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    private String description;

    @NotBlank
    @Size(max = 50)
    private String dataType;

    @NotBlank
    @Size(max = 50)
    private String category;

    @Size(max = 50)
    private String unit;
}
