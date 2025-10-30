package org.example.loficonnect.dto.mapper.follower;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.follower.RemoveFollowersRequest;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelRemoveFollowersRequest {

    @JsonAlias("follower_ids")
    private List<String> followers;

    private GoHighLevelRemoveFollowersRequest() {
        // Prevent direct instantiation
    }

    public static GoHighLevelRemoveFollowersRequest fromRequest(RemoveFollowersRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelRemoveFollowersRequest.class);
    }
}