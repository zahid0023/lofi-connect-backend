package org.example.loficonnect.auth.controller;

import org.example.loficonnect.auth.config.JwtTokenProvider;
import org.example.loficonnect.auth.dto.request.ForgotPasswordRequest;
import org.example.loficonnect.auth.dto.request.LoginRequest;
import org.example.loficonnect.auth.dto.request.ResetPasswordRequest;
import org.example.loficonnect.auth.dto.request.VerifyOtpRequest;
import org.example.loficonnect.auth.dto.response.LoginResponse;
import org.example.loficonnect.auth.model.enitty.UserEntity;
import org.example.loficonnect.auth.service.PasswordResetService;
import org.example.loficonnect.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordResetService passwordResetService;
    private final UserService userService;

    public LoginController(AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider, PasswordResetService passwordResetService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordResetService = passwordResetService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        } catch (DisabledException ex) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("User account is disabled");

        } catch (LockedException ex) {
            return ResponseEntity
                    .status(HttpStatus.LOCKED)
                    .body("User account is locked");
        } catch (AccountExpiredException ex) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("User account is expired");

        } catch (AuthenticationException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Authentication failed");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        UserEntity userEntity = userService.getUserByUsername(forgotPasswordRequest.getUserName());
        return ResponseEntity.ok(passwordResetService.forgotPassword(userEntity));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return null;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        passwordResetService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password has been reset successfully.");
    }

}