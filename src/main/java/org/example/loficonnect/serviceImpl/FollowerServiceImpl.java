package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.follower.GoHighLevelAddFollowersRequest;
import org.example.loficonnect.dto.mapper.follower.GoHighLevelRemoveFollowersRequest;
import org.example.loficonnect.dto.request.follower.AddFollowersRequest;
import org.example.loficonnect.dto.request.follower.RemoveFollowersRequest;
import org.example.loficonnect.feignclients.FollowerClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.FollowerService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FollowerServiceImpl implements FollowerService {
    private final AuthorizationService authorizationService;
    private final FollowerClient followerClient;
    private final ObjectMapper objectMapper;

    public FollowerServiceImpl(AuthorizationService authorizationService,
                               FollowerClient followerClient,
                               ObjectMapper objectMapper) {
        this.authorizationService = authorizationService;
        this.followerClient = followerClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode addFollowers(String contactId, AddFollowersRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelAddFollowersRequest ghlRequest = GoHighLevelAddFollowersRequest.fromRequest(request, objectMapper);
        return followerClient.addFollowers(accessKey, version, ghlRequest, contactId);
    }

    @Override
    public JsonNode removeFollowers(String contactId, RemoveFollowersRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelRemoveFollowersRequest ghlRequest = GoHighLevelRemoveFollowersRequest.fromRequest(request, objectMapper);
        return followerClient.removeFollowers(accessKey, version, contactId, ghlRequest);
    }
}
