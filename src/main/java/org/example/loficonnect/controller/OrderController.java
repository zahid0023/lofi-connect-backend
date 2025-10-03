package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.OrderService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @AppKey
    @GetMapping
    public ResponseEntity<?> getOrders(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "contact-id", required = false) String contactId,
            @RequestParam(value = "end-at", required = false) String endAt,
            @RequestParam(value = "funnel-product-ids", required = false) String funnelProductIds,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "location-id", required = false) String locationId,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "payment-mode", required = false) String paymentMode,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "start-at", required = false) String startAt,
            @RequestParam(value = "status", required = false) String status
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "contactId", contactId);
        MapUtil.putIfNotNull(queryParams, "endAt", endAt);
        MapUtil.putIfNotNull(queryParams, "funnelProductIds", funnelProductIds);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "paymentMode", paymentMode);
        MapUtil.putIfNotNull(queryParams, "search", search);
        MapUtil.putIfNotNull(queryParams, "startAt", startAt);
        MapUtil.putIfNotNull(queryParams, "status", status);

        return ResponseEntity.ok(orderService.getOrders(queryParams));
    }

    @AppKey
    @GetMapping("/{order-id}")
    public ResponseEntity<?> getOrderById(
            @PathVariable("order-id") String orderId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "location-id", required = false) String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(orderService.getOrderById(orderId, queryParams));
    }

}
