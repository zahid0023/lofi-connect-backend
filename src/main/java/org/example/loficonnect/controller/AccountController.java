package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.service.AccountService;
import org.example.loficonnect.util.MapUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @AppKey
    @GetMapping("/locations/{location-id}/accounts")
    public ResponseEntity<?> getAccounts(
        @PathVariable("location-id") String locationId
    ) {
        return ResponseEntity.ok(accountService.getAccounts(locationId));
    }

    @AppKey
    @DeleteMapping("/locations/{location-id}/accounts/{id}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable("location-id") String locationId,
            @PathVariable("id") String id,
            @RequestParam(value = "company-id", required = false) String companyId,
            @RequestParam(value = "user-id", required = false) String userId
    ) {
        Map<String, Object> queryParams = new HashMap<>();
        MapUtil.putIfNotNull(queryParams, "companyId", companyId);
        MapUtil.putIfNotNull(queryParams, "userId", userId);
        return ResponseEntity.ok(accountService.deleteAccount(locationId, id, queryParams));
    }

}
