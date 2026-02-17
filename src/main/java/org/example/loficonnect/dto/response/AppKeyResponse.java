package org.example.loficonnect.dto.response;

import lombok.Data;
import org.example.loficonnect.auth.model.dto.LofiConnectAppKeyDTO;

@Data
public class AppKeyResponse {
    private final LofiConnectAppKeyDTO data;

    public AppKeyResponse(LofiConnectAppKeyDTO data) {
        this.data = data;
    }
}
