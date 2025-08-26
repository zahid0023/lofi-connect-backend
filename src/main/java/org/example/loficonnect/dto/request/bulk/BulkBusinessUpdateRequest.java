package org.example.loficonnect.dto.request.bulk;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BulkBusinessUpdateRequest {
    private String locationId;
    private List<String> ids;
    private String businessId; // nullable → if null, removes business from contacts
}
