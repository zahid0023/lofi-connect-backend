package org.example.loficonnect.dto.mapper.triggerlinks;

import lombok.Data;
import org.example.loficonnect.dto.request.triggerlinks.LinkCreateRequest;

@Data
public class GoHighLevelLinkCreateRequest {
    private String locationId;
    private String name;
    private String redirectTo;

    public static GoHighLevelLinkCreateRequest fromRequest(LinkCreateRequest request) {
        GoHighLevelLinkCreateRequest ghlRequest = new GoHighLevelLinkCreateRequest();
        ghlRequest.setLocationId(request.getLocation_id());
        ghlRequest.setName(request.getName());
        ghlRequest.setRedirectTo(request.getRedirect_to());
        return ghlRequest;
    }
}
