package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.statictics.GoHighLevelStaticticsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "staticticsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface StaticticsClient {

    @PostMapping(
        value = "/social-media-posting/statistics",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getStatistics(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam("locationId") String locationId,
        @RequestBody GoHighLevelStaticticsRequest request
    );
}
