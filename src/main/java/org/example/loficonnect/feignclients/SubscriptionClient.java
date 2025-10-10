package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "subscriptionClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface SubscriptionClient {

    @GetMapping(
            value = "/payments/subscriptions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getSubscriptions(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/payments/subscriptions/{subscriptionId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getSubscriptionById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("subscriptionId") String subscriptionId,
            @RequestParam Map<String, Object> queryParams
    );

}
