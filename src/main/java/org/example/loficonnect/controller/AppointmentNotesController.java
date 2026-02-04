package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.appointmentnotes.AppointmentNoteCreateRequest;
import org.example.loficonnect.dto.request.appointmentnotes.AppointmentNoteUpdateRequest;
import org.example.loficonnect.service.AppointmentNotesService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class AppointmentNotesController {
    private final AppointmentNotesService appointmentNotesService;

    public AppointmentNotesController(AppointmentNotesService appointmentNotesService) {
        this.appointmentNotesService = appointmentNotesService;
    }

    @AppKey
    @GetMapping("/appointments/{appointment-id}/notes")
    public ResponseEntity<?> getAppointmentNotes(
            @PathVariable("appointment-id") String appointmentId,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "limit", limit);
        MapUtil.putIfNotNull(queryParams, "offset", offset);

        return ResponseEntity.ok(appointmentNotesService.getAppointmentNotes(appointmentId, queryParams));
    }

    @AppKey
    @PostMapping("/appointments/{appointment-id}/notes")
    public ResponseEntity<?> createAppointmentNote(
            @PathVariable("appointment-id") String appointmentId,
            @RequestBody AppointmentNoteCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentNotesService.createAppointmentNote(appointmentId, request));
    }

    @AppKey
    @PutMapping("/appointments/{appointment-id}/notes/{note-id}")
    public ResponseEntity<?> updateAppointmentNote(
            @PathVariable("appointment-id") String appointmentId,
            @PathVariable("note-id") String noteId,
            @RequestBody AppointmentNoteUpdateRequest request
    ) {
        return ResponseEntity.ok(appointmentNotesService.updateAppointmentNote(appointmentId, noteId, request));
    }

    @AppKey
    @DeleteMapping("/appointments/{appointment-id}/notes/{note-id}")
    public ResponseEntity<?> deleteAppointmentNote(
            @PathVariable("appointment-id") String appointmentId,
            @PathVariable("note-id") String noteId
    ) {
        return ResponseEntity.ok(appointmentNotesService.deleteAppointmentNote(appointmentId, noteId));
    }
}
