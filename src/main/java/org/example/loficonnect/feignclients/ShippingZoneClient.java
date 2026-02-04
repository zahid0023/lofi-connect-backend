package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.shippingzone.GoHighLevelShippingRateRequest;
import org.example.loficonnect.dto.mapper.shippingzone.GoHighLevelShippingZoneCreateRequest;
import org.example.loficonnect.dto.mapper.shippingzone.GoHighLevelShippingZoneUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "shippingZoneClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface ShippingZoneClient {

    @PostMapping(
            value = "/store/shipping-zone",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createShippingZone(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelShippingZoneCreateRequest request
    );

    @GetMapping(
            value = "/store/shipping-zone",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode listShippingZones(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/store/shipping-zone/{shippingZoneId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getShippingZoneById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingZoneId") String shippingZoneId,
            @RequestParam Map<String, Object> queryParams
    );

    @PutMapping(
            value = "/store/shipping-zone/{shippingZoneId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateShippingZone(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingZoneId") String shippingZoneId,
            @RequestBody GoHighLevelShippingZoneUpdateRequest request
    );

    @DeleteMapping(
            value = "/store/shipping-zone/{shippingZoneId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteShippingZone(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingZoneId") String shippingZoneId,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/store/shipping-zone/shipping-rates",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getShippingRates(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelShippingRateRequest request
    );

}
