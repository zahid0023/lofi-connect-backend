package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.oauthlinkedin.GhlAttachLinkedinAccountRequest;
import org.example.loficonnect.dto.request.oauthlinkedin.AttachLinkedinAccountRequest;
import org.example.loficonnect.feignclients.OauthLinkedinClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OauthLinkedinService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OauthLinkedinServiceImpl implements OauthLinkedinService {

    private final OauthLinkedinClient oauthLinkedinClient;
    private final AuthorizationService authorizationService;

    public OauthLinkedinServiceImpl(OauthLinkedinClient oauthLinkedinClient, AuthorizationService authorizationService) {
        this.oauthLinkedinClient = oauthLinkedinClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode startOauthLinkedin(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthLinkedinClient.startOauthLinkedin(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getLinkedinAccounts(String locationId, String accountId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthLinkedinClient.getLinkedinAccounts(accessKey, version, locationId, accountId);
    }

    @Override
    public JsonNode attachLinkedinAccount(String locationId, String accountId, AttachLinkedinAccountRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GhlAttachLinkedinAccountRequest ghlRequest = GhlAttachLinkedinAccountRequest.fromRequest(request);
        return oauthLinkedinClient.attachLinkedinAccount(accessKey, version, locationId, accountId, ghlRequest);
    }

}
