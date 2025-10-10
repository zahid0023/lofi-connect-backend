package org.example.loficonnect.dto.mapper.opportunityfollower;

import lombok.Data;
import org.example.loficonnect.dto.request.opportunityfollower.OpportunityFollowerRequest;

import java.util.List;

@Data
public class GoHighLevelOpportunityFollowerRequest {
    private List<String> followers;

    public static GoHighLevelOpportunityFollowerRequest fromRequest(OpportunityFollowerRequest request) {
        GoHighLevelOpportunityFollowerRequest ghl = new GoHighLevelOpportunityFollowerRequest();
        ghl.setFollowers(request.getFollowers());
        return ghl;
    }
}
