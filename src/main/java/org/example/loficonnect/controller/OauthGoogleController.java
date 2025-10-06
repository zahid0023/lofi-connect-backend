package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.oauthgoogle.OauthGoogleBusinessLocationRequest;
import org.example.loficonnect.service.OauthGoogleService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/oauth/google")
public class OauthGoogleController {

    private final OauthGoogleService oauthGoogleService;

    public OauthGoogleController(OauthGoogleService oauthGoogleService) {
        this.oauthGoogleService = oauthGoogleService;
    }

    @AppKey
    @GetMapping("/start")
    public ResponseEntity<?> startOauth(
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

        return ResponseEntity.ok(oauthGoogleService.startOauth(queryParams));
    }

    @AppKey
    @GetMapping("/locations/{location-id}/accounts/{account-id}/google/locations")
    public ResponseEntity<?> getGoogleBusinessLocations(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId
    ) {
        return ResponseEntity.ok(oauthGoogleService.getGoogleBusinessLocations(locationId, accountId));
    }

    @AppKey
    @PostMapping("/locations/{location-id}/accounts/{account-id}/google/locations")
    public ResponseEntity<?> setGoogleBusinessLocation(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId,
            @RequestBody OauthGoogleBusinessLocationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(oauthGoogleService.setGoogleBusinessLocation(locationId, accountId, request));
    }

}
