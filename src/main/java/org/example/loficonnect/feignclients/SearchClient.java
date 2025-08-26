package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.search.GoHighLevelContactSearchRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "searchClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface SearchClient {
    @PostMapping(
            value = "/contacts/search",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode searchContacts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelContactSearchRequest request
    );

    @GetMapping(
            value = "/contacts/search/duplicate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getDuplicateContacts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam Map<String, Object> queryParams
    );


}
