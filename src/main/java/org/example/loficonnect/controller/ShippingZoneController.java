package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.shippingzone.ShippingRateRequest;
import org.example.loficonnect.dto.request.shippingzone.ShippingZoneCreateRequest;
import org.example.loficonnect.dto.request.shippingzone.ShippingZoneUpdateRequest;
import org.example.loficonnect.service.ShippingZoneService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shipping-zone")
public class ShippingZoneController {

    private final ShippingZoneService shippingZoneService;

    public ShippingZoneController(ShippingZoneService shippingZoneService) {
        this.shippingZoneService = shippingZoneService;
    }

    @AppKey
    @PostMapping
    public ResponseEntity<?> createShippingZone(@RequestBody ShippingZoneCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shippingZoneService.createShippingZone(request));
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> listShippingZones(
            @RequestParam(value = "limit", required = false, defaultValue = "0") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "with-shipping-rate", required = false) Boolean withShippingRate,
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "withShippingRate", withShippingRate);
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        return ResponseEntity.ok(shippingZoneService.listShippingZones(queryParams));
    }

    @AppKey
    @GetMapping("/{shipping-zone-id}")
    public ResponseEntity<?> getShippingZoneById(
            @PathVariable("shipping-zone-id") String shippingZoneId,
            @RequestParam(value = "with-shipping-rate", required = false) Boolean withShippingRate,
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "withShippingRate", withShippingRate);
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        return ResponseEntity.ok(shippingZoneService.getShippingZoneById(shippingZoneId, queryParams));
    }

    @AppKey
    @PutMapping("/{shipping-zone-id}")
    public ResponseEntity<?> updateShippingZone(
            @PathVariable("shipping-zone-id") String shippingZoneId,
            @RequestBody ShippingZoneUpdateRequest request
    ) {
        return ResponseEntity.ok(shippingZoneService.updateShippingZone(shippingZoneId, request));
    }

    @AppKey
    @DeleteMapping("/{shipping-zone-id}")
    public ResponseEntity<?> deleteShippingZone(
            @PathVariable("shipping-zone-id") String shippingZoneId,
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        return ResponseEntity.ok(shippingZoneService.deleteShippingZone(shippingZoneId, queryParams));
    }

    @AppKey
    @PostMapping("/shipping-rates")
    public ResponseEntity<?> getShippingRates(@RequestBody ShippingRateRequest request) {
        return ResponseEntity.ok(shippingZoneService.getShippingRates(request));
    }

}
