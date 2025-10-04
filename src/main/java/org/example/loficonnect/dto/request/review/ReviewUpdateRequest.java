package org.example.loficonnect.dto.request.review;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewUpdateRequest {
    private String altId;
    private String altType;
    private String productId;
    private String status;
    private List<Reply> reply;
    private String rating;
    private String headline;
    private String detail;

    @Data
    public static class Reply {
        private String headline;
        private String comment;
        private User user;
    }

    @Data
    public static class User {
        private String name;
        private String email;
        private String phone;
        private Boolean isCustomer;
    }
}
