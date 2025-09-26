package org.example.loficonnect.dto.mapper.triggerlinks;

import lombok.Data;
import org.example.loficonnect.dto.request.triggerlinks.LinkUpdateRequest;

@Data
public class GoHighLevelLinkUpdateRequest {
    private String name;
    private String redirectTo;

    public static GoHighLevelLinkUpdateRequest fromRequest(LinkUpdateRequest request) {
        GoHighLevelLinkUpdateRequest ghlRequest = new GoHighLevelLinkUpdateRequest();
        ghlRequest.setName(request.getName());
        ghlRequest.setRedirectTo(request.getRedirect_to());
        return ghlRequest;
    }
}
