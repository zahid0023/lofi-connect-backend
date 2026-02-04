package org.example.loficonnect.auth.service;

import org.example.loficonnect.auth.dto.request.RegistrationRequest;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    SuccessResponse registerAdmin(RegistrationRequest request);

    SuccessResponse registerUser(RegistrationRequest request);

    UserEntity getUserById(Long id);

    SuccessResponse activateUser(UserEntity userEntity);

    Boolean existsSuperAdmin(String username);

    UserEntity getAuthenticatedUserEntity();

    UserEntity getUserByUsername(String username);
}
