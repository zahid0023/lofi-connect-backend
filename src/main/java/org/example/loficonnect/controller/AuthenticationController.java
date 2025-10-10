package org.example.loficonnect.controller;

import org.example.loficonnect.dto.request.authentication.UserLoginRequest;
import org.example.loficonnect.dto.request.authentication.UserRegisterRequest;
import org.example.loficonnect.service.AutheticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authorization")
public class AuthenticationController {
    private final AutheticationService autheticationService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(AutheticationService autheticationService, AuthenticationManager authenticationManager) {
        this.autheticationService = autheticationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autheticationService.registerUser(userRegisterRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
        return ResponseEntity.ok().body(authentication);
    }
}
