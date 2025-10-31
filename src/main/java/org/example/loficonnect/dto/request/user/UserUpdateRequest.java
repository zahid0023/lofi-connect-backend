package org.example.loficonnect.dto.request.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String emailChangeOtp;
    private String pass;
    private String phone;
    private Boolean isEjectedUser;
    private String type;
    private String role;
    private String companyId;
    private List<String> locationIds;
    private Map<String, Boolean> permissions;
    private List<String> scopes;
    private List<String> scopesAssignedToOnly;
    private String profilePhoto;
}
