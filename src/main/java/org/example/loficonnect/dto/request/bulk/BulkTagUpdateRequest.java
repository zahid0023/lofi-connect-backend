package org.example.loficonnect.dto.request.bulk;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BulkTagUpdateRequest {
    private List<String> contactIds;
    private List<String> tags;
    private String locationId;
    private Boolean removeAllTags;
}
