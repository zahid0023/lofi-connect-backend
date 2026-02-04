package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.businesses.GoHighLevelBusinessCreateRequest;
import org.example.loficonnect.dto.mapper.businesses.GoHighLevelBusinessUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "businessClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)

public interface BusinessClient {

    @GetMapping("/businesses/{businessId}")
    JsonNode getBusiness(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("businessId") String businessId
    );

    @GetMapping(
            value = "/businesses/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getBusinessesByLocation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/businesses/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createBusiness(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelBusinessCreateRequest request
    );

    @PutMapping(
            value = "/businesses/{businessId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateBusiness(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("businessId") String businessId,
            @RequestBody GoHighLevelBusinessUpdateRequest request
    );

    @DeleteMapping(
            value = "/businesses/{businessId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteBusiness(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("businessId") String businessId
    );

}
