package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.snapshot.SnapshotShareLinkCreateRequest;
import org.example.loficonnect.service.SnapshotService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class SnapshotController {

    private final SnapshotService snapshotService;

    public SnapshotController(SnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @AppKey
    @GetMapping("/snapshots")
    public ResponseEntity<?> getSnapshots(@RequestParam("company-id") String companyId) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "companyId", companyId);

        return ResponseEntity.ok(snapshotService.getSnapshots(queryParams));
    }


    @AppKey
    @PostMapping("/snapshots/share")
    public ResponseEntity<?> createSnapshotShareLink(@RequestBody SnapshotShareLinkCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(snapshotService.createSnapshotShareLink(request));
    }

    @AppKey
    @GetMapping("/snapshots/status/{snapshot-id}")
    public ResponseEntity<?> getSnapshotPushStatus(
            @PathVariable("snapshot-id") String snapshotId,
            @RequestParam("company-id") String companyId,
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("last-doc") String lastDoc,
            @RequestParam("limit") String limit
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "companyId", companyId);
        MapUtil.putIfNotNull(queryParams, "from", from);
        MapUtil.putIfNotNull(queryParams, "to", to);
        MapUtil.putIfNotNull(queryParams, "lastDoc", lastDoc);
        MapUtil.putIfNotNull(queryParams, "limit", limit);

        return ResponseEntity.ok(snapshotService.getSnapshotPushStatus(snapshotId, queryParams));
    }

    @AppKey
    @GetMapping("/snapshots/status/{snapshot-id}/location/{location-id}")
    public ResponseEntity<?> getLastSnapshotPush(
            @PathVariable("snapshot-id") String snapshotId,
            @PathVariable("location-id") String locationId,
            @RequestParam("company-id") String companyId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "companyId", companyId);

        return ResponseEntity.ok(snapshotService.getLastSnapshotPush(snapshotId, locationId, queryParams));
    }

}
