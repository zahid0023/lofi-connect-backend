package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.collections.GoHighLevelCollectionCreateRequest;
import org.example.loficonnect.dto.mapper.collections.GoHighLevelCollectionUpdateRequest;
import org.example.loficonnect.dto.request.collections.CollectionCreateRequest;
import org.example.loficonnect.dto.request.collections.CollectionUpdateRequest;
import org.example.loficonnect.feignclients.CollectionClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CollectionService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CollectionServiceImpl implements CollectionService {

    private final CollectionClient collectionClient;
    private final AuthorizationService authorizationService;

    public CollectionServiceImpl(CollectionClient collectionClient, AuthorizationService authorizationService) {
        this.collectionClient = collectionClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getProductCollections(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return collectionClient.getProductCollections(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createCollection(CollectionCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCollectionCreateRequest ghlRequest = GoHighLevelCollectionCreateRequest.fromRequest(request);
        return collectionClient.createCollection(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getCollectionById(String collectionId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return collectionClient.getCollectionById(accessKey, version, collectionId);
    }

    @Override
    public JsonNode updateCollection(String collectionId, CollectionUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCollectionUpdateRequest ghlRequest = GoHighLevelCollectionUpdateRequest.fromRequest(request);
        return collectionClient.updateCollection(accessKey, version, collectionId, ghlRequest);
    }

    @Override
    public JsonNode deleteCollection(String collectionId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return collectionClient.deleteCollection(accessKey, version, collectionId, queryParams);
    }

}
