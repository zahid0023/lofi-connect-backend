package org.example.loficonnect.service;

import org.example.loficonnect.dto.request.users.UserRegisterRequest;
import org.example.loficonnect.dto.response.SuccessResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    SuccessResponse registerUser(UserRegisterRequest request);
}
