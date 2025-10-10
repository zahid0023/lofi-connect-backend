package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.prices.PriceCreateRequest;
import org.example.loficonnect.dto.request.prices.PriceInventoryUpdateRequest;
import org.example.loficonnect.dto.request.prices.PriceUpdateRequest;
import org.example.loficonnect.service.PriceService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @AppKey
    @PostMapping("/products/{product-id}/price")
    public ResponseEntity<?> createPrice(
        @PathVariable("product-id") String productId,
        @RequestBody PriceCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(priceService.createPrice(productId, request));
    }

    @AppKey
    @GetMapping("/products/{product-id}/price")
    public ResponseEntity<?> getPrices(
            @PathVariable("product-id") String productId,
            @RequestParam(value = "ids", required = false) String ids,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "ids", ids);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(priceService.getPrices(productId, queryParams));
    }

    @AppKey
    @GetMapping("/products/inventory")
    public ResponseEntity<?> listInventory(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "search", search);
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);

        return ResponseEntity.ok(priceService.listInventory(queryParams));
    }

    @AppKey
    @PostMapping("/products/inventory")
    public ResponseEntity<?> updateInventory(@RequestBody PriceInventoryUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(priceService.updateInventory(request));
    }

    @AppKey
    @GetMapping("/products/{product-id}/price/{price-id}")
    public ResponseEntity<?> getPriceById(
            @PathVariable("product-id") String productId,
            @PathVariable("price-id") String priceId,
            @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(priceService.getPriceById(productId, priceId, queryParams));
    }

    @AppKey
    @PutMapping("/products/{product-id}/price/{price-id}")
    public ResponseEntity<?> updatePrice(
            @PathVariable("product-id") String productId,
            @PathVariable("price-id") String priceId,
            @RequestBody PriceUpdateRequest request
    ) {
        return ResponseEntity.ok(priceService.updatePrice(productId, priceId, request));
    }

    @AppKey
    @DeleteMapping("/products/{product-id}/price/{price-id}")
    public ResponseEntity<?> deletePrice(
            @PathVariable("product-id") String productId,
            @PathVariable("price-id") String priceId,
            @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(priceService.deletePrice(productId, priceId, queryParams));
    }

}
