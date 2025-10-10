package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.oauthgoogle.GoHighLevelOauthGoogleBusinessLocationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "oauthGoogleClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OauthGoogleClient {

    @GetMapping(
            value = "/social-media-posting/oauth/google/start",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode startOauth(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/social-media-posting/oauth/{locationId}/google/locations/{accountId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getGoogleBusinessLocations(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId
    );

    @PostMapping(
            value = "/social-media-posting/oauth/{locationId}/google/locations/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode setGoogleBusinessLocation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId,
            @RequestBody GoHighLevelOauthGoogleBusinessLocationRequest request
    );

}
