package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.user.GoHighLevelUserCreateRequest;
import org.example.loficonnect.dto.mapper.user.GoHighLevelUserUpdateRequest;
import org.example.loficonnect.dto.request.user.UserCreateRequest;
import org.example.loficonnect.dto.request.user.UserUpdateRequest;
import org.example.loficonnect.feignclients.UserClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.UserService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.LocationContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserClient userClient;
    private final AuthorizationService authorizationService;
    private final ObjectMapper objectMapper;

    public UserServiceImpl(UserClient userClient,
                           AuthorizationService authorizationService,
                           ObjectMapper objectMapper) {
        this.userClient = userClient;
        this.authorizationService = authorizationService;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode getUserById(String userId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return userClient.getUserById(accessKey, version, userId);
    }

    @Override
    public JsonNode updateUserById(String userId, UserUpdateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelUserUpdateRequest ghlRequest = GoHighLevelUserUpdateRequest.fromRequest(request, objectMapper);
        return userClient.updateUserById(accessKey, version, userId, ghlRequest);
    }

    @Override
    public JsonNode deleteUserById(String userId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return userClient.deleteUserById(accessKey, version, userId);
    }

    @Override
    public JsonNode getUsersByLocation(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        queryParams.put("location_id", LocationContext.getLocationId());
        return userClient.getUsersByLocation(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createUser(UserCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelUserCreateRequest ghlRequest = GoHighLevelUserCreateRequest.fromRequest(request, objectMapper);
        return userClient.createUser(accessKey, version, ghlRequest);
    }

}
