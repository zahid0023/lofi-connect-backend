package org.example.loficonnect.dto.mapper.collections;

import lombok.Data;
import org.example.loficonnect.dto.request.collections.CollectionCreateRequest;

@Data
public class GoHighLevelCollectionCreateRequest {
    private String altId;
    private String altType;
    private String collectionId;
    private String name;
    private String slug;
    private String image;
    private Seo seo;

    @Data
    public static class Seo {
        private String title;
        private String description;
    }

    public static GoHighLevelCollectionCreateRequest fromRequest(CollectionCreateRequest request) {
        GoHighLevelCollectionCreateRequest ghlRequest = new GoHighLevelCollectionCreateRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setCollectionId(request.getCollectionId());
        ghlRequest.setName(request.getName());
        ghlRequest.setSlug(request.getSlug());
        ghlRequest.setImage(request.getImage());

        if (request.getSeo() != null) {
            Seo seo = new Seo();
            seo.setTitle(request.getSeo().getTitle());
            seo.setDescription(request.getSeo().getDescription());
            ghlRequest.setSeo(seo);
        }

        return ghlRequest;
    }
}
