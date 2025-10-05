package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.storesetting.GoHighLevelStoreSettingCreateRequest;
import org.example.loficonnect.dto.request.storesetting.StoreSettingCreateRequest;
import org.example.loficonnect.feignclients.StoreSettingClient;
import org.example.loficonnect.service.StoreSettingService;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class StoreSettingServiceImpl implements StoreSettingService {

    private final StoreSettingClient storeSettingClient;
    private final AuthorizationService authorizationService;

    public StoreSettingServiceImpl(StoreSettingClient storeSettingClient, AuthorizationService authorizationService) {
        this.storeSettingClient = storeSettingClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createOrUpdateStoreSetting(StoreSettingCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelStoreSettingCreateRequest ghlRequest = GoHighLevelStoreSettingCreateRequest.fromRequest(request);
        return storeSettingClient.createOrUpdateStoreSetting(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getStoreSetting(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return storeSettingClient.getStoreSetting(accessKey, version, queryParams);
    }

}
