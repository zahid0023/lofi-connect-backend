package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.shippingzonerate.ShippingZoneRateCreateRequest;
import org.example.loficonnect.dto.request.shippingzonerate.ShippingZoneRateUpdateRequest;
import org.example.loficonnect.service.ShippingZoneRateService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class ShippingZoneRateController {

    private final ShippingZoneRateService shippingZoneRateService;

    public ShippingZoneRateController(ShippingZoneRateService shippingZoneRateService) {
        this.shippingZoneRateService = shippingZoneRateService;
    }

    @AppKey
    @PostMapping("/shipping-zone/{shipping-zone-id}/shipping-rate")
    public ResponseEntity<?> createShippingZoneRate(
            @PathVariable("shipping-zone-id") String shippingZoneId,
            @RequestBody ShippingZoneRateCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shippingZoneRateService.createShippingZoneRate(shippingZoneId, request));
    }

    @AppKey
    @GetMapping("/shipping-zone/{shipping-zone-id}/shipping-rate")
    public ResponseEntity<?> listShippingZoneRates(
            @PathVariable("shipping-zone-id") String shippingZoneId,
            @RequestParam(value = "limit", required = false, defaultValue = "0") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        return ResponseEntity.ok(shippingZoneRateService.listShippingZoneRates(shippingZoneId, queryParams));
    }

    @AppKey
    @GetMapping("/shipping-zone/{shipping-zone-id}/shipping-rate/{shipping-rate-id}")
    public ResponseEntity<?> getShippingZoneRateById(
            @PathVariable("shipping-zone-id") String shippingZoneId,
            @PathVariable("shipping-rate-id") String shippingRateId,
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        return ResponseEntity.ok(shippingZoneRateService.getShippingZoneRateById(shippingZoneId, shippingRateId, queryParams));
    }

    @AppKey
    @PutMapping("/shipping-zone/{shipping-zone-id}/shipping-rate/{shipping-rate-id}")
    public ResponseEntity<?> updateShippingZoneRate(
            @PathVariable("shipping-zone-id") String shippingZoneId,
            @PathVariable("shipping-rate-id") String shippingRateId,
            @RequestBody ShippingZoneRateUpdateRequest request
    ) {
        return ResponseEntity.ok(shippingZoneRateService.updateShippingZoneRate(shippingZoneId, shippingRateId, request));
    }

    @AppKey
    @DeleteMapping("/shipping-zone/{shipping-zone-id}/shipping-rate/{shipping-rate-id}")
    public ResponseEntity<?> deleteShippingZoneRate(
            @PathVariable("shipping-zone-id") String shippingZoneId,
            @PathVariable("shipping-rate-id") String shippingRateId,
            @RequestParam(value = "alt-id") String altId,
            @RequestParam(value = "alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        return ResponseEntity.ok(shippingZoneRateService.deleteShippingZoneRate(shippingZoneId, shippingRateId, queryParams));
    }

}
