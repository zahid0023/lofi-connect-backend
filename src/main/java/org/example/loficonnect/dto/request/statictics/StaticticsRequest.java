package org.example.loficonnect.dto.request.statictics;

import lombok.Data;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StaticticsRequest {
    private List<String> profileIds;
    private List<String> platforms;
}
