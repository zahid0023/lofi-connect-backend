package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "transactionClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface TransactionClient {

    @GetMapping(
            value = "/payments/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTransactions(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/payments/transactions/{transactionId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTransactionById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("transactionId") String transactionId,
            @RequestParam Map<String, Object> queryParams
    );

}
