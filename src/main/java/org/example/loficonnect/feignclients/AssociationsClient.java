package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.commons.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.associations.GoHighLevelCreateAssociationRequest;
import org.example.loficonnect.dto.mapper.associations.GoHighLevelUpdateAssociationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "AssociationsClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface AssociationsClient {

    @GetMapping(
        value = "/associations/key/{key_name}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAssociationKeyByName(
        @RequestHeader("Authorization") String authorization,
        @RequestHeader("Version") String version,
        @PathVariable("key_name") String keyName,
        @RequestParam("locationId") String locationId
    );

    @GetMapping(
            value = "/associations/objectKey/{objectKey}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAssociationByObjectKey(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("objectKey") String objectKey,
            @RequestParam("locationId") String locationId
    );

    @PutMapping(
            value = "/associations/{associationId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateAssociation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("associationId") String associationId,
            @RequestBody GoHighLevelUpdateAssociationRequest request
    );

    @DeleteMapping(
            value = "/associations/{associationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteAssociation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("associationId") String associationId
    );

    @GetMapping(
            value = "/associations/{associationId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAssociationById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("associationId") String associationId
    );

    @PostMapping(
            value = "/associations/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createAssociation(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelCreateAssociationRequest request
    );

    @GetMapping(
            value = "/associations/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAllAssociations(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestParam("limit") int limit,
            @RequestParam("skip") int skip,
            @RequestParam("locationId") String locationId
    );
}
