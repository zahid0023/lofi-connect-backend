package org.example.loficonnect.commons.controller;

import org.example.loficonnect.commons.dto.request.SubscriptionModelCreateRequest;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.service.CurrencyService;
import org.example.loficonnect.commons.service.SubscriptionModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admins/subscription/models")
public class SubscriptionModelController {
    private final SubscriptionModelService subscriptionModelService;
    private final CurrencyService currencyService;

    public SubscriptionModelController(SubscriptionModelService subscriptionModelService,
                                       CurrencyService currencyService) {
        this.subscriptionModelService = subscriptionModelService;
        this.currencyService = currencyService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createSubscriptionModel(@RequestBody SubscriptionModelCreateRequest request) {
        CurrencyEntity currencyEntity = currencyService.getCurrencyEntityById(request.getCurrencyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionModelService.createSubscriptionModel(request, currencyEntity));
    }
}
