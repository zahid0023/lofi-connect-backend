package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.oauthtiktok.GoHighLevelOauthtiktokAttachRequest;
import org.example.loficonnect.dto.request.oauthtiktok.OauthtiktokAttachRequest;
import org.example.loficonnect.feignclients.OauthtiktokClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OauthtiktokService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OauthtiktokServiceImpl implements OauthtiktokService {

    private final OauthtiktokClient oauthtiktokClient;
    private final AuthorizationService authorizationService;

    public OauthtiktokServiceImpl(OauthtiktokClient oauthtiktokClient, AuthorizationService authorizationService) {
        this.oauthtiktokClient = oauthtiktokClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode startTiktokOAuth(Map<String, Object> queryParams) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthtiktokClient.startTiktokOAuth(accessToken, version, queryParams);
    }

    @Override
    public JsonNode getTiktokProfile(String locationId, String accountId) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthtiktokClient.getTiktokProfile(accessToken, version, locationId, accountId);
    }

    @Override
    public JsonNode attachTiktokProfile(String locationId, String accountId, OauthtiktokAttachRequest request) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelOauthtiktokAttachRequest ghlRequest = GoHighLevelOauthtiktokAttachRequest.fromRequest(request);
        return oauthtiktokClient.attachTiktokProfile(accessToken, version, locationId, accountId, ghlRequest);
    }

    @Override
    public JsonNode startTiktokBusinessOAuth(Map<String, Object> queryParams) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthtiktokClient.startTiktokBusinessOAuth(accessToken, version, queryParams);
    }

    @Override
    public JsonNode getTiktokBusinessProfile(String locationId, String accountId) {
        String accessToken = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthtiktokClient.getTiktokBusinessProfile(accessToken, version, locationId, accountId);
    }

}
