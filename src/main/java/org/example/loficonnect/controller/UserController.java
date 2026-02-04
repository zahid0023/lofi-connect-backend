package org.example.loficonnect.controller;

import org.example.loficonnect.commons.annotation.AppKey;
import org.example.loficonnect.dto.request.user.UserCreateRequest;
import org.example.loficonnect.dto.request.user.UserUpdateRequest;
import org.example.loficonnect.service.GhlUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class UserController {

    private final GhlUserService ghlUserService;

    public UserController(GhlUserService ghlUserService) {
        this.ghlUserService = ghlUserService;
    }

    @AppKey
    @GetMapping("/users/{user-id}")
    public ResponseEntity<?> getUserById(@PathVariable("user-id") String userId) {
        return ResponseEntity.ok(ghlUserService.getUserById(userId));
    }

    @AppKey
    @PutMapping("/users/{user-id}")
    public ResponseEntity<?> updateUserById(
            @PathVariable("user-id") String userId,
            @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(ghlUserService.updateUserById(userId, request));
    }

    @AppKey
    @DeleteMapping("/users/{user-id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("user-id") String userId) {
        return ResponseEntity.ok(ghlUserService.deleteUserById(userId));
    }

    @AppKey
    @GetMapping("/users")
    public ResponseEntity<?> getUsersByLocation() {
        Map<String, Object> queryParams = new HashMap<>();
        return ResponseEntity.ok(ghlUserService.getUsersByLocation(queryParams));
    }

    @AppKey
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ghlUserService.createUser(request));
    }
}
