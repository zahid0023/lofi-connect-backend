package org.example.loficonnect.dto.request.workflow;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactWorkflowDeleteRequest {
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String timeZone;
}
