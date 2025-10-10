package org.example.loficonnect.dto.mapper.user;

import lombok.Data;
import org.example.loficonnect.dto.request.user.UserUpdateRequest;

import java.util.List;
import java.util.Map;

@Data
public class GoHighLevelUserUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String emailChangeOTP;
    private String password;
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

    public static GoHighLevelUserUpdateRequest fromRequest(UserUpdateRequest request) {
        GoHighLevelUserUpdateRequest ghlRequest = new GoHighLevelUserUpdateRequest();
        ghlRequest.setFirstName(request.getFirstName());
        ghlRequest.setLastName(request.getLastName());
        ghlRequest.setEmail(request.getEmail());
        ghlRequest.setEmailChangeOTP(request.getEmailChangeOtp());
        ghlRequest.setPassword(request.getPassword());
        ghlRequest.setPhone(request.getPhone());
        ghlRequest.setIsEjectedUser(request.getIsEjectedUser());
        ghlRequest.setType(request.getType());
        ghlRequest.setRole(request.getRole());
        ghlRequest.setCompanyId(request.getCompanyId());
        ghlRequest.setLocationIds(request.getLocationIds());
        ghlRequest.setPermissions(request.getPermissions());
        ghlRequest.setScopes(request.getScopes());
        ghlRequest.setScopesAssignedToOnly(request.getScopesAssignedToOnly());
        ghlRequest.setProfilePhoto(request.getProfilePhoto());
        return ghlRequest;
    }
}
