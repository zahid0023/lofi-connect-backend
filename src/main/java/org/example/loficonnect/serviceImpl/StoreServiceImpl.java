package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.store.GoHighLevelStoreUpdateRequest;
import org.example.loficonnect.dto.request.store.StoreUpdateRequest;
import org.example.loficonnect.feignclients.StoreClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.StoreService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreClient storeClient;
    private final AuthorizationService authorizationService;

    public StoreServiceImpl(StoreClient storeClient, AuthorizationService authorizationService) {
        this.storeClient = storeClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getStoreStats(String storeId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return storeClient.getStoreStats(accessKey, version, storeId, queryParams);
    }

    @Override
    public JsonNode updateStoreProducts(String storeId, StoreUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelStoreUpdateRequest ghlRequest = GoHighLevelStoreUpdateRequest.fromRequest(request);
        return storeClient.updateStoreProducts(accessKey, version, storeId, ghlRequest);
    }

}
