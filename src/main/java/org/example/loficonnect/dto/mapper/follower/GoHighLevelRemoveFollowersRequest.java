package org.example.loficonnect.dto.mapper.follower;

import lombok.Data;
import org.example.loficonnect.dto.request.follower.RemoveFollowersRequest;

import java.util.List;

@Data
public class GoHighLevelRemoveFollowersRequest {
    private List<String> followers;

    public static GoHighLevelRemoveFollowersRequest fromRequest(RemoveFollowersRequest request) {
        GoHighLevelRemoveFollowersRequest ghlRequest = new GoHighLevelRemoveFollowersRequest();
        ghlRequest.setFollowers(request.getFollowers());
        return ghlRequest;
    }
}
