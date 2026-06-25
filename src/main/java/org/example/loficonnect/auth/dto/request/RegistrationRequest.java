package org.example.loficonnect.auth.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegistrationRequest {

    @NotBlank(message = "user_name is required")
    @Size(min = 3, max = 50, message = "user_name must be between 3 and 50 characters")
    private String userName;

    @NotBlank(message = "password is required")
    @Size(min = 8, max = 100, message = "password must be at least 8 characters")
    private String password;

    @NotBlank(message = "confirm_password is required")
    private String confirmPassword;
}