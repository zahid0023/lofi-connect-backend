package org.example.loficonnect.dto.request.oauthgoogle;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OauthGoogleBusinessLocationRequest {
    private Map<String, Object> location;
    private Map<String, Object> account;
    private String company_id;
}
