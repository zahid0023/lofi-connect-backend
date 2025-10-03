package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.walletcharges.WalletChargeCreateRequest;

import java.util.Map;

public interface WalletChargesService {
    JsonNode createWalletCharge(WalletChargeCreateRequest request);
    JsonNode getWalletCharges(Map<String, Object> queryParams);
    JsonNode deleteWalletCharge(String chargeId);
    JsonNode getWalletCharge(String chargeId);
    JsonNode hasSufficientFunds();
}
