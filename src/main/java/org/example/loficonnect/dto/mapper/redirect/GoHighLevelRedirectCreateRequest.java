package org.example.loficonnect.dto.mapper.redirect;

import lombok.Data;
import org.example.loficonnect.dto.request.redirect.RedirectCreateRequest;

@Data
public class GoHighLevelRedirectCreateRequest {
    private String locationId;
    private String domain;
    private String path;
    private String target;
    private String action;

    public static GoHighLevelRedirectCreateRequest fromRequest(RedirectCreateRequest request) {
        GoHighLevelRedirectCreateRequest ghlRequest = new GoHighLevelRedirectCreateRequest();
        ghlRequest.setLocationId(request.getLocation_id());
        ghlRequest.setDomain(request.getDomain());
        ghlRequest.setPath(request.getPath());
        ghlRequest.setTarget(request.getTarget());
        ghlRequest.setAction(request.getAction());
        return ghlRequest;
    }
}
