package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.user.GoHighLevelUserCreateRequest;
import org.example.loficonnect.dto.mapper.user.GoHighLevelUserUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "userClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface UserClient {

    @GetMapping(
        value = "/users/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getUserById(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("userId") String userId
    );

    @PutMapping(
            value = "/users/{userId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateUserById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("userId") String userId,
            @RequestBody GoHighLevelUserUpdateRequest request
    );

    @DeleteMapping(
            value = "/users/{userId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteUserById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("userId") String userId
    );

    @GetMapping(
            value = "/users/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getUsersByLocation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/users/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createUser(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelUserCreateRequest request
    );

}
