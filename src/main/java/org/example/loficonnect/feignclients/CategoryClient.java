package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "categoryClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface CategoryClient {

    @GetMapping(
        value = "/social-media-posting/{locationId}/categories",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCategories(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("locationId") String locationId,
        @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/social-media-posting/{locationId}/categories/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getCategoryById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String categoryId
    );

}
