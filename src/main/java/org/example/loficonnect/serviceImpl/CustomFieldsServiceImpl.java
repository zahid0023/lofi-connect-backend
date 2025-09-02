package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.custom.fields.GoHighLevelCustomFieldCreateRequest;
import org.example.loficonnect.dto.mapper.custom.fields.GoHighLevelCustomFieldUpdateRequest;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldCreateRequest;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldUpdateRequest;
import org.example.loficonnect.dto.request.custom.fields.UploadCustomFieldFileRequest;
import org.example.loficonnect.feignclients.CustomFieldsClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CustomFieldsService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CustomFieldsServiceImpl implements CustomFieldsService {
    private final AuthorizationService authorizationService;
    private final CustomFieldsClient customFieldsClient;

    public CustomFieldsServiceImpl(AuthorizationService authorizationService, CustomFieldsClient customFieldsClient) {
        this.authorizationService = authorizationService;
        this.customFieldsClient = customFieldsClient;
    }

    @Override
    public JsonNode getCustomFields(String locationId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customFieldsClient.getCustomFields(accessKey, version, locationId, queryParams);
    }

    @Override
    public JsonNode createCustomField(String locationId, CustomFieldCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomFieldCreateRequest ghlRequest = GoHighLevelCustomFieldCreateRequest.fromRequest(request);
        return customFieldsClient.createCustomField(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode getCustomField(String locationId, String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customFieldsClient.getCustomField(accessKey, version, locationId, id);
    }

    @Override
    public JsonNode updateCustomField(String locationId, String id, CustomFieldUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomFieldUpdateRequest ghlRequest = GoHighLevelCustomFieldUpdateRequest.fromRequest(request);
        return customFieldsClient.updateCustomField(accessKey, version, locationId, id, ghlRequest);
    }

    @Override
    public JsonNode deleteCustomField(String locationId, String id) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customFieldsClient.deleteCustomField(accessKey, version, locationId, id);
    }

    @Override
    public JsonNode uploadCustomFieldFile(String locationId, UploadCustomFieldFileRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customFieldsClient.uploadCustomFieldFile(
                accessKey,
                version,
                locationId,
                request.getId(),
                request.getMaxFiles(),
                request.getFile()
        );
    }


}
