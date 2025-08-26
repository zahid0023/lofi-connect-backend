package org.example.loficonnect.dto.mapper.follower;

import lombok.Data;
import org.example.loficonnect.dto.request.follower.AddFollowersRequest;

import java.util.List;

@Data
public class GoHighLevelAddFollowersRequest {
    private List<String> followers;

    public static GoHighLevelAddFollowersRequest fromRequest(AddFollowersRequest request) {
        GoHighLevelAddFollowersRequest ghlRequest = new GoHighLevelAddFollowersRequest();
        ghlRequest.setFollowers(request.getFollowers());
        return ghlRequest;
    }
}
