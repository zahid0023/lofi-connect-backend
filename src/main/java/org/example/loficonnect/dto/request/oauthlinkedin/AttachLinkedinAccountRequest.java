package org.example.loficonnect.dto.request.oauthlinkedin;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AttachLinkedinAccountRequest {
    private String type;
    private String originId;
    private String name;
    private String avatar;
    private String urn;
    private String companyId;
}
