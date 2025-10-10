package org.example.loficonnect.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.mapper.walletcharges.GoHighLevelWalletChargeCreateRequest;
import org.example.loficonnect.dto.request.walletcharges.WalletChargeCreateRequest;
import org.example.loficonnect.feignclients.WalletChargesClient;
import org.example.loficonnect.service.AuthorizationService;
import org.example.loficonnect.service.WalletChargesService;
import org.example.loficonnect.util.AppKeyContext;
import org.example.loficonnect.util.VersionContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class WalletChargesServiceImpl implements WalletChargesService {

    private final WalletChargesClient walletChargesClient;
    private final AuthorizationService authorizationService;

    public WalletChargesServiceImpl(WalletChargesClient walletChargesClient, AuthorizationService authorizationService) {
        this.walletChargesClient = walletChargesClient;
        this.authorizationService = authorizationService;
    }

    @Override
    public JsonNode createWalletCharge(WalletChargeCreateRequest request) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        GoHighLevelWalletChargeCreateRequest ghlReq = GoHighLevelWalletChargeCreateRequest.fromRequest(request);
        return walletChargesClient.createWalletCharge(accessKey, version, ghlReq);
    }

    @Override
    public JsonNode getWalletCharges(Map<String, Object> queryParams) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return walletChargesClient.getWalletCharges(accessKey, version, queryParams);
    }

    @Override
    public JsonNode deleteWalletCharge(String chargeId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return walletChargesClient.deleteWalletCharge(accessKey, version, chargeId);
    }

    @Override
    public JsonNode getWalletCharge(String chargeId) {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return walletChargesClient.getWalletCharge(accessKey, version, chargeId);
    }

    @Override
    public JsonNode hasSufficientFunds() {
        String accessKey = authorizationService.getAccessToken(AppKeyContext.getAppKey());
        String version = VersionContext.getVersion();
        return walletChargesClient.hasSufficientFunds(accessKey, version);
    }

}
