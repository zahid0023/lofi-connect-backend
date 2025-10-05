package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.custommenulink.CustomMenuLinkCreateRequest;
import org.example.loficonnect.dto.request.custommenulink.CustomMenuLinkUpdateRequest;
import org.example.loficonnect.service.CustomMenuLinkService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/custom-menu-links")
public class CustomMenuLinkController {

    private final CustomMenuLinkService customMenuLinkService;

    public CustomMenuLinkController(CustomMenuLinkService customMenuLinkService) {
        this.customMenuLinkService = customMenuLinkService;
    }

    @AppKey
    @GetMapping("/{custom-menu-id}")
    public ResponseEntity<?> getCustomMenuLink(@PathVariable("custom-menu-id") String customMenuId) {
        return ResponseEntity.ok(customMenuLinkService.getCustomMenuLink(customMenuId));
    }

    @AppKey
    @DeleteMapping("/{custom-menu-id}")
    public ResponseEntity<?> deleteCustomMenuLink(@PathVariable("custom-menu-id") String customMenuId) {
        return ResponseEntity.ok(customMenuLinkService.deleteCustomMenuLink(customMenuId));
    }

    @AppKey
    @PutMapping("/{custom-menu-id}")
    public ResponseEntity<?> updateCustomMenuLink(
            @PathVariable("custom-menu-id") String customMenuId,
            @RequestBody CustomMenuLinkUpdateRequest request
    ) {
        return ResponseEntity.ok(customMenuLinkService.updateCustomMenuLink(customMenuId, request));
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> getCustomMenuLinks(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "location-id", required = false) String locationId,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "show-on-company", required = false) Boolean showOnCompany,
            @RequestParam(value = "skip", required = false) Integer skip
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "query", query);
        MapUtil.putIfNotNull(queryParams, "showOnCompany", showOnCompany);
        MapUtil.putIfNotNull(queryParams, "skip", skip);

        return ResponseEntity.ok(customMenuLinkService.getCustomMenuLinks(queryParams));
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> createCustomMenuLink(@RequestBody CustomMenuLinkCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customMenuLinkService.createCustomMenuLink(request));
    }

}
