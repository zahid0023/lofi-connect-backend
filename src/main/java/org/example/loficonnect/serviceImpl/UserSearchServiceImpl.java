package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.usersearch.GoHighLevelUserSearchRequest;
import org.example.loficonnect.dto.request.usersearch.UserSearchRequest;
import org.example.loficonnect.feignclients.UserSearchClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.UserSearchService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class UserSearchServiceImpl implements UserSearchService {

    private final UserSearchClient userSearchClient;
    private final AuthorizationService authorizationService;

    public UserSearchServiceImpl(UserSearchClient userSearchClient, AuthorizationService authorizationService) {
        this.userSearchClient = userSearchClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode searchUsers(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return userSearchClient.searchUsers(accessKey, version, queryParams);
    }

    @Override
    public JsonNode filterUsersByEmail(UserSearchRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelUserSearchRequest ghlRequest = GoHighLevelUserSearchRequest.fromRequest(request);
        return userSearchClient.filterUsersByEmail(accessKey, version, ghlRequest);
    }
}
