package org.example.loficonnect.dto.request.oauthfacebook;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttachFacebookPageRequest {
    private String type;
    private String origin_id;
    private String name;
    private String avatar;
    private String company_id;
}
