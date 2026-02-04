package org.example.loficonnect.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VerifyOtpResponse {
    private final String resetToken;

    public VerifyOtpResponse(String resetToken) {
        this.resetToken = resetToken;
    }
}
