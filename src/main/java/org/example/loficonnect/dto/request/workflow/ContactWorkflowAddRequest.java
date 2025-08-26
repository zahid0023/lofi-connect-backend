package org.example.loficonnect.dto.request.workflow;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactWorkflowAddRequest {
    private LocalDate date;   // User provides the date part
    private LocalTime time;   // User provides the time part
    private String timeZone;  // User provides the timezone
}
