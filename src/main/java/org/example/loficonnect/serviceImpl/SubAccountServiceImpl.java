package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.subaccount.GoHighLevelLocationCreateRequest;
import org.example.loficonnect.dto.mapper.subaccount.GoHighLevelLocationUpdateRequest;
import org.example.loficonnect.dto.request.subaccount.LocationCreateRequest;
import org.example.loficonnect.dto.request.subaccount.LocationUpdateRequest;
import org.example.loficonnect.feignclients.SubAccountClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.SubAccountService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SubAccountServiceImpl implements SubAccountService {
    private final AuthorizationService authorizationService;
    private final SubAccountClient subAccountClient;
    private final ObjectMapper objectMapper;

    public SubAccountServiceImpl(AuthorizationService authorizationService,
                                 SubAccountClient subAccountClient,
                                 ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.subAccountClient = subAccountClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode getLocation(String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return subAccountClient.getLocation(accessKey, version, locationId);
    }

    @Override
    public JsonNode updateLocation(String locationId, LocationUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelLocationUpdateRequest ghlRequest = GoHighLevelLocationUpdateRequest.fromRequest(request);
        return subAccountClient.updateLocation(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode deleteLocation(String locationId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return subAccountClient.deleteLocation(accessKey, version, locationId, queryParams);
    }

    @Override
    public JsonNode createLocation(LocationCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelLocationCreateRequest ghlRequest = GoHighLevelLocationCreateRequest.fromRequest(request, objectMapper);
        return subAccountClient.createLocation(accessKey, version, ghlRequest);
    }


}
