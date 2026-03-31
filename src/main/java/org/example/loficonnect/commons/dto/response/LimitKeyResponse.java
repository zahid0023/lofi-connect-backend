package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.commons.model.dto.LimitKeyDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LimitKeyResponse {
    private LimitKeyDto data;

    public LimitKeyResponse(LimitKeyDto limitKey) {
        this.data = limitKey;
    }
}
