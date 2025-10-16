package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.oauthtiktok.OauthtiktokAttachRequest;
import org.example.loficonnect.service.OauthtiktokService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class OauthtiktokController {

    private final OauthtiktokService oauthtiktokService;

    public OauthtiktokController(OauthtiktokService oauthtiktokService) {
        this.oauthtiktokService = oauthtiktokService;
    }

    @AppKey
    @GetMapping("/oauth/tiktok/start")
    public ResponseEntity<?> startOAuth(
        @RequestParam("location-id") String locationId,
        @RequestParam("user-id") String userId,
        @RequestParam(value = "page", required = false) String page,
        @RequestParam(value = "reconnect", required = false) String reconnect
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "userId", userId);
        MapUtil.putIfNotNull(queryParams, "page", page);
        MapUtil.putIfNotNull(queryParams, "reconnect", reconnect);

        return ResponseEntity.ok(oauthtiktokService.startTiktokOAuth(queryParams));
    }

    @AppKey
    @GetMapping("/oauth/tiktok/{location-id}/accounts/{account-id}")
    public ResponseEntity<?> getTiktokProfile(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId
    ) {
        return ResponseEntity.ok(oauthtiktokService.getTiktokProfile(locationId, accountId));
    }

    @AppKey
    @PostMapping("/oauth/tiktok/{location-id}/accounts/{account-id}")
    public ResponseEntity<?> attachTiktokProfile(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId,
            @RequestBody OauthtiktokAttachRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oauthtiktokService.attachTiktokProfile(locationId, accountId, request));
    }

    @AppKey
    @GetMapping("/oauth/tiktok/tiktok-business/start")
    public ResponseEntity<?> startTiktokBusinessOAuth(
            @RequestParam("location-id") String locationId,
            @RequestParam("user-id") String userId,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "reconnect", required = false) String reconnect
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "userId", userId);
        MapUtil.putIfNotNull(queryParams, "page", page);
        MapUtil.putIfNotNull(queryParams, "reconnect", reconnect);

        return ResponseEntity.ok(oauthtiktokService.startTiktokBusinessOAuth(queryParams));
    }

    @AppKey
    @GetMapping("/oauth/tiktok/{location-id}/tiktok-business/accounts/{account-id}")
    public ResponseEntity<?> getTiktokBusinessProfile(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId
    ) {
        return ResponseEntity.ok(oauthtiktokService.getTiktokBusinessProfile(locationId, accountId));
    }

}
