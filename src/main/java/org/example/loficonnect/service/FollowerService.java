package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.follower.AddFollowersRequest;
import org.example.loficonnect.dto.request.follower.RemoveFollowersRequest;

public interface FollowerService {
    JsonNode addFollowers(String contactId, AddFollowersRequest request);

    JsonNode removeFollowers(String contactId, RemoveFollowersRequest request);
}
