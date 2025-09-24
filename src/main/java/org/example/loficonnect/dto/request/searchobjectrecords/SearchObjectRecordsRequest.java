package org.example.loficonnect.dto.request.searchobjectrecords;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchObjectRecordsRequest {
    private String locationId;
    private int page;
    private int pageLimit;
    private String query;
    private List<String> searchAfter;
}
