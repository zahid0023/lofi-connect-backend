package org.example.loficonnect.dto.mapper.oauthlinkedin;

import lombok.Data;
import org.example.loficonnect.dto.request.oauthlinkedin.AttachLinkedinAccountRequest;

@Data
public class GhlAttachLinkedinAccountRequest {
    private String type;
    private String originId;
    private String name;
    private String avatar;
    private String urn;
    private String companyId;

    public static GhlAttachLinkedinAccountRequest fromRequest(AttachLinkedinAccountRequest req) {
        GhlAttachLinkedinAccountRequest ghl = new GhlAttachLinkedinAccountRequest();
        ghl.setType(req.getType());
        ghl.setOriginId(req.getOriginId());
        ghl.setName(req.getName());
        ghl.setAvatar(req.getAvatar());
        ghl.setUrn(req.getUrn());
        ghl.setCompanyId(req.getCompanyId());
        return ghl;
    }
}
