package org.example.loficonnect.dto.mapper.campaigns;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactCampaignAssignRequest {
    // Empty payload as per the curl '{}' body
}
