package org.example.loficonnect.dto.mapper.bulk;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.bulk.BulkBusinessUpdateRequest;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelBulkBusinessUpdateRequest {

    @JsonAlias("location_id")
    private String locationId;

    @JsonAlias("contact_ids")
    private List<String> ids;

    @JsonAlias("business_id")
    private String businessId;

    private GoHighLevelBulkBusinessUpdateRequest() {
        // Prevent direct instantiation
    }

    public static GoHighLevelBulkBusinessUpdateRequest fromRequest(BulkBusinessUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelBulkBusinessUpdateRequest.class);
    }
}
