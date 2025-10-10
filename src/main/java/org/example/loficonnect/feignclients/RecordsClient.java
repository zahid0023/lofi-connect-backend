package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.records.GoHighLevelCreateRecordRequest;
import org.example.loficonnect.dto.mapper.records.GoHighLevelUpdateRecordRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ObjectsRecordsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface RecordsClient {

    @GetMapping(
        value = "/objects/{schema-key}/records/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getRecordById(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("schema-key") String schemaKey,
        @PathVariable("id") String id
    );

    @PutMapping(
            value = "/objects/{schema-key}/records/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateRecord(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("schema-key") String schemaKey,
            @PathVariable("id") String id,
            @RequestBody GoHighLevelUpdateRecordRequest request
    );

    @DeleteMapping(
            value = "/objects/{schema-key}/records/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteRecord(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("schema-key") String schemaKey,
            @PathVariable("id") String id
    );

    @PostMapping(
            value = "/objects/{schema-key}/records",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createRecord(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("schema-key") String schemaKey,
            @RequestBody GoHighLevelCreateRecordRequest request
    );
}
