package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.bulk.GoHighLevelBulkBusinessUpdateRequest;
import org.example.loficonnect.dto.mapper.bulk.GoHighLevelBulkTagUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "bulkClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface BulkClient {
    @PostMapping(
            value = "/contacts/bulk/tags/update/{type}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateBulkTags(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("type") String type,
            @RequestBody GoHighLevelBulkTagUpdateRequest request
    );

    @PostMapping(
            value = "/contacts/bulk/business",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateBulkBusiness(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelBulkBusinessUpdateRequest request
    );


}
