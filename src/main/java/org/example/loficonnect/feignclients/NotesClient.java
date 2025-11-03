package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.notes.GoHighLevelContactNoteCreateRequest;
import org.example.loficonnect.dto.mapper.notes.GoHighLevelContactNoteUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "notesClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface NotesClient {
    @GetMapping(
            value = "/contacts/{contactId}/notes",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getContactNotes(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId
    );

    @PostMapping(
            value = "/contacts/{contactId}/notes",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createContactNote(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @RequestBody GoHighLevelContactNoteCreateRequest request
    );

    @GetMapping(
            value = "/contacts/{contactId}/notes/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getContactNoteById(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("id") String noteId
    );
    @PutMapping(
            value = "/contacts/{contactId}/notes/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateContactNote(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("id") String noteId,
            @RequestBody GoHighLevelContactNoteUpdateRequest request
    );
    @DeleteMapping(
            value = "/contacts/{contactId}/notes/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteContactNote(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("contactId") String contactId,
            @PathVariable("id") String noteId
    );
}
