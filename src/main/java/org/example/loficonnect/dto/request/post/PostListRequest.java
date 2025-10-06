package org.example.loficonnect.dto.request.post;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostListRequest {
    private String type;
    private String accounts;
    private String skip;
    private String limit;
    private String from_date;
    private String to_date;
    private String include_users;
    private String post_type;
}
