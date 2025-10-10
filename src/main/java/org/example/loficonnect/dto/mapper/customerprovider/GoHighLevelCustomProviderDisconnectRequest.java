package org.example.loficonnect.dto.mapper.customerprovider;

import lombok.Data;
import org.example.loficonnect.dto.request.customerprovider.CustomProviderDisconnectRequest;

@Data
public class GoHighLevelCustomProviderDisconnectRequest {
    private Boolean liveMode;

    public static GoHighLevelCustomProviderDisconnectRequest fromRequest(CustomProviderDisconnectRequest request) {
        GoHighLevelCustomProviderDisconnectRequest ghlRequest = new GoHighLevelCustomProviderDisconnectRequest();
        ghlRequest.setLiveMode(request.getLiveMode());
        return ghlRequest;
    }
}
