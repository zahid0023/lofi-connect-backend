package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.store.StoreUpdateRequest;
import org.example.loficonnect.service.StoreService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @AppKey
    @GetMapping("/products/store/{store-id}/stats")
    public ResponseEntity<?> getStoreStats(
        @PathVariable("store-id") String storeId,
        @RequestParam(value = "collection-ids", required = false) String collectionIds,
        @RequestParam(value = "search", required = false) String search,
        @RequestParam("alt-id") String altId,
        @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "collectionIds", collectionIds);
        MapUtil.putIfNotNull(queryParams, "search", search);
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);

        return ResponseEntity.ok(storeService.getStoreStats(storeId, queryParams));
    }

    @AppKey
    @PostMapping("/products/store/{store-id}")
    public ResponseEntity<?> updateStoreProducts(
            @PathVariable("store-id") String storeId,
            @RequestBody StoreUpdateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(storeService.updateStoreProducts(storeId, request));
    }

}
