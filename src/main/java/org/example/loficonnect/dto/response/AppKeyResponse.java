package org.example.loficonnect.dto.response;

import lombok.Data;

@Data
public class AppKeyResponse {
    private final String secretKey;

    public AppKeyResponse(String secretKey) {
        this.secretKey = secretKey;
    }
}
