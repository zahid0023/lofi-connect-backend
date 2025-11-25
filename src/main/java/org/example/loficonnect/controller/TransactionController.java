package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.TransactionService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @AppKey
    @GetMapping("/payments/transactions")
    public ResponseEntity<?> getTransactions(
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "contact-id", required = false) String contactId,
            @RequestParam(value = "end-at", required = false) String endAt,
            @RequestParam(value = "entity-id", required = false) String entityId,
            @RequestParam(value = "entity-source-sub-type", required = false) String entitySourceSubType,
            @RequestParam(value = "entity-source-type", required = false) String entitySourceType,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "location-id", required = false) String locationId,
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "payment-mode", required = false) String paymentMode,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "start-at", required = false) String startAt,
            @RequestParam(value = "subscription-id", required = false) String subscriptionId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "contactId", contactId);
        MapUtil.putIfNotNull(queryParams, "endAt", endAt);
        MapUtil.putIfNotNull(queryParams, "entityId", entityId);
        MapUtil.putIfNotNull(queryParams, "entitySourceSubType", entitySourceSubType);
        MapUtil.putIfNotNull(queryParams, "entitySourceType", entitySourceType);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        MapUtil.putIfNotNull(queryParams, "offset", offset);
        MapUtil.putIfNotNull(queryParams, "paymentMode", paymentMode);
        MapUtil.putIfNotNull(queryParams, "search", search);
        MapUtil.putIfNotNull(queryParams, "startAt", startAt);
        MapUtil.putIfNotNull(queryParams, "subscriptionId", subscriptionId);

        return ResponseEntity.ok(transactionService.getTransactions(queryParams));
    }

    @AppKey
    @GetMapping("/payments/transactions/{transaction-id}")
    public ResponseEntity<?> getTransactionById(
            @PathVariable("transaction-id") String transactionId,
            @RequestParam("alt-id") String altId,
            @RequestParam("alt-type") String altType,
            @RequestParam(value = "location-id", required = false) String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "altId", altId);
        MapUtil.putIfNotNull(queryParams, "altType", altType);
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);

        return ResponseEntity.ok(transactionService.getTransactionById(transactionId, queryParams));
    }

}
