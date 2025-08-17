package org.example.loficonnect.dto.request.appointmentnotes;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppointmentNoteUpdateRequest {
    private String userId;
    private String body;
}
