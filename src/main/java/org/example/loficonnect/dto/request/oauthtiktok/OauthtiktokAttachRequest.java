package org.example.loficonnect.dto.request.oauthtiktok;

import lombok.Data;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OauthtiktokAttachRequest {
    private String type;
    private String originId;
    private String name;
    private String avatar;
    private Boolean verified;
    private String username;
    private String companyId;
}
