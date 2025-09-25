package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.text2pay.GoHighLevelText2PayCreateRequest;
import org.example.loficonnect.dto.request.text2pay.Text2PayCreateRequest;
import org.example.loficonnect.feignclients.Text2PayClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.Text2PayService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Text2PayServiceImpl implements Text2PayService {

    private final AuthorizationService authorizationService;
    private final Text2PayClient text2PayClient;

    public Text2PayServiceImpl(AuthorizationService authorizationService, Text2PayClient text2PayClient) {
        this.authorizationService = authorizationService;
        this.text2PayClient = text2PayClient;
    }

    @Override
    public JsonNode createText2PayInvoice(Text2PayCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelText2PayCreateRequest ghlRequest = GoHighLevelText2PayCreateRequest.fromRequest(request);
        return text2PayClient.createText2PayInvoice(accessKey, version, ghlRequest);
    }

}
