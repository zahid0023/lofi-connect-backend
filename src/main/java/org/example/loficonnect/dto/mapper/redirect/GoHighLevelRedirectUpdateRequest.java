package org.example.loficonnect.dto.mapper.redirect;

import lombok.Data;
import org.example.loficonnect.dto.request.redirect.RedirectUpdateRequest;

@Data
public class GoHighLevelRedirectUpdateRequest {
    private String target;
    private String action;
    private String locationId;

    public static GoHighLevelRedirectUpdateRequest fromRequest(RedirectUpdateRequest request) {
        GoHighLevelRedirectUpdateRequest ghlRequest = new GoHighLevelRedirectUpdateRequest();
        ghlRequest.setTarget(request.getTarget());
        ghlRequest.setAction(request.getAction());
        ghlRequest.setLocationId(request.getLocation_id());
        return ghlRequest;
    }
}
