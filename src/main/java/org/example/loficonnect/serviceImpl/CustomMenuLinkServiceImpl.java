package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.custommenulink.GoHighLevelCustomMenuLinkCreateRequest;
import org.example.loficonnect.dto.mapper.custommenulink.GoHighLevelCustomMenuLinkUpdateRequest;
import org.example.loficonnect.dto.request.custommenulink.CustomMenuLinkCreateRequest;
import org.example.loficonnect.dto.request.custommenulink.CustomMenuLinkUpdateRequest;
import org.example.loficonnect.feignclients.CustomMenuLinkClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CustomMenuLinkService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CustomMenuLinkServiceImpl implements CustomMenuLinkService {

    private final CustomMenuLinkClient customMenuLinkClient;
    private final AuthorizationService authorizationService;

    public CustomMenuLinkServiceImpl(CustomMenuLinkClient customMenuLinkClient, AuthorizationService authorizationService) {
        this.customMenuLinkClient = customMenuLinkClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getCustomMenuLink(String customMenuId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customMenuLinkClient.getCustomMenuLink(accessKey, version, customMenuId);
    }

    @Override
    public JsonNode deleteCustomMenuLink(String customMenuId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customMenuLinkClient.deleteCustomMenuLink(accessKey, version, customMenuId);
    }

    @Override
    public JsonNode updateCustomMenuLink(String customMenuId, CustomMenuLinkUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomMenuLinkUpdateRequest ghlRequest = GoHighLevelCustomMenuLinkUpdateRequest.fromRequest(request);
        return customMenuLinkClient.updateCustomMenuLink(accessKey, version, customMenuId, ghlRequest);
    }

    @Override
    public JsonNode getCustomMenuLinks(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return customMenuLinkClient.getCustomMenuLinks(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createCustomMenuLink(CustomMenuLinkCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelCustomMenuLinkCreateRequest ghlRequest = GoHighLevelCustomMenuLinkCreateRequest.fromRequest(request);
        return customMenuLinkClient.createCustomMenuLink(accessKey, version, ghlRequest);
    }

}
