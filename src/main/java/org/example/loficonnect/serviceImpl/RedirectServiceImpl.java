package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.redirect.GoHighLevelRedirectCreateRequest;
import org.example.loficonnect.dto.mapper.redirect.GoHighLevelRedirectUpdateRequest;
import org.example.loficonnect.dto.request.redirect.RedirectCreateRequest;
import org.example.loficonnect.dto.request.redirect.RedirectUpdateRequest;
import org.example.loficonnect.feignclients.RedirectClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.RedirectService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class RedirectServiceImpl implements RedirectService {

    private final RedirectClient redirectClient;
    private final AuthorizationService authorizationService;

    public RedirectServiceImpl(RedirectClient redirectClient, AuthorizationService authorizationService) {
        this.redirectClient = redirectClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createRedirect(RedirectCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelRedirectCreateRequest ghlRequest = GoHighLevelRedirectCreateRequest.fromRequest(request);
        return redirectClient.createRedirect(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode updateRedirect(String id, RedirectUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelRedirectUpdateRequest ghlRequest = GoHighLevelRedirectUpdateRequest.fromRequest(request);
        return redirectClient.updateRedirect(accessKey, version, id, ghlRequest);
    }

    @Override
    public JsonNode deleteRedirectById(String id, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return redirectClient.deleteRedirectById(accessKey, version, id, queryParams);
    }

    @Override
    public JsonNode getRedirectList(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return redirectClient.getRedirectList(accessKey, version, queryParams);
    }

}
