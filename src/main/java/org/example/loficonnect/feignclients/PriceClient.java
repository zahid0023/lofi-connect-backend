package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.prices.GoHighLevelPriceCreateRequest;
import org.example.loficonnect.dto.mapper.prices.GoHighLevelPriceInventoryUpdateRequest;
import org.example.loficonnect.dto.mapper.prices.GoHighLevelPriceUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "priceClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface PriceClient {

    @PostMapping(
        value = "/products/{productId}/price",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createPrice(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("productId") String productId,
        @RequestBody GoHighLevelPriceCreateRequest request
    );

    @GetMapping(
            value = "/products/{productId}/price",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getPrices(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("productId") String productId,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/products/inventory",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode listInventory(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/products/inventory",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateInventory(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelPriceInventoryUpdateRequest request
    );

    @GetMapping(
            value = "/products/{productId}/price/{priceId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getPriceById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("productId") String productId,
            @PathVariable("priceId") String priceId,
            @RequestParam Map<String, Object> queryParams
    );

    @PutMapping(
            value = "/products/{productId}/price/{priceId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updatePrice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("productId") String productId,
            @PathVariable("priceId") String priceId,
            @RequestBody GoHighLevelPriceUpdateRequest request
    );

    @DeleteMapping(
            value = "/products/{productId}/price/{priceId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deletePrice(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("productId") String productId,
            @PathVariable("priceId") String priceId,
            @RequestParam Map<String, Object> queryParams
    );

}
