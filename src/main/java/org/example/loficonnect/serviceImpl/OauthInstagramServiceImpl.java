package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.oauthinstagram.GoHighLevelAttachInstagramAccountRequest;
import org.example.loficonnect.dto.request.oauthinstagram.AttachInstagramAccountRequest;
import org.example.loficonnect.feignclients.OauthInstagramClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OauthInstagramService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OauthInstagramServiceImpl implements OauthInstagramService {

    private final OauthInstagramClient oauthInstagramClient;
    private final AuthorizationService authorizationService;

    public OauthInstagramServiceImpl(OauthInstagramClient oauthInstagramClient, AuthorizationService authorizationService) {
        this.oauthInstagramClient = oauthInstagramClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode startOauthInstagram(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthInstagramClient.startOauthInstagram(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getInstagramProfessionalAccounts(String locationId, String accountId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthInstagramClient.getInstagramProfessionalAccounts(accessKey, version, locationId, accountId);
    }

    @Override
    public JsonNode attachInstagramAccount(String locationId, String accountId, AttachInstagramAccountRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelAttachInstagramAccountRequest ghlRequest = GoHighLevelAttachInstagramAccountRequest.fromRequest(request);
        return oauthInstagramClient.attachInstagramAccount(accessKey, version, locationId, accountId, ghlRequest);
    }

}
