package org.example.loficonnect.dto.mapper.oauthfacebook;

import lombok.Data;
import org.example.loficonnect.dto.request.oauthfacebook.AttachFacebookPageRequest;

@Data
public class GoHighLevelAttachFacebookPageRequest {
    private String type;
    private String originId;
    private String name;
    private String avatar;
    private String companyId;

    public static GoHighLevelAttachFacebookPageRequest fromRequest(AttachFacebookPageRequest request) {
        GoHighLevelAttachFacebookPageRequest ghl = new GoHighLevelAttachFacebookPageRequest();
        ghl.setType(request.getType());
        ghl.setOriginId(request.getOrigin_id());
        ghl.setName(request.getName());
        ghl.setAvatar(request.getAvatar());
        ghl.setCompanyId(request.getCompany_id());
        return ghl;
    }
}
