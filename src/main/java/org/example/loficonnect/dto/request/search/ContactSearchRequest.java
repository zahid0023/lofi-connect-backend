package org.example.loficonnect.dto.request.search;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactSearchRequest {
    private String phone;
    private String email;
    private Integer pageLimit;
    private Integer page;
}
