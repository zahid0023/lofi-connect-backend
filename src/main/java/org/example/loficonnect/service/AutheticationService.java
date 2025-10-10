package org.example.loficonnect.service;

import org.example.loficonnect.dto.request.authentication.UserRegisterRequest;
import org.example.loficonnect.dto.response.SuccessResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AutheticationService extends UserDetailsService {
    SuccessResponse registerUser(UserRegisterRequest request);
}
