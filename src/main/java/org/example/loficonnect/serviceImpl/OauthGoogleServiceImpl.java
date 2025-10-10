package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.oauthgoogle.GoHighLevelOauthGoogleBusinessLocationRequest;
import org.example.loficonnect.dto.request.oauthgoogle.OauthGoogleBusinessLocationRequest;
import org.example.loficonnect.feignclients.OauthGoogleClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OauthGoogleService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OauthGoogleServiceImpl implements OauthGoogleService {

    private final OauthGoogleClient oauthGoogleClient;
    private final AuthorizationService authorizationService;

    public OauthGoogleServiceImpl(OauthGoogleClient oauthGoogleClient, AuthorizationService authorizationService) {
        this.oauthGoogleClient = oauthGoogleClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode startOauth(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthGoogleClient.startOauth(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getGoogleBusinessLocations(String locationId, String accountId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthGoogleClient.getGoogleBusinessLocations(accessKey, version, locationId, accountId);
    }

    @Override
    public JsonNode setGoogleBusinessLocation(String locationId, String accountId, OauthGoogleBusinessLocationRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOauthGoogleBusinessLocationRequest ghlRequest = GoHighLevelOauthGoogleBusinessLocationRequest.fromRequest(request);
        return oauthGoogleClient.setGoogleBusinessLocation(accessKey, version, locationId, accountId, ghlRequest);
    }

}
