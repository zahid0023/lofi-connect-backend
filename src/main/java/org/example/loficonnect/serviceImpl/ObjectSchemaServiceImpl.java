package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.objectschema.GoHighLevelCreateCustomObjectRequest;
import org.example.loficonnect.dto.mapper.objectschema.GoHighLevelUpdateObjectSchemaRequest;
import org.example.loficonnect.dto.request.objectschema.CustomObjectCreateRequest;
import org.example.loficonnect.dto.request.objectschema.ObjectSchemaUpdateRequest;
import org.example.loficonnect.feignclients.ObjectSchemaClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.ObjectSchemaService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ObjectSchemaServiceImpl implements ObjectSchemaService {

    private final ObjectSchemaClient objectSchemaClient;
    private final AuthorizationService authorizationService;

    public ObjectSchemaServiceImpl(ObjectSchemaClient objectSchemaClient, AuthorizationService authorizationService) {
        this.objectSchemaClient = objectSchemaClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getObjectSchema(String key, String locationId, String fetchProperties) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return objectSchemaClient.getObjectSchema(accessKey, version, key, fetchProperties, locationId);
    }

    @Override
    public JsonNode updateObjectSchema(String key, ObjectSchemaUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelUpdateObjectSchemaRequest ghlRequest = GoHighLevelUpdateObjectSchemaRequest.fromRequest(request);

        return objectSchemaClient.updateObjectSchema(accessKey, version, key, ghlRequest);
    }

    @Override
    public JsonNode getAllObjects(String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return objectSchemaClient.getAllObjects(accessKey, version, locationId);
    }

    @Override
    public JsonNode createCustomObject(CustomObjectCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCreateCustomObjectRequest ghlRequest = GoHighLevelCreateCustomObjectRequest.fromRequest(request);
        return objectSchemaClient.createCustomObject(accessKey, version, ghlRequest);
    }
}
