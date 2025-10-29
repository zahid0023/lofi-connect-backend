package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.usersearch.GoHighLevelUserSearchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "userSearchClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface UserSearchClient {

    @GetMapping(
        value = "/users/search",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode searchUsers(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/users/search/filter-by-email",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode filterUsersByEmail(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelUserSearchRequest request
    );
}
