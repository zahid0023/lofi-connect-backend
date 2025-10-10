package org.example.loficonnect.dto.request.tag;

import lombok.Data;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TagByIdsRequest {
    private List<String> tagIds;
}
