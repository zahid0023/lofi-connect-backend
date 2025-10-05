package org.example.loficonnect.dto.mapper.emailverification;

import lombok.Data;
import org.example.loficonnect.dto.request.emailverification.EmailVerificationRequest;

@Data
public class GoHighLevelEmailVerificationRequest {
    private String type;
    private String verify;

    public static GoHighLevelEmailVerificationRequest fromRequest(EmailVerificationRequest request) {
        GoHighLevelEmailVerificationRequest ghlRequest = new GoHighLevelEmailVerificationRequest();
        ghlRequest.setType(request.getType());
        ghlRequest.setVerify(request.getVerify());
        return ghlRequest;
    }
}
