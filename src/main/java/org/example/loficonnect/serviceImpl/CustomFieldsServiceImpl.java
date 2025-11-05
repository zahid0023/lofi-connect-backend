package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.custom.fields.GoHighLevelCustomFieldCreateRequest;
import org.example.loficonnect.dto.mapper.custom.fields.GoHighLevelCustomFieldUpdateRequest;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldCreateRequest;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldUpdateRequest;
import org.example.loficonnect.dto.request.custom.fields.UploadCustomFieldFileRequest;
import org.example.loficonnect.dto.response.customfields.CustomFieldsResponse;
import org.example.loficonnect.feignclients.CustomFieldsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CustomFieldsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CustomFieldsServiceImpl implements CustomFieldsService {
    private final AuthorizationService authorizationService;
    private final CustomFieldsClient customFieldsClient;
    private final ObjectMapper objectMapper;

    public CustomFieldsServiceImpl(AuthorizationService authorizationService,
                                   CustomFieldsClient customFieldsClient,
                                   ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.customFieldsClient = customFieldsClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode getCustomFields(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customFieldsClient.getCustomFields(accessKey, version, LocationContext.getLocationId(), queryParams);
    }

    @Override
    public CustomFieldsResponse getCustomFieldsTypes(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        JsonNode customFieldsNode = customFieldsClient.getCustomFields(accessKey, version, LocationContext.getLocationId(), queryParams);
        return CustomFieldsResponse.fromJson(customFieldsNode.get("customFields"), objectMapper);
    }

    @Override
    public JsonNode createCustomField(CustomFieldCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomFieldCreateRequest ghlRequest = GoHighLevelCustomFieldCreateRequest.fromRequest(request, objectMapper);
        return customFieldsClient.createCustomField(accessKey, version, LocationContext.getLocationId(), ghlRequest);
    }

    @Override
    public JsonNode getCustomField(String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customFieldsClient.getCustomField(accessKey, version, LocationContext.getLocationId(), id);
    }

    @Override
    public JsonNode updateCustomField(String id, CustomFieldUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomFieldUpdateRequest ghlRequest = GoHighLevelCustomFieldUpdateRequest.fromRequest(request, objectMapper);
        return customFieldsClient.updateCustomField(accessKey, version, LocationContext.getLocationId(), id, ghlRequest);
    }

    @Override
    public JsonNode deleteCustomField(String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customFieldsClient.deleteCustomField(accessKey, version, LocationContext.getLocationId(), id);
    }

    @Override
    public JsonNode uploadCustomFieldFile(UploadCustomFieldFileRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customFieldsClient.uploadCustomFieldFile(
                accessKey,
                version,
                LocationContext.getLocationId(),
                request.getId(),
                request.getMaxFiles(),
                request.getFile()
        );
    }
}
