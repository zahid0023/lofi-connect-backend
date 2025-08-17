package org.example.loficonnect.dto.mapper.appointmentnotes;

import lombok.Data;
import org.example.loficonnect.dto.request.appointmentnotes.AppointmentNoteUpdateRequest;

@Data
public class GoHighLevelAppointmentNoteUpdateRequest {
    private String userId;
    private String body;

    public static GoHighLevelAppointmentNoteUpdateRequest fromRequest(AppointmentNoteUpdateRequest request) {
        GoHighLevelAppointmentNoteUpdateRequest ghlRequest = new GoHighLevelAppointmentNoteUpdateRequest();
        ghlRequest.setUserId(request.getUserId());
        ghlRequest.setBody(request.getBody());
        return ghlRequest;
    }
}
