package org.example.loficonnect.commons.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.request.CurrencyUpdateRequest;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.service.CurrencyService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CurrencyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(currencyService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(currencyService.getAll(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody CurrencyUpdateRequest request) {
        CurrencyEntity entity = currencyService.getEntityById(id);
        return ResponseEntity.ok(currencyService.update(entity, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        CurrencyEntity entity = currencyService.getEntityById(id);
        return ResponseEntity.ok(currencyService.delete(entity));
    }
}
