package org.example.loficonnect.dto.request.review;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewBulkUpdateRequest {
    private String altId;
    private String altType;
    private List<Review> reviews;
    private String status;

    @Data
    public static class Review {
        private String reviewId;
        private String productId;
        private String storeId;
    }
}
