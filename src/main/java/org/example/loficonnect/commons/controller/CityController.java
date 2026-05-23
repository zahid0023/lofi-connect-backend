package org.example.loficonnect.commons.controller;

import jakarta.validation.Valid;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.request.city.CreateCityRequest;
import org.example.loficonnect.commons.dto.request.city.UpdateCityRequest;
import org.example.loficonnect.commons.model.entity.CityEntity;
import org.example.loficonnect.commons.model.entity.CountryEntity;
import org.example.loficonnect.commons.service.CityService;
import org.example.loficonnect.commons.service.CountryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/countries/{country-id}/cities")
public class CityController {

    private final CityService cityService;
    private final CountryService countryService;

    public CityController(CityService cityService,
                          CountryService countryService) {
        this.cityService = cityService;
        this.countryService = countryService;
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable("country-id") Long countryId,
                                    @Valid @RequestBody CreateCityRequest request) {
        CountryEntity countryEntity = countryService.getEntityById(countryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.create(request, countryEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("country-id") Long countryId,
                                     @PathVariable Long id) {
        return ResponseEntity.ok(cityService.getById(countryId, id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable("country-id") Long countryId,
                                    @Valid @ParameterObject PaginatedRequest request) {
        return ResponseEntity.ok(cityService.getAll(countryId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("country-id") Long countryId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateCityRequest request) {
        CityEntity entity = cityService.getEntityById(countryId, id);
        return ResponseEntity.ok(cityService.update(entity, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("country-id") Long countryId,
                                    @PathVariable Long id) {
        CityEntity entity = cityService.getEntityById(countryId, id);
        return ResponseEntity.ok(cityService.delete(entity));
    }
}
