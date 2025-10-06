package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.AccountClient;
import org.example.loficonnect.service.AccountService;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountClient accountClient;
    private final AuthorizationService authorizationService;

    public AccountServiceImpl(AccountClient accountClient, AuthorizationService authorizationService) {
        this.accountClient = accountClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getAccounts(String locationId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return accountClient.getAccounts(accessKey, version, locationId);
    }

    @Override
    public JsonNode deleteAccount(String locationId, String id, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return accountClient.deleteAccount(accessKey, version, locationId, id, queryParams);
    }

}
