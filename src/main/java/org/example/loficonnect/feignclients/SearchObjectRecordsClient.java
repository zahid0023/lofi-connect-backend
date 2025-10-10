package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.searchobjectrecords.GoHighLevelSearchObjectRecordsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ObjectsRecordsSearchClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface SearchObjectRecordsClient {

    @PostMapping(
        value = "/objects/{schema-key}/records/search",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode searchObjectRecords(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("schema-key") String schemaKey,
        @RequestBody GoHighLevelSearchObjectRecordsRequest request
    );
}
