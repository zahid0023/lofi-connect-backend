package org.example.loficonnect.dto.request.calendar;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TeamMemberRequest {
    private String userId;
    private Double priority;
    private Boolean isPrimary;
    private List<LocationConfigurationRequest> locationConfigurations;
}
