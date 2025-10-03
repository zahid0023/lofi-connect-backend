package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "funnelClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface FunnelClient {

    @GetMapping(
        value = "/funnels/funnel/list",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getFunnelList(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/funnels/page",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getFunnelPages(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

    @GetMapping(
            value = "/funnels/page/count",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getFunnelPageCount(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );

}
