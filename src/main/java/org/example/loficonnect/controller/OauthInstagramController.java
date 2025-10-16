package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.oauthinstagram.AttachInstagramAccountRequest;
import org.example.loficonnect.service.OauthInstagramService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class OauthInstagramController {

    private final OauthInstagramService oauthInstagramService;

    public OauthInstagramController(OauthInstagramService oauthInstagramService) {
        this.oauthInstagramService = oauthInstagramService;
    }

    @AppKey
    @GetMapping("/oauth/instagram/start")
    public ResponseEntity<?> startOauthInstagram(
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

        return ResponseEntity.ok(oauthInstagramService.startOauthInstagram(queryParams));
    }

    @AppKey
    @GetMapping("/oauth/instagram/locations/{location-id}/accounts/{account-id}/instagram/accounts")
    public ResponseEntity<?> getInstagramProfessionalAccounts(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId
    ) {
        return ResponseEntity.ok(oauthInstagramService.getInstagramProfessionalAccounts(locationId, accountId));
    }

    @AppKey
    @PostMapping("/oauth/instagram/locations/{location-id}/accounts/{account-id}/instagram/attach")
    public ResponseEntity<?> attachInstagramAccount(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId,
            @RequestBody AttachInstagramAccountRequest request
    ) {
        return ResponseEntity.ok(oauthInstagramService.attachInstagramAccount(locationId, accountId, request));
    }

}
