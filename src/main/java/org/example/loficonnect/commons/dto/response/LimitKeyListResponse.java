package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.commons.model.dto.LimitKeyDto;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LimitKeyListResponse {
    private List<LimitKeyDto> limitKeys;

    public LimitKeyListResponse(List<LimitKeyDto> limitKeys) {
        this.limitKeys = limitKeys;
    }
}
