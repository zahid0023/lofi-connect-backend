package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.orderfulfillment.OrderFulfillmentCreateRequest;
import org.example.loficonnect.service.OrderFulfillmentService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class OrderFulfillmentController {

    private final OrderFulfillmentService orderFulfillmentService;

    public OrderFulfillmentController(OrderFulfillmentService orderFulfillmentService) {
        this.orderFulfillmentService = orderFulfillmentService;
    }

    @AppKey
    @PostMapping("/payments/orders/{order-id}/fulfillments")
    public ResponseEntity<?> createFulfillment(
            @PathVariable("order-id") String orderId,
            @RequestBody OrderFulfillmentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderFulfillmentService.createFulfillment(orderId, request));
    }

    @AppKey
    @GetMapping("/payments/orders/{order-id}/fulfillments")
    public ResponseEntity<?> getFulfillments(
            @PathVariable("order-id") String orderId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);

        return ResponseEntity.ok(orderFulfillmentService.getFulfillments(orderId, queryParams));
    }

}
