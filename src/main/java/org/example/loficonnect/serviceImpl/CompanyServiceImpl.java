package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.CompanyClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.CompanyService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {
    private final AuthorizationService authorizationService;
    private final CompanyClient companyClient;

    public CompanyServiceImpl(AuthorizationService authorizationService, CompanyClient companyClient) {
        this.authorizationService = authorizationService;
        this.companyClient = companyClient;
    }

    @Override
    public JsonNode getCompany(String companyId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return companyClient.getCompany(accessKey, version, companyId);
    }
}
