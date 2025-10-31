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
@RequestMapping("/api/v1/ghl")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @AppKey
    @GetMapping("/tags")
    public ResponseEntity<?> getTags(
            @RequestParam(value = "limit", required = false) String limit,
            @RequestParam(value = "skip", required = false) String skip,
            @RequestParam(value = "search-text", required = false) String searchText
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "searchText", searchText);

        return ResponseEntity.ok(tagService.getTags(queryParams));
    }

    @AppKey
    @PostMapping("/tags/details")
    public ResponseEntity<?> getTagsByIds(
            @RequestBody TagByIdsRequest request
    ) {
        return ResponseEntity.ok(tagService.getTagsByIds(request));
    }
}
