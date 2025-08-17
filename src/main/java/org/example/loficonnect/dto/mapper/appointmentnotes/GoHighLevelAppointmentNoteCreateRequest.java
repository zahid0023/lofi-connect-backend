package org.example.loficonnect.dto.mapper.appointmentnotes;

import lombok.Data;
import org.example.loficonnect.dto.request.appointmentnotes.AppointmentNoteCreateRequest;

@Data
public class GoHighLevelAppointmentNoteCreateRequest {
    private String userId;
    private String body;

    public static GoHighLevelAppointmentNoteCreateRequest fromRequest(AppointmentNoteCreateRequest request) {
        GoHighLevelAppointmentNoteCreateRequest ghlRequest = new GoHighLevelAppointmentNoteCreateRequest();
        ghlRequest.setUserId(request.getUserId());
        ghlRequest.setBody(request.getBody());
        return ghlRequest;
    }
}
