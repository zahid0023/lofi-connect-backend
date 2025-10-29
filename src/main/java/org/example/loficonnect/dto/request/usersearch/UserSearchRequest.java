package org.example.loficonnect.dto.request.usersearch;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserSearchRequest {
    private String companyId;
    private List<String> emails;
    private Boolean deleted;
    private String skip;
    private String limit;
    private String projection;
}
