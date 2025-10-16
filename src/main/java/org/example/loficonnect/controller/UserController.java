package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.user.UserCreateRequest;
import org.example.loficonnect.dto.request.user.UserUpdateRequest;
import org.example.loficonnect.service.UserService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @AppKey
    @GetMapping("/users/{user-id}")
    public ResponseEntity<?> getUserById(@PathVariable("user-id") String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @AppKey
    @PutMapping("/users/{user-id}")
    public ResponseEntity<?> updateUserById(
            @PathVariable("user-id") String userId,
            @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updateUserById(userId, request));
    }

    @AppKey
    @DeleteMapping("/users/{user-id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("user-id") String userId) {
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }

    @AppKey
    @GetMapping("/users")
    public ResponseEntity<?> getUsersByLocation(
            @RequestParam("location-id") String locationId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "locationId", locationId);
        return ResponseEntity.ok(userService.getUsersByLocation(queryParams));
    }

    @AppKey
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

}
