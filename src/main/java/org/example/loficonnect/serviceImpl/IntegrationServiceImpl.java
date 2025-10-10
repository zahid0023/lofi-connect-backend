package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.integrations.GoHighLevelIntegrationCreateRequest;
import org.example.loficonnect.dto.request.integrations.IntegrationCreateRequest;
import org.example.loficonnect.feignclients.IntegrationClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.IntegrationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class IntegrationServiceImpl implements IntegrationService {

    private final IntegrationClient integrationClient;
    private final AuthorizationService authorizationService;

    public IntegrationServiceImpl(IntegrationClient integrationClient, AuthorizationService authorizationService) {
        this.integrationClient = integrationClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createIntegration(IntegrationCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelIntegrationCreateRequest ghlRequest = GoHighLevelIntegrationCreateRequest.fromRequest(request);
        return integrationClient.createIntegration(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getIntegrationProviders(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return integrationClient.getIntegrationProviders(accessKey, version, queryParams);
    }

}
