package org.example.loficonnect.subscription.dto.request.tenantsubscription;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewRefundRequest {

    @Size(max = 2000, message = "Admin notes must not exceed 2000 characters")
    private String adminNotes;
}
