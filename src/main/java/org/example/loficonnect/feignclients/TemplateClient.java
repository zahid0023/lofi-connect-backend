package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "templateClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface TemplateClient {
    @GetMapping(
            value = "/locations/{locationId}/templates",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTemplates(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @RequestParam Map<String, Object> queryParams
    );

    @DeleteMapping(
            value = "/locations/{locationId}/templates/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteTemplate(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("locationId") String locationId,
            @PathVariable("id") String templateId
    );


}
