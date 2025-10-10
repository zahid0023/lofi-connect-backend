package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.oauthfacebook.GoHighLevelAttachFacebookPageRequest;
import org.example.loficonnect.dto.request.oauthfacebook.AttachFacebookPageRequest;
import org.example.loficonnect.feignclients.OauthFacebookClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.OauthFacebookService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OauthFacebookServiceImpl implements OauthFacebookService {

    private final OauthFacebookClient oauthFacebookClient;
    private final AuthorizationService authorizationService;

    public OauthFacebookServiceImpl(OauthFacebookClient oauthFacebookClient, AuthorizationService authorizationService) {
        this.oauthFacebookClient = oauthFacebookClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode startOauthFacebook(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthFacebookClient.startOauthFacebook(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getFacebookPages(String locationId, String accountId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return oauthFacebookClient.getFacebookPages(accessKey, version, locationId, accountId);
    }

    @Override
    public JsonNode attachFacebookPage(String locationId, String accountId, AttachFacebookPageRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelAttachFacebookPageRequest ghlRequest = GoHighLevelAttachFacebookPageRequest.fromRequest(request);
        return oauthFacebookClient.attachFacebookPage(accessKey, version, locationId, accountId, ghlRequest);
    }

}
