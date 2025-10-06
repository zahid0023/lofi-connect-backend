package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.oauthtiktok.GoHighLevelOauthtiktokAttachRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "oauthtiktokClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OauthtiktokClient {

    @GetMapping(
        value = "/social-media-posting/oauth/tiktok/start",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode startTiktokOAuth(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/social-media-posting/oauth/{locationId}/tiktok/accounts/{accountId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTiktokProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId
    );

    @PostMapping(
            value = "/social-media-posting/oauth/{locationId}/tiktok/accounts/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode attachTiktokProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId,
            @RequestBody GoHighLevelOauthtiktokAttachRequest request
    );

    @GetMapping(
            value = "/social-media-posting/oauth/tiktok-business/start",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode startTiktokBusinessOAuth(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/social-media-posting/oauth/{locationId}/tiktok-business/accounts/{accountId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTiktokBusinessProfile(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId
    );

}
