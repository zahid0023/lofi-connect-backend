package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.oauthinstagram.GoHighLevelAttachInstagramAccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "oauthInstagramClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OauthInstagramClient {

    @GetMapping(
        value = "/social-media-posting/oauth/instagram/start",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode startOauthInstagram(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/social-media-posting/oauth/{locationId}/instagram/accounts/{accountId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getInstagramProfessionalAccounts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId
    );

    @PostMapping(
            value = "/social-media-posting/oauth/{locationId}/instagram/accounts/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode attachInstagramAccount(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId,
            @RequestBody GoHighLevelAttachInstagramAccountRequest request
    );

}
