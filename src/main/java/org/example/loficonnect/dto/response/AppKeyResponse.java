package org.example.loficonnect.dto.response;

import lombok.Data;
import org.example.loficonnect.model.dto.LofiConnectAppKeyDTO;

@Data
public class AppKeyResponse {
    private final LofiConnectAppKeyDTO data;

    public AppKeyResponse(LofiConnectAppKeyDTO data) {
        this.data = data;
    }
}
