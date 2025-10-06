package org.example.loficonnect.dto.mapper.oauthgoogle;

import lombok.Data;
import org.example.loficonnect.dto.request.oauthgoogle.OauthGoogleBusinessLocationRequest;

import java.util.Map;

@Data
public class GoHighLevelOauthGoogleBusinessLocationRequest {
    private Map<String, Object> location;
    private Map<String, Object> account;
    private String companyId;

    public static GoHighLevelOauthGoogleBusinessLocationRequest fromRequest(OauthGoogleBusinessLocationRequest request) {
        GoHighLevelOauthGoogleBusinessLocationRequest ghlRequest = new GoHighLevelOauthGoogleBusinessLocationRequest();
        ghlRequest.setLocation(request.getLocation());
        ghlRequest.setAccount(request.getAccount());
        ghlRequest.setCompanyId(request.getCompany_id());
        return ghlRequest;
    }
}
