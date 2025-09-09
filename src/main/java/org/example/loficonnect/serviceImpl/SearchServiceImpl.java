package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.search.GoHighLevelContactSearchRequest;
import org.example.loficonnect.dto.mapper.search.GoHighLevelTaskSearchRequest;
import org.example.loficonnect.dto.request.search.ContactSearchRequest;
import org.example.loficonnect.dto.request.search.TaskSearchRequest;
import org.example.loficonnect.feignclients.SearchClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.SearchService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final AuthorizationService authorizationService;
    private final SearchClient searchClient;

    public SearchServiceImpl(AuthorizationService authorizationService, SearchClient searchClient) {
        this.authorizationService = authorizationService;
        this.searchClient = searchClient;
    }

    @Override
    public JsonNode searchContacts(ContactSearchRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelContactSearchRequest ghlRequest = GoHighLevelContactSearchRequest.fromRequest(request);
        return searchClient.searchContacts(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getDuplicateContacts(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return searchClient.getDuplicateContacts(accessKey, version, queryParams);
    }

    @Override
    public JsonNode searchLocations(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return searchClient.searchLocations(accessKey, version, queryParams);
    }

    @Override
    public JsonNode searchTasks(String locationId, TaskSearchRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelTaskSearchRequest ghlRequest = GoHighLevelTaskSearchRequest.fromRequest(request);
        return searchClient.searchTasks(accessKey, version, locationId, ghlRequest);
    }

    @Override
    public JsonNode searchConversations(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return searchClient.searchConversations(accessKey, version, queryParams);
    }


}
