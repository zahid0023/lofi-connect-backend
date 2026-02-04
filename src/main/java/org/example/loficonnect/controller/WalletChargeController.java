package org.example.loficonnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.walletcharges.WalletChargeCreateRequest;
import org.example.loficonnect.service.WalletChargesService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class WalletChargeController {

    private final WalletChargesService walletChargesService;

    public WalletChargeController(WalletChargesService walletChargesService) {
        this.walletChargesService = walletChargesService;
    }

    @AppKey
    @PostMapping("/marketplace/billing/charges")
    public ResponseEntity<?> createWalletCharge(@RequestBody WalletChargeCreateRequest request) {
        JsonNode response = walletChargesService.createWalletCharge(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @AppKey
    @GetMapping("/marketplace/billing/charges")
    public ResponseEntity<?> getWalletCharges(
            @RequestParam(value = "end-date", required = false) LocalDate endDate,
            @RequestParam(value = "event-id", required = false) String eventId,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "meter-id", required = false) String meterId,
            @RequestParam(value = "skip", required = false) Integer skip,
            @RequestParam(value = "start-date", required = false) LocalDate startDate,
            @RequestParam(value = "user-id", required = false) String userId
    ) {
        Map<String, Object> queryParams = new HashMap<>();

        MapUtil.putIfNotNull(queryParams, "endDate", endDate != null ? endDate.toString() : null);
        MapUtil.putIfNotNull(queryParams, "eventId", eventId);
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "meterId", meterId);
        MapUtil.putIfNotNull(queryParams, "skip", skip);
        MapUtil.putIfNotNull(queryParams, "startDate", startDate != null ? startDate.toString() : null);
        MapUtil.putIfNotNull(queryParams, "userId", userId);

        JsonNode response = walletChargesService.getWalletCharges(queryParams);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @DeleteMapping("/marketplace/billing/charges/{charge-id}")
    public ResponseEntity<?> deleteWalletCharge(@PathVariable("charge-id") String chargeId) {
        JsonNode response = walletChargesService.deleteWalletCharge(chargeId);
        return ResponseEntity.ok(response);
    }

    @AppKey
    @GetMapping("/marketplace/billing/charges/{charge-id}")
    public ResponseEntity<?> getWalletCharge(@PathVariable("charge-id") String chargeId) {
        return ResponseEntity.ok(walletChargesService.getWalletCharge(chargeId));
    }

    @AppKey
    @GetMapping("/marketplace/billing/charges/has-funds")
    public ResponseEntity<?> hasSufficientFunds() {
        return ResponseEntity.ok(walletChargesService.hasSufficientFunds());
    }

}
