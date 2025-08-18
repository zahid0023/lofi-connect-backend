package org.example.loficonnect.dto.request.task;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskCreateRequest {
    private String title;
    private String body;
    private LocalDate due_date;
    private LocalTime due_time;
    private Boolean completed;
    private String assigned_to;
}
