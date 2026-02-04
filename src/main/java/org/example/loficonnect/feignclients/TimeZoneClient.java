package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "timeZoneClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface TimeZoneClient {
    @GetMapping(
            value = "/locations/{locationId}/timezones",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getTimezones(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable String locationId
    );

}
