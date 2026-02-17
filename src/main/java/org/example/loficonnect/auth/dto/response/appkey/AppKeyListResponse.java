package org.example.loficonnect.auth.dto.response.appkey;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.auth.model.dto.LofiConnectAppKeyDTO;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppKeyListResponse {
    private List<LofiConnectAppKeyDTO> data;

    public AppKeyListResponse(List<LofiConnectAppKeyDTO> data) {
        this.data = data;
    }
}
