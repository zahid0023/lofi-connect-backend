package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.oauthlinkedin.AttachLinkedinAccountRequest;
import org.example.loficonnect.service.OauthLinkedinService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class OauthLinkedinController {

    private final OauthLinkedinService oauthLinkedinService;

    public OauthLinkedinController(OauthLinkedinService oauthLinkedinService) {
        this.oauthLinkedinService = oauthLinkedinService;
    }

    @AppKey
    @GetMapping("/oauth/linkedin/start")
    public ResponseEntity<?> startOauthLinkedin(
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

        return ResponseEntity.ok(oauthLinkedinService.startOauthLinkedin(queryParams));
    }

    @AppKey
    @GetMapping("/oauth/linkedin/{location-id}/accounts/{account-id}")
    public ResponseEntity<?> getLinkedinAccounts(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId
    ) {
        return ResponseEntity.ok(oauthLinkedinService.getLinkedinAccounts(locationId, accountId));
    }

    @AppKey
    @PostMapping("/oauth/linkedin/{location-id}/accounts/{account-id}")
    public ResponseEntity<?> attachLinkedinAccount(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId,
            @RequestBody AttachLinkedinAccountRequest request
    ) {
        return ResponseEntity.ok(oauthLinkedinService.attachLinkedinAccount(locationId, accountId, request));
    }

}
