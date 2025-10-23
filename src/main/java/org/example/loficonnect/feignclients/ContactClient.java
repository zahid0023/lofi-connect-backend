package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.contact.GoHighLevelContactCreateRequest;
import org.example.loficonnect.dto.mapper.contact.GoHighLevelContactUpdateRequest;
import org.example.loficonnect.dto.mapper.contact.GoHighLevelContactUpsertRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "contactClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface ContactClient {
    @GetMapping(
            value = "/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getContact(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId
    );

    @PutMapping(
            value = "/contacts/{contactId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateContact(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @RequestBody GoHighLevelContactUpdateRequest request
    );

    @DeleteMapping(
            value = "/contacts/{contactId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteContact(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId
    );

    @GetMapping(
            value = "/contacts/business/{businessId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getBusinessContacts(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("businessId") String businessId
    );

    @PostMapping(
            value = "/contacts/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createContact(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelContactCreateRequest request
    );

    @PostMapping(
            value = "/contacts/upsert",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode upsertContact(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @RequestBody GoHighLevelContactUpsertRequest request
    );

}
