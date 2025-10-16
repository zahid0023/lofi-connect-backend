package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.collections.CollectionCreateRequest;
import org.example.loficonnect.dto.request.collections.CollectionUpdateRequest;
import org.example.loficonnect.service.CollectionService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @AppKey
    @GetMapping("/collections")
    public ResponseEntity<?> getProductCollections(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "collection-ids", required = false) String collectionIds,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "collectionIds", collectionIds);
        MapUtil.putIfNotNull(queryParams, "name", name);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);

        return ResponseEntity.ok(collectionService.getProductCollections(queryParams));
    }

    @AppKey
    @PostMapping("/collections")
    public ResponseEntity<?> createCollection(@RequestBody CollectionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(collectionService.createCollection(request));
    }

    @AppKey
    @GetMapping("/collections/{collection-id}")
    public ResponseEntity<?> getCollectionById(@PathVariable("collection-id") String collectionId) {
        return ResponseEntity.ok(collectionService.getCollectionById(collectionId));
    }

    @AppKey
    @PutMapping("/collections/{collection-id}")
    public ResponseEntity<?> updateCollection(
            @PathVariable("collection-id") String collectionId,
            @RequestBody CollectionUpdateRequest request
    ) {
        return ResponseEntity.ok(collectionService.updateCollection(collectionId, request));
    }

    @AppKey
    @DeleteMapping("/collections/{collection-id}")
    public ResponseEntity<?> deleteCollection(
            @PathVariable("collection-id") String collectionId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        return ResponseEntity.ok(collectionService.deleteCollection(collectionId, queryParams));
    }

}
