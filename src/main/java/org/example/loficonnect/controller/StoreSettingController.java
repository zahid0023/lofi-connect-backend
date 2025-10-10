package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.storesetting.StoreSettingCreateRequest;
import org.example.loficonnect.service.StoreSettingService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/store-settings")
public class StoreSettingController {

    private final StoreSettingService storeSettingService;

    public StoreSettingController(StoreSettingService storeSettingService) {
        this.storeSettingService = storeSettingService;
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> createOrUpdateStoreSetting(@RequestBody StoreSettingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeSettingService.createOrUpdateStoreSetting(request));
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> getStoreSetting(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);

        return ResponseEntity.ok(storeSettingService.getStoreSetting(queryParams));
    }

}
