package org.example.loficonnect.dto.mapper.usersearch;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.usersearch.UserSearchRequest;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelUserSearchRequest {

    @JsonAlias("company_id")
    private String companyId;

    private List<String> emails;
    private Boolean deleted;
    private String skip;
    private String limit;
    private String projection;

    private GoHighLevelUserSearchRequest(){

    }

    public static GoHighLevelUserSearchRequest fromRequest(UserSearchRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelUserSearchRequest.class);
    }

}
