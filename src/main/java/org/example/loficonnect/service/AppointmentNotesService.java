package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.appointmentnotes.AppointmentNoteCreateRequest;
import org.example.loficonnect.dto.request.appointmentnotes.AppointmentNoteUpdateRequest;

import java.util.Map;

public interface AppointmentNotesService {
    JsonNode getAppointmentNotes(String appointmentId, Map<String, Object> queryParams);

    JsonNode createAppointmentNote(String appointmentId, AppointmentNoteCreateRequest request);

    JsonNode updateAppointmentNote(String appointmentId, String noteId, AppointmentNoteUpdateRequest request);

    JsonNode deleteAppointmentNote(String appointmentId, String noteId);

}
