package org.example.loficonnect.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegistrationRequest {
    private String fullName;

    private String userName;

    private String password;

    private String confirmPassword;
}