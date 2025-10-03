package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface TransactionService {
    JsonNode getTransactions(Map<String, Object> queryParams);
    JsonNode getTransactionById(String transactionId, Map<String, Object> queryParams);
}
