package org.example.loficonnect.dto.mapper.opportunityfollower;

import lombok.Data;
import org.example.loficonnect.dto.request.opportunityfollower.OpportunityRemoveFollowersRequest;

import java.util.List;

@Data
public class GoHighLevelOpportunityRemoveFollowersRequest {
    private List<String> followers;

    public static GoHighLevelOpportunityRemoveFollowersRequest fromRequest(OpportunityRemoveFollowersRequest request) {
        GoHighLevelOpportunityRemoveFollowersRequest ghlRequest = new GoHighLevelOpportunityRemoveFollowersRequest();
        ghlRequest.setFollowers(request.getFollowers());
        return ghlRequest;
    }
}