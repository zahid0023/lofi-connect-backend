package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.FunnelClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.FunnelService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class FunnelServiceImpl implements FunnelService {

    private final FunnelClient funnelClient;
    private final AuthorizationService authorizationService;

    public FunnelServiceImpl(FunnelClient funnelClient, AuthorizationService authorizationService) {
        this.funnelClient = funnelClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getFunnelList(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return funnelClient.getFunnelList(accessKey, version,queryParams);
    }

    @Override
    public JsonNode getFunnelPages(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return funnelClient.getFunnelPages(accessKey,version, queryParams);
    }

    @Override
    public JsonNode getFunnelPageCount(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return funnelClient.getFunnelPageCount(accessKey, version, queryParams);
    }

}
