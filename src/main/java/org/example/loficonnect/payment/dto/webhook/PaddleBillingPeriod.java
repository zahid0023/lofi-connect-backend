package org.example.loficonnect.payment.dto.webhook;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaddleBillingPeriod {
    private Instant startsAt;
    private Instant endsAt;
}
