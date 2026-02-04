package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.snapshot.GoHighLevelSnapshotShareLinkCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "snapshotClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface SnapshotClient {

    @GetMapping(
        value = "/snapshots/",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getSnapshots(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/snapshots/share/link",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createSnapshotShareLink(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelSnapshotShareLinkCreateRequest request
    );

    @GetMapping(
            value = "/snapshots/snapshot-status/{snapshotId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getSnapshotPushStatus(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("snapshotId") String snapshotId,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/snapshots/snapshot-status/{snapshotId}/location/{locationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getLastSnapshotPush(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("snapshotId") String snapshotId,
            @PathVariable("locationId") String locationId,
            @RequestParam Map<String, Object> queryParams
    );

}
