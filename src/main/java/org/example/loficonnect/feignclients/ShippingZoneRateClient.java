package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.shippingzonerate.GoHighLevelShippingZoneRateCreateRequest;
import org.example.loficonnect.dto.mapper.shippingzonerate.GoHighLevelShippingZoneRateUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "shippingZoneRateClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface ShippingZoneRateClient {

    @PostMapping(
            value = "/store/shipping-zone/{shippingZoneId}/shipping-rate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createShippingZoneRate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingZoneId") String shippingZoneId,
            @RequestBody GoHighLevelShippingZoneRateCreateRequest request
    );

    @GetMapping(
            value = "/store/shipping-zone/{shippingZoneId}/shipping-rate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode listShippingZoneRates(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingZoneId") String shippingZoneId,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/store/shipping-zone/{shippingZoneId}/shipping-rate/{shippingRateId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getShippingZoneRateById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingZoneId") String shippingZoneId,
            @PathVariable("shippingRateId") String shippingRateId,
            @RequestParam Map<String, Object> queryParams
    );

    @PutMapping(
            value = "/store/shipping-zone/{shippingZoneId}/shipping-rate/{shippingRateId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateShippingZoneRate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingZoneId") String shippingZoneId,
            @PathVariable("shippingRateId") String shippingRateId,
            @RequestBody GoHighLevelShippingZoneRateUpdateRequest request
    );

    @DeleteMapping(
            value = "/store/shipping-zone/{shippingZoneId}/shipping-rate/{shippingRateId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteShippingZoneRate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingZoneId") String shippingZoneId,
            @PathVariable("shippingRateId") String shippingRateId,
            @RequestParam Map<String, Object> queryParams
    );

}
