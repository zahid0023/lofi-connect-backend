package org.example.loficonnect.dto.mapper.customerprovider;

import lombok.Data;
import org.example.loficonnect.dto.request.customerprovider.CustomProviderCreateRequest;

@Data
public class GoHighLevelCustomProviderCreateRequest {
    private Config live;
    private Config test;

    @Data
    public static class Config {
        private String apiKey;
        private String publishableKey;
    }

    public static GoHighLevelCustomProviderCreateRequest fromRequest(CustomProviderCreateRequest request) {
        GoHighLevelCustomProviderCreateRequest ghlRequest = new GoHighLevelCustomProviderCreateRequest();
        Config liveConfig = new Config();
        liveConfig.setApiKey(request.getLive().getApiKey());
        liveConfig.setPublishableKey(request.getLive().getPublishableKey());

        Config testConfig = new Config();
        testConfig.setApiKey(request.getTest().getApiKey());
        testConfig.setPublishableKey(request.getTest().getPublishableKey());

        ghlRequest.setLive(liveConfig);
        ghlRequest.setTest(testConfig);
        return ghlRequest;
    }
}
