package org.example.loficonnect.commons.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.request.country.CreateCountryRequest;
import org.example.loficonnect.commons.dto.request.country.UpdateCountryRequest;
import org.example.loficonnect.commons.model.entity.CountryEntity;
import org.example.loficonnect.commons.service.CountryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCountryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(countryService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(countryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(countryService.getAll(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCountryRequest request) {
        CountryEntity entity = countryService.getEntityById(id);
        return ResponseEntity.ok(countryService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        CountryEntity entity = countryService.getEntityById(id);
        return ResponseEntity.ok(countryService.delete(entity));
    }
}
