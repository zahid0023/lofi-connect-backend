package org.example.loficonnect.dto.request.campaigns;

import lombok.Data;
import org.example.loficonnect.dto.mapper.campaigns.ContactCampaignAssignRequest;

@Data
public class GoHighLevelContactCampaignAssignRequest {

    public static GoHighLevelContactCampaignAssignRequest fromRequest(ContactCampaignAssignRequest request) {
        return new GoHighLevelContactCampaignAssignRequest();
    }
}
