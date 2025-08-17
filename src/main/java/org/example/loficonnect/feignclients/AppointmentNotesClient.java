package org.example.loficonnect.feignclients;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.config.FeignLoggingConfig;
import org.example.loficonnect.dto.mapper.appointmentnotes.GoHighLevelAppointmentNoteCreateRequest;
import org.example.loficonnect.dto.mapper.appointmentnotes.GoHighLevelAppointmentNoteUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "appointmentNotesClient", url = "https://services.leadconnectorhq.com", configuration = FeignLoggingConfig.class)
public interface AppointmentNotesClient {
    @GetMapping(
            value = "/calendars/appointments/{appointmentId}/notes",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode getAppointmentNotes(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("appointmentId") String appointmentId,
            @RequestParam Map<String, Object> queryParams
    );

    @PostMapping(
            value = "/calendars/appointments/{appointmentId}/notes",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode createAppointmentNote(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("appointmentId") String appointmentId,
            @RequestBody GoHighLevelAppointmentNoteCreateRequest request
    );

    @PutMapping(
            value = "/calendars/appointments/{appointmentId}/notes/{noteId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode updateAppointmentNote(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("appointmentId") String appointmentId,
            @PathVariable("noteId") String noteId,
            @RequestBody GoHighLevelAppointmentNoteUpdateRequest request
    );

    @DeleteMapping(
            value = "/calendars/appointments/{appointmentId}/notes/{noteId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    JsonNode deleteAppointmentNote(
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("Version") String version,
            @PathVariable("appointmentId") String appointmentId,
            @PathVariable("noteId") String noteId
    );


}
