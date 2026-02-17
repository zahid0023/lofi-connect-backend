package org.example.loficonnect.auth.dto.response.appkey;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.auth.model.dto.LofiConnectAppKeyDTO;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenerateAppKeyResponse {
    private LofiConnectAppKeyDTO data;

    public GenerateAppKeyResponse(LofiConnectAppKeyDTO dto) {
        this.data = dto;
    }
}
