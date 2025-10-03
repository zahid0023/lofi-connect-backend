package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.FunnelService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/funnels")
public class FunnelController {

    private final FunnelService funnelService;

    public FunnelController(FunnelService funnelService) {
        this.funnelService = funnelService;
    }

    @AppKey
    @GetMapping("/list")
    public ResponseEntity<?> getFunnelList(
        @RequestParam("location-id") String locationId,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "limit", required = false) String limit,
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "offset", required = false) String offset,
        @RequestParam(value = "parent-id", required = false) String parentId,
        @RequestParam(value = "type", required = false) String type
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "category", category);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "name", name);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "parentId", parentId);
        MapUtil.putIfNotNull(queryParams, "type", type);

        return ResponseEntity.ok(funnelService.getFunnelList(queryParams));
    }

    @AppKey
    @GetMapping("/pages")
    public ResponseEntity<?> getFunnelPages(
            @RequestParam("location-id") String locationId,
            @RequestParam("funnel-id") String funnelId,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @RequestParam(value = "name", required = false) String name
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "funnelId", funnelId);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "name", name);

        return ResponseEntity.ok(funnelService.getFunnelPages(queryParams));
    }

    @AppKey
    @GetMapping("/pages/count")
    public ResponseEntity<?> getFunnelPageCount(
            @RequestParam("location-id") String locationId,
            @RequestParam("funnel-id") String funnelId,
            @RequestParam(value = "name", required = false) String name
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "funnelId", funnelId);
        MapUtil.putIfNotNull(queryParams, "name", name);

        return ResponseEntity.ok(funnelService.getFunnelPageCount(queryParams));
    }

}
