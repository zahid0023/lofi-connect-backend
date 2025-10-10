package org.example.loficonnect.dto.mapper.user;

import lombok.Data;
import org.example.loficonnect.dto.request.user.UserCreateRequest;

import java.util.List;
import java.util.Map;

@Data
public class GoHighLevelUserCreateRequest {
    private String companyId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String type;
    private String role;
    private List<String> locationIds;
    private Map<String, Boolean> permissions;
    private List<String> scopes;
    private List<String> scopesAssignedToOnly;
    private String profilePhoto;

    public static GoHighLevelUserCreateRequest fromRequest(UserCreateRequest request) {
        GoHighLevelUserCreateRequest ghlRequest = new GoHighLevelUserCreateRequest();
        ghlRequest.setCompanyId(request.getCompanyId());
        ghlRequest.setFirstName(request.getFirstName());
        ghlRequest.setLastName(request.getLastName());
        ghlRequest.setEmail(request.getEmail());
        ghlRequest.setPassword(request.getPassword());
        ghlRequest.setPhone(request.getPhone());
        ghlRequest.setType(request.getType());
        ghlRequest.setRole(request.getRole());
        ghlRequest.setLocationIds(request.getLocationIds());
        ghlRequest.setPermissions(request.getPermissions());
        ghlRequest.setScopes(request.getScopes());
        ghlRequest.setScopesAssignedToOnly(request.getScopesAssignedToOnly());
        ghlRequest.setProfilePhoto(request.getProfilePhoto());
        return ghlRequest;
    }
}
