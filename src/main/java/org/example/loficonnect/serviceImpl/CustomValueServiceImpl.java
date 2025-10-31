package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.custom.value.GoHighLevelCustomValueCreateRequest;
import org.example.loficonnect.dto.mapper.custom.value.GoHighLevelCustomValueUpdateRequest;
import org.example.loficonnect.dto.request.custom.value.CustomValueCreateRequest;
import org.example.loficonnect.dto.request.custom.value.CustomValueUpdateRequest;
import org.example.loficonnect.feignclients.CustomValueClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CustomValueService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomValueServiceImpl implements CustomValueService {
    private final AuthorizationService authorizationService;
    private final CustomValueClient customValueClient;

    public CustomValueServiceImpl(AuthorizationService authorizationService, CustomValueClient customValueClient) {
        this.authorizationService = authorizationService;
        this.customValueClient = customValueClient;
    }

    @Override
    public JsonNode getCustomValues() {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customValueClient.getCustomValues(accessKey, version, LocationContext.getLocationId());
    }

    @Override
    public JsonNode createCustomValue(CustomValueCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomValueCreateRequest ghlRequest = GoHighLevelCustomValueCreateRequest.fromRequest(request);
        return customValueClient.createCustomValue(accessKey, version, LocationContext.getLocationId(), ghlRequest);
    }

    @Override
    public JsonNode getCustomValue(String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customValueClient.getCustomValue(accessKey, version, LocationContext.getLocationId(), id);
    }

    @Override
    public JsonNode updateCustomValue(String id, CustomValueUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomValueUpdateRequest ghlRequest = GoHighLevelCustomValueUpdateRequest.fromRequest(request);
        return customValueClient.updateCustomValue(accessKey, version, LocationContext.getLocationId(), id, ghlRequest);
    }

    @Override
    public JsonNode deleteCustomValue(String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customValueClient.deleteCustomValue(accessKey, version, LocationContext.getLocationId(), id);
    }
}
