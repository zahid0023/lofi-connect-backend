package org.example.loficonnect.dto.mapper.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.loficonnect.dto.request.user.UserCreateRequest;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoHighLevelUserCreateRequest {

    @JsonAlias("company_id")
    private String companyId;

    @JsonAlias("first_name")
    private String firstName;

    @JsonAlias("last_name")
    private String lastName;

    private String email;
    private String password;
    private String phone;
    private String type;
    private String role;

    @JsonAlias("location_ids")
    private List<String> locationIds;

    private Map<String, Boolean> permissions;
    private List<String> scopes;

    @JsonAlias("scopes_assigned_to_only")
    private List<String> scopesAssignedToOnly;

    @JsonAlias("profile_photo")
    private String profilePhoto;

    /**
     * Converts UserCreateRequest -> GoHighLevelUserCreateRequest using ObjectMapper.
     */
    public static GoHighLevelUserCreateRequest fromRequest(UserCreateRequest request, ObjectMapper objectMapper) {
        return objectMapper.convertValue(request, GoHighLevelUserCreateRequest.class);
    }
}       