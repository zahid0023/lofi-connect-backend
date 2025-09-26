package org.example.loficonnect.dto.mapper.estimate;

import lombok.Data;
import org.example.loficonnect.dto.request.estimate.EstimateSendRequest;

@Data
public class GoHighLevelEstimateSendRequest {
    private String altId;
    private String altType;
    private String action;
    private Boolean liveMode;
    private String userId;
    private SentFrom sentFrom;
    private String estimateName;

    public static GoHighLevelEstimateSendRequest fromRequest(EstimateSendRequest request) {
        GoHighLevelEstimateSendRequest ghlRequest = new GoHighLevelEstimateSendRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setAction(request.getAction());
        ghlRequest.setLiveMode(request.getLiveMode());
        ghlRequest.setUserId(request.getUserId());

        SentFrom sentFrom = new SentFrom();
        sentFrom.setFromName(request.getSentFrom().getFromName());
        sentFrom.setFromEmail(request.getSentFrom().getFromEmail());
        ghlRequest.setSentFrom(sentFrom);

        ghlRequest.setEstimateName(request.getEstimateName());

        return ghlRequest;
    }

    @Data
    public static class SentFrom {
        private String fromName;
        private String fromEmail;
    }
}
