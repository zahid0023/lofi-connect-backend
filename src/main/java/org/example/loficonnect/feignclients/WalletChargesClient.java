package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.walletcharges.GoHighLevelWalletChargeCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "billingChargesClient",url = "https://services.leadconnectorhq.com",configuration = FeignLoggingConfig.class)
public interface WalletChargesClient {

    @PostMapping(
            value = "/marketplace/billing/charges",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createWalletCharge(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelWalletChargeCreateRequest request
    );

    @GetMapping(
            value = "/marketplace/billing/charges",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getWalletCharges(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @DeleteMapping(
            value = "/marketplace/billing/charges/{chargeId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteWalletCharge(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("chargeId") String chargeId
    );

    @GetMapping(
            value = "/marketplace/billing/charges/{chargeId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getWalletCharge(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("chargeId") String chargeId
    );

    @GetMapping(
            value = "/marketplace/billing/charges/has-funds",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode hasSufficientFunds(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version
    );
}
