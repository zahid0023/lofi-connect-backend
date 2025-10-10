package org.example.loficonnect.dto.request.blogs;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BlogPostUpdateRequest {
    private String title;
    private String locationId;
    private String blogId;
    private String imageUrl;
    private String description;
    private String rawHtml;
    private String status;
    private String imageAltText;
    private List<String> categories;
    private List<String> tags;
    private String author;
    private String urlSlug;
    private String canonicalLink;

    private LocalDate publishedDate;
    private LocalTime publishedTime;
    private String timeZone;
}
