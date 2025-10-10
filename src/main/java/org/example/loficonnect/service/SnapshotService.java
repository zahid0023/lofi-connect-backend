package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.snapshot.SnapshotShareLinkCreateRequest;

import java.util.Map;

public interface SnapshotService {
    JsonNode getSnapshots(Map<String, Object> queryParams);
    JsonNode createSnapshotShareLink(SnapshotShareLinkCreateRequest request);
    JsonNode getSnapshotPushStatus(String snapshotId, Map<String, Object> queryParams);
    JsonNode getLastSnapshotPush(String snapshotId, String locationId, Map<String, Object> queryParams);
}
