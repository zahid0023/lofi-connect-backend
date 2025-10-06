package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "accountClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface AccountClient {

    @GetMapping(
        value = "/social-media-posting/{locationId}/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAccounts(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("locationId") String locationId
    );

    @DeleteMapping(
            value = "/social-media-posting/{locationId}/accounts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteAccount(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String id,
            @RequestParam Map<String, Object> queryParams
    );

}
