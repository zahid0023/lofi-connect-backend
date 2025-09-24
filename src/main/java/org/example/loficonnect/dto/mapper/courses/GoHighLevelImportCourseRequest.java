package org.example.loficonnect.dto.mapper.courses;

import lombok.Data;
import org.example.loficonnect.dto.request.courses.ImportCourseRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelImportCourseRequest {
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

    public static GoHighLevelImportCourseRequest fromRequest(ImportCourseRequest request) {
        GoHighLevelImportCourseRequest ghlRequest = new GoHighLevelImportCourseRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setUserId(request.getUserId());

        List<Product> products = request.getProducts().stream()
                .map(product -> {
                    Product ghlProduct = new Product();
                    ghlProduct.setTitle(product.getTitle());
                    ghlProduct.setDescription(product.getDescription());
                    ghlProduct.setImageUrl(product.getImageUrl());

                    List<Category> categories = product.getCategories().stream()
                            .map(category -> {
                                Category ghlCategory = new Category();
                                ghlCategory.setTitle(category.getTitle());
                                ghlCategory.setVisibility(category.getVisibility());
                                ghlCategory.setThumbnailUrl(category.getThumbnailUrl());

                                List<Post> posts = category.getPosts().stream()
                                        .map(post -> {
                                            Post ghlPost = new Post();
                                            ghlPost.setTitle(post.getTitle());
                                            ghlPost.setVisibility(post.getVisibility());
                                            ghlPost.setThumbnailUrl(post.getThumbnailUrl());
                                            ghlPost.setContentType(post.getContentType());
                                            ghlPost.setDescription(post.getDescription());
                                            ghlPost.setBucketVideoUrl(post.getBucketVideoUrl());

                                            List<PostMaterial> postMaterials = post.getPostMaterials().stream()
                                                    .map(material -> {
                                                        PostMaterial ghlPostMaterial = new PostMaterial();
                                                        ghlPostMaterial.setTitle(material.getTitle());
                                                        ghlPostMaterial.setType(material.getType());
                                                        ghlPostMaterial.setUrl(material.getUrl());
                                                        return ghlPostMaterial;
                                                    })
                                                    .collect(Collectors.toList());

                                            ghlPost.setPostMaterials(postMaterials);
                                            return ghlPost;
                                        })
                                        .collect(Collectors.toList());

                                ghlCategory.setPosts(posts);

                                List<SubCategory> subCategories = category.getSubCategories().stream()
                                        .map(subCategory -> {
                                            SubCategory ghlSubCategory = new SubCategory();
                                            ghlSubCategory.setTitle(subCategory.getTitle());
                                            ghlSubCategory.setVisibility(subCategory.getVisibility());
                                            ghlSubCategory.setThumbnailUrl(subCategory.getThumbnailUrl());

                                            // Map subcategory posts
                                            List<Post> subCategoryPosts = subCategory.getPosts().stream()
                                                    .map(subCategoryPost -> {
                                                        Post ghlSubCategoryPost = new Post();
                                                        ghlSubCategoryPost.setTitle(subCategoryPost.getTitle());
                                                        ghlSubCategoryPost.setVisibility(subCategoryPost.getVisibility());
                                                        ghlSubCategoryPost.setThumbnailUrl(subCategoryPost.getThumbnailUrl());
                                                        ghlSubCategoryPost.setContentType(subCategoryPost.getContentType());
                                                        ghlSubCategoryPost.setDescription(subCategoryPost.getDescription());
                                                        ghlSubCategoryPost.setBucketVideoUrl(subCategoryPost.getBucketVideoUrl());

                                                        List<PostMaterial> subCategoryPostMaterials = subCategoryPost.getPostMaterials().stream()
                                                                .map(material -> {
                                                                    PostMaterial ghlPostMaterial = new PostMaterial();
                                                                    ghlPostMaterial.setTitle(material.getTitle());
                                                                    ghlPostMaterial.setType(material.getType());
                                                                    ghlPostMaterial.setUrl(material.getUrl());
                                                                    return ghlPostMaterial;
                                                                })
                                                                .collect(Collectors.toList());

                                                        ghlSubCategoryPost.setPostMaterials(subCategoryPostMaterials);
                                                        return ghlSubCategoryPost;
                                                    })
                                                    .collect(Collectors.toList());

                                            ghlSubCategory.setPosts(subCategoryPosts);
                                            return ghlSubCategory;
                                        })
                                        .collect(Collectors.toList());

                                ghlCategory.setSubCategories(subCategories);
                                return ghlCategory;
                            })
                            .collect(Collectors.toList());

                    ghlProduct.setCategories(categories);

                    InstructorDetails instructorDetails = new InstructorDetails();
                    instructorDetails.setName(product.getInstructorDetails().getName());
                    instructorDetails.setDescription(product.getInstructorDetails().getDescription());
                    ghlProduct.setInstructorDetails(instructorDetails);

                    return ghlProduct;
                })
                .collect(Collectors.toList());

        ghlRequest.setProducts(products);
        return ghlRequest;
    }
}
