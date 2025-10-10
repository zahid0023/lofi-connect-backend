package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.oauthfacebook.AttachFacebookPageRequest;
import org.example.loficonnect.service.OauthFacebookService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/oauth/facebook")
public class OauthFacebookController {

    private final OauthFacebookService oauthFacebookService;

    public OauthFacebookController(OauthFacebookService oauthFacebookService) {
        this.oauthFacebookService = oauthFacebookService;
    }

    @AppKey
    @GetMapping("/start")
    public ResponseEntity<?> startOauthFacebook(
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

        return ResponseEntity.ok(oauthFacebookService.startOauthFacebook(queryParams));
    }

    @AppKey
    @GetMapping("/locations/{location-id}/accounts/{account-id}/facebook/pages")
    public ResponseEntity<?> getFacebookPages(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId
    ) {
        return ResponseEntity.ok(oauthFacebookService.getFacebookPages(locationId, accountId));
    }

    @AppKey
    @PostMapping("/locations/{location-id}/accounts/{account-id}/facebook/attach")
    public ResponseEntity<?> attachFacebookPage(
            @PathVariable("location-id") String locationId,
            @PathVariable("account-id") String accountId,
            @RequestBody AttachFacebookPageRequest request
    ) {
        return ResponseEntity.ok(oauthFacebookService.attachFacebookPage(locationId, accountId, request));
    }

}
