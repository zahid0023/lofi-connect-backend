package org.example.loficonnect.dto.mapper.oauthtiktok;

import lombok.Data;
import org.example.loficonnect.dto.request.oauthtiktok.OauthtiktokAttachRequest;

@Data
public class GoHighLevelOauthtiktokAttachRequest {
    private String type;
    private String originId;
    private String name;
    private String avatar;
    private Boolean verified;
    private String username;
    private String companyId;

    public static GoHighLevelOauthtiktokAttachRequest fromRequest(OauthtiktokAttachRequest request) {
        GoHighLevelOauthtiktokAttachRequest ghl = new GoHighLevelOauthtiktokAttachRequest();
        ghl.setType(request.getType());
        ghl.setOriginId(request.getOriginId());
        ghl.setName(request.getName());
        ghl.setAvatar(request.getAvatar());
        ghl.setVerified(request.getVerified());
        ghl.setUsername(request.getUsername());
        ghl.setCompanyId(request.getCompanyId());
        return ghl;
    }
}
