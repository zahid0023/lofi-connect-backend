package org.example.loficonnect.commons.controller;

import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.request.CurrencyUpdateRequest;
import org.example.loficonnect.commons.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admins/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> createCurrency(@RequestBody CurrencyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(currencyService.createCurrency(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<?> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCurrencyById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCurrency(@PathVariable Long id, @RequestBody CurrencyUpdateRequest request) {
        return ResponseEntity.ok(currencyService.updateCurrency(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurrency(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.deleteCurrency(id));
    }
}
