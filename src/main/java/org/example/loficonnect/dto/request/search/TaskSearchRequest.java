package org.example.loficonnect.dto.request.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskSearchRequest {
    private List<String> contactId;
    private Boolean completed;
    private List<String> assignedTo;
    private String query;
    private Integer limit;
    private Integer skip;
    private String businessId;
}
