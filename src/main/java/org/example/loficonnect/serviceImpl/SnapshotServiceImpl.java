package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.snapshot.GoHighLevelSnapshotShareLinkCreateRequest;
import org.example.loficonnect.dto.request.snapshot.SnapshotShareLinkCreateRequest;
import org.example.loficonnect.feignclients.SnapshotClient;
import org.example.loficonnect.service.SnapshotService;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SnapshotServiceImpl implements SnapshotService {

    private final SnapshotClient snapshotClient;
    private final AuthorizationService authorizationService;

    public SnapshotServiceImpl(SnapshotClient snapshotClient, AuthorizationService authorizationService) {
        this.snapshotClient = snapshotClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getSnapshots(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return snapshotClient.getSnapshots(accessKey, version, queryParams);
    }

    @Override
    public JsonNode createSnapshotShareLink(SnapshotShareLinkCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelSnapshotShareLinkCreateRequest ghlRequest = GoHighLevelSnapshotShareLinkCreateRequest.fromRequest(request);
        return snapshotClient.createSnapshotShareLink(accessKey, version, ghlRequest);
    }

    @Override
    public JsonNode getSnapshotPushStatus(String snapshotId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return snapshotClient.getSnapshotPushStatus(accessKey, version, snapshotId, queryParams);
    }

    @Override
    public JsonNode getLastSnapshotPush(String snapshotId, String locationId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return snapshotClient.getLastSnapshotPush(accessKey, version, snapshotId, locationId, queryParams);
    }

}
