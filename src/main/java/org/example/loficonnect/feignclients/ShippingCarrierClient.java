package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.shippingcarrier.GoHighLevelShippingCarrierCreateRequest;
import org.example.loficonnect.dto.mapper.shippingcarrier.GoHighLevelShippingCarrierUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "shippingCarrierClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface ShippingCarrierClient {

    @PostMapping(
        value = "/store/shipping-carrier",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createShippingCarrier(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestBody GoHighLevelShippingCarrierCreateRequest request
    );

    @GetMapping(
            value = "/store/shipping-carrier",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode listShippingCarriers(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/store/shipping-carrier/{shippingCarrierId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getShippingCarrier(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingCarrierId") String shippingCarrierId,
            @RequestParam Map<String, Object> queryParams
    );

    @PutMapping(
            value = "/store/shipping-carrier/{shippingCarrierId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateShippingCarrier(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingCarrierId") String shippingCarrierId,
            @RequestBody GoHighLevelShippingCarrierUpdateRequest request
    );

    @DeleteMapping(
            value = "/store/shipping-carrier/{shippingCarrierId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteShippingCarrier(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("shippingCarrierId") String shippingCarrierId,
            @RequestParam Map<String, Object> queryParams
    );

}
