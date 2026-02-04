package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.orderfulfillment.GoHighLevelOrderFulfillmentCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "orderFulfillmentClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OrderFulfillmentClient {

    @PostMapping(
            value = "/payments/orders/{orderId}/fulfillments",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createFulfillment(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("orderId") String orderId,
            @RequestBody GoHighLevelOrderFulfillmentCreateRequest request
    );

    @GetMapping(
            value = "/payments/orders/{orderId}/fulfillments",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getFulfillments(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("orderId") String orderId,
            @RequestParam Map<String, Object> queryParams
    );

}
