package org.example.loficonnect.payment.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaddleCustomData {
    private String userId;
    private Object planId;

    public Long getPlanIdAsLong() {
        if (planId == null) return null;
        if (planId instanceof Number n) return n.longValue();
        return Long.parseLong(planId.toString());
    }
}
