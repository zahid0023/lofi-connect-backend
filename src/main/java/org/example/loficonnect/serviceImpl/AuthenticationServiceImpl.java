package org.example.loficonnect.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.loficonnect.dto.request.authentication.UserRegisterRequest;
import org.example.loficonnect.dto.response.SuccessResponse;
import org.example.loficonnect.exception.PasswordMismatchException;
import org.example.loficonnect.model.entity.UserEntity;
import org.example.loficonnect.repository.UserRepository;
import org.example.loficonnect.service.AutheticationService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AutheticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SuccessResponse registerUser(UserRegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException();
        }
                
        if (userRepository.existsByUsername(request.getEmail())) {
            throw new RuntimeException("Username already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
        userRepository.save(user);

        return new SuccessResponse(true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }
}
