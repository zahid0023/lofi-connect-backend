package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.redirect.RedirectCreateRequest;
import org.example.loficonnect.dto.request.redirect.RedirectUpdateRequest;
import org.example.loficonnect.service.RedirectService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class RedirectController {

    private final RedirectService redirectService;

    public RedirectController(RedirectService redirectService) {
        this.redirectService = redirectService;
    }

    @AppKey
    @PostMapping("/funnels/redirects")
    public ResponseEntity<?> createRedirect(@RequestBody RedirectCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(redirectService.createRedirect(request));
    }

    @AppKey
    @PatchMapping("/funnels/redirects/{id}")
    public ResponseEntity<?> updateRedirect(
            @PathVariable("id") String id,
            @RequestBody RedirectUpdateRequest request
    ) {
        return ResponseEntity.ok(redirectService.updateRedirect(id, request));
    }

    @AppKey
    @DeleteMapping("/funnels/redirects/{id}")
    public ResponseEntity<?> deleteRedirectById(
            @PathVariable("id") String id,
            @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        return ResponseEntity.ok(redirectService.deleteRedirectById(id, queryParams));
    }

    @AppKey
    @GetMapping("/funnels/redirects/list")
    public ResponseEntity<?> getRedirectList(
            @RequestParam("location-id") String locationId,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @RequestParam(value = "search", required = false) String search
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "search", search);
        return ResponseEntity.ok(redirectService.getRedirectList(queryParams));
    }

}
