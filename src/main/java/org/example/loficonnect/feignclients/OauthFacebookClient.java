package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.oauthfacebook.GoHighLevelAttachFacebookPageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "oauthFacebookClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface OauthFacebookClient {

    @GetMapping(
        value = "/social-media-posting/oauth/facebook/start",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode startOauthFacebook(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/social-media-posting/oauth/{locationId}/facebook/accounts/{accountId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getFacebookPages(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId
    );

    @PostMapping(
            value = "/social-media-posting/oauth/{locationId}/facebook/accounts/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode attachFacebookPage(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("accountId") String accountId,
            @RequestBody GoHighLevelAttachFacebookPageRequest request
    );

}
