package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.follower.GoHighLevelAddFollowersRequest;
import org.example.loficonnect.dto.mapper.follower.GoHighLevelRemoveFollowersRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "followerClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface FollowerClient {
    @PostMapping(
            value = "/contacts/{contactId}/followers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode addFollowers(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelAddFollowersRequest request,
            @PathVariable String contactId);

    @DeleteMapping(
            value = "/contacts/{contactId}/followers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode removeFollowers(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable String contactId,
            @RequestBody GoHighLevelRemoveFollowersRequest request
    );


}
