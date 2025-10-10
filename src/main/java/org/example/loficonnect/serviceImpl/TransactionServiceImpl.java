package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.feignclients.TransactionClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.TransactionService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionClient transactionClient;
    private final AuthorizationService authorizationService;

    public TransactionServiceImpl(TransactionClient transactionClient, AuthorizationService authorizationService) {
        this.transactionClient = transactionClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode getTransactions(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return transactionClient.getTransactions(accessKey, version, queryParams);
    }

    @Override
    public JsonNode getTransactionById(String transactionId, Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return transactionClient.getTransactionById(accessKey, version, transactionId, queryParams);
    }

}
