package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.shippingcarrier.ShippingCarrierCreateRequest;
import org.example.loficonnect.dto.request.shippingcarrier.ShippingCarrierUpdateRequest;
import org.example.loficonnect.service.ShippingCarrierService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class ShippingCarrierController {

    private final ShippingCarrierService shippingCarrierService;

    public ShippingCarrierController(ShippingCarrierService shippingCarrierService) {
        this.shippingCarrierService = shippingCarrierService;
    }

    @AppKey
    @PostMapping("/shipping-carriers")
    public ResponseEntity<?> createShippingCarrier(@RequestBody ShippingCarrierCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shippingCarrierService.createShippingCarrier(request));
    }

    @AppKey
    @GetMapping("/shipping-carriers")
    public ResponseEntity<?> listShippingCarriers(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);

        return ResponseEntity.ok(shippingCarrierService.listShippingCarriers(queryParams));
    }

    @AppKey
    @GetMapping("/shipping-carriers/{shipping-carrier-id}")
    public ResponseEntity<?> getShippingCarrier(
            @PathVariable("shipping-carrier-id") String shippingCarrierId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);

        return ResponseEntity.ok(shippingCarrierService.getShippingCarrier(shippingCarrierId, queryParams));
    }

    @AppKey
    @PutMapping("/shipping-carriers/{shipping-carrier-id}")
    public ResponseEntity<?> updateShippingCarrier(
            @PathVariable("shipping-carrier-id") String shippingCarrierId,
            @RequestBody ShippingCarrierUpdateRequest request
    ) {
        return ResponseEntity.ok(shippingCarrierService.updateShippingCarrier(shippingCarrierId, request));
    }

    @AppKey
    @DeleteMapping("/shipping-carriers/{shipping-carrier-id}")
    public ResponseEntity<?> deleteShippingCarrier(
            @PathVariable("shipping-carrier-id") String shippingCarrierId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);

        return ResponseEntity.ok(shippingCarrierService.deleteShippingCarrier(shippingCarrierId, queryParams));
    }

}
