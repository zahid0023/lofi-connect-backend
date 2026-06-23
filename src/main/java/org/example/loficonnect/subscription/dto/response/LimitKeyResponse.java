package org.example.loficonnect.subscription.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.subscription.model.dto.LimitKeyDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LimitKeyResponse {
    private final LimitKeyDto limitKey;

    public LimitKeyResponse(LimitKeyDto limitKey) {
        this.limitKey = limitKey;
    }
}
