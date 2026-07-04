package org.example.loficonnect.subscription.dto.request.tenantsubscription;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubmitRefundRequest {

    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 2000, message = "Reason must be between 10 and 2000 characters")
    private String reason;
}
