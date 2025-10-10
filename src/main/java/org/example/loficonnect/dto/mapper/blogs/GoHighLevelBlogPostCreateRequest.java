package org.example.loficonnect.dto.mapper.blogs;

import lombok.Data;
import org.example.loficonnect.dto.request.blogs.BlogPostCreateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class GoHighLevelBlogPostCreateRequest {
    private String title;
    private String locationId;
    private String blogId;
    private String imageUrl;
    private String description;
    private String rawHTML;
    private String status;
    private String imageAltText;
    private List<String> categories;
    private List<String> tags;
    private String author;
    private String urlSlug;
    private String canonicalLink;
    private ZonedDateTime publishedAt;

    public static GoHighLevelBlogPostCreateRequest fromRequest(BlogPostCreateRequest request) {
        GoHighLevelBlogPostCreateRequest ghl = new GoHighLevelBlogPostCreateRequest();
        ghl.setTitle(request.getTitle());
        ghl.setLocationId(request.getLocationId());
        ghl.setBlogId(request.getBlogId());
        ghl.setImageUrl(request.getImageUrl());
        ghl.setDescription(request.getDescription());
        ghl.setRawHTML(request.getRawHtml());
        ghl.setStatus(request.getStatus());
        ghl.setImageAltText(request.getImageAltText());
        ghl.setCategories(request.getCategories());
        ghl.setTags(request.getTags());
        ghl.setAuthor(request.getAuthor());
        ghl.setUrlSlug(request.getUrlSlug());
        ghl.setCanonicalLink(request.getCanonicalLink());

        LocalDate d = request.getPublishedDate();
        LocalTime t = request.getPublishedTime();
        String zoneId = request.getTimeZone();
        if (d != null && t != null && zoneId != null) {
            ZoneId zone;
            try {
                zone = zoneId.isBlank() ? ZoneId.systemDefault() : ZoneId.of(zoneId);
            } catch (Exception e) {
                zone = ZoneId.systemDefault();
            }
            ghl.setPublishedAt(ZonedDateTime.of(LocalDateTime.of(d, t), zone));
        } else {
            ghl.setPublishedAt(null);
        }
        return ghl;
    }
}
