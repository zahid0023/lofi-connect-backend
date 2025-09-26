package org.example.loficonnect.dto.request.estimate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceCreateRequest {
    private String altId;
    private String altType;
    private Boolean markAsInvoiced;
    private String version;
}
