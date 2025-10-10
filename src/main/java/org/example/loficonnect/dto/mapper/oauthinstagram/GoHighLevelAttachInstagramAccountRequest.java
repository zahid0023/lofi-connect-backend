package org.example.loficonnect.dto.mapper.oauthinstagram;

import lombok.Data;
import org.example.loficonnect.dto.request.oauthinstagram.AttachInstagramAccountRequest;

@Data
public class GoHighLevelAttachInstagramAccountRequest {
    private String originId;
    private String name;
    private String avatar;
    private String pageId;
    private String companyId;

    public static GoHighLevelAttachInstagramAccountRequest fromRequest(AttachInstagramAccountRequest request) {
        GoHighLevelAttachInstagramAccountRequest ghl = new GoHighLevelAttachInstagramAccountRequest();
        ghl.setOriginId(request.getOrigin_id());
        ghl.setName(request.getName());
        ghl.setAvatar(request.getAvatar());
        ghl.setPageId(request.getPage_id());
        ghl.setCompanyId(request.getCompany_id());
        return ghl;
    }
}
