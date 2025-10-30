package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.follower.AddFollowersRequest;
import org.example.loficonnect.dto.request.follower.RemoveFollowersRequest;
import org.example.loficonnect.service.FollowerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ghl")
public class FollowerController {
    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @AppKey
    @PostMapping("/contacts/{contact-id}/followers")
    public ResponseEntity<?> addFollowers(
            @PathVariable("contact-id") String contactId,
            @RequestBody AddFollowersRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(followerService.addFollowers(contactId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}/followers")
    public ResponseEntity<?> removeFollowers(
            @PathVariable("contact-id") String contactId,
            @RequestBody RemoveFollowersRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(followerService.removeFollowers(contactId, request));
    }

}
