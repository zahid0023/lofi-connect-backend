package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.oauthlinkedin.GhlAttachLinkedinAccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "oauthLinkedinClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OauthLinkedinClient {

    @GetMapping(
        value = "/social-media-posting/oauth/linkedin/start",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode startOauthLinkedin(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/social-media-posting/oauth/{locationId}/linkedin/accounts/{accountId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getLinkedinAccounts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId
    );

    @PostMapping(
            value = "/social-media-posting/oauth/{locationId}/linkedin/accounts/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode attachLinkedinAccount(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId,
            @RequestBody GhlAttachLinkedinAccountRequest request
    );
}
