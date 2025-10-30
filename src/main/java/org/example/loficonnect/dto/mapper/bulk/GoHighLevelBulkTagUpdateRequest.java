package org.example.loficonnect.dto.mapper.bulk;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.bulk.BulkTagUpdateRequest;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelBulkTagUpdateRequest {

    @JsonAlias("contact_ids")
    private List<String> contacts;

    @JsonAlias("tags")
    private List<String> tags;

    @JsonAlias("location_id")
    private String locationId;

    @JsonAlias("remove_all_tags")
    private Boolean removeAllTags;

    private GoHighLevelBulkTagUpdateRequest() {
        // private constructor to enforce static method usage
    }

    public static GoHighLevelBulkTagUpdateRequest fromRequest(BulkTagUpdateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelBulkTagUpdateRequest.class);
    }
}