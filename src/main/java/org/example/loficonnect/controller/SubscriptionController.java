package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.SubscriptionService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @AppKey
    @GetMapping("/subscriptions")
    public ResponseEntity<?> getSubscriptions(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "contact-id", required = false) String contactId,
            @RequestParam(value = "end-at", required = false) String endAt,
            @RequestParam(value = "entity-id", required = false) String entityId,
            @RequestParam(value = "entity-source-type", required = false) String entitySourceType,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "payment-mode", required = false) String paymentMode,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "start-at", required = false) String startAt
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "contactId", contactId);
        MapUtil.putIfNotNull(queryParams, "endAt", endAt);
        MapUtil.putIfNotNull(queryParams, "entityId", entityId);
        MapUtil.putIfNotNull(queryParams, "entitySourceType", entitySourceType);
        MapUtil.putIfNotNull(queryParams, "id", id);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "paymentMode", paymentMode);
        MapUtil.putIfNotNull(queryParams, "search", search);
        MapUtil.putIfNotNull(queryParams, "startAt", startAt);

        return ResponseEntity.ok(subscriptionService.getSubscriptions(queryParams));
    }

    @AppKey
    @GetMapping("/subscriptions/{subscription-id}")
    public ResponseEntity<?> getSubscriptionById(
            @PathVariable("subscription-id") String subscriptionId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);

        return ResponseEntity.ok(subscriptionService.getSubscriptionById(subscriptionId, queryParams));
    }

}
