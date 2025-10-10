package org.example.loficonnect.dto.request.courses;

import lombok.Data;
import java.util.List;

@Data
public class ImportCourseRequest {
    private String locationId;
    private String userId;
    private List<Product> products;

    @Data
    public static class Product {
        private String title;
        private String description;
        private String imageUrl;
        private List<Category> categories;
        private InstructorDetails instructorDetails;
    }

    @Data
    public static class Category {
        private String title;
        private String visibility;
        private String thumbnailUrl;
        private List<Post> posts;
        private List<SubCategory> subCategories;
    }

    @Data
    public static class SubCategory {
        private String title;
        private String visibility;
        private String thumbnailUrl;
        private List<Post> posts;
    }

    @Data
    public static class Post {
        private String title;
        private String visibility;
        private String thumbnailUrl;
        private String contentType;
        private String description;
        private String bucketVideoUrl;
        private List<PostMaterial> postMaterials;
    }

    @Data
    public static class PostMaterial {
        private String title;
        private String type;
        private String url;
    }

    @Data
    public static class InstructorDetails {
        private String name;
        private String description;
    }
}
