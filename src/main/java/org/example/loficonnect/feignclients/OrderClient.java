package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "orderClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OrderClient {

    @GetMapping(
            value = "/payments/orders",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getOrders(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/payments/orders/{orderId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getOrderById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("orderId") String orderId,
            @RequestParam Map<String, Object> queryParams
    );

}
