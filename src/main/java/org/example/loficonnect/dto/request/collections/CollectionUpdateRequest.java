package org.example.loficonnect.dto.request.collections;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CollectionUpdateRequest {
    private String altId;
    private String altType;
    private String name;
    private String slug;
    private String image;
    private Seo seo;

    @Data
    public static class Seo {
        private String title;
        private String description;
    }
}
