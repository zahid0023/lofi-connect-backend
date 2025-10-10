package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.tag.TagByIdsRequest;
import org.example.loficonnect.service.TagService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @AppKey
    @GetMapping("/{location-id}")
    public ResponseEntity<?> getTags(
        @PathVariable("location-id") String locationId,
        @RequestParam(value = "limit", required = false) String limit,
        @RequestParam(value = "skip", required = false) String skip,
        @RequestParam(value = "search-text", required = false) String searchText
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "searchText", searchText);

        return ResponseEntity.ok(tagService.getTags(locationId, queryParams));
    }

    @AppKey
    @PostMapping("/{location-id}/details")
    public ResponseEntity<?> getTagsByIds(
            @PathVariable("location-id") String locationId,
            @RequestBody TagByIdsRequest request
    ) {
        return ResponseEntity.ok(tagService.getTagsByIds(locationId, request));
    }

}
