package org.example.loficonnect.dto.request.oauthinstagram;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttachInstagramAccountRequest {
    private String origin_id;
    private String name;
    private String avatar;
    private String page_id;
    private String company_id;
}
