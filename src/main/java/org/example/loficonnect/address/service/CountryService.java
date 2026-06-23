package org.example.loficonnect.address.service;

import org.example.loficonnect.address.dto.request.country.CreateCountryRequest;
import org.example.loficonnect.address.dto.request.country.UpdateCountryRequest;
import org.example.loficonnect.address.dto.response.CountryResponse;
import org.example.loficonnect.address.model.entity.CountryEntity;
import org.example.loficonnect.address.model.projection.CountrySummary;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;

import java.util.List;
import java.util.Set;

public interface CountryService {
    SuccessResponse create(CreateCountryRequest request);

    CountryEntity getEntityById(Long id);

    CountryResponse getById(Long id);

    PaginatedResponse<CountrySummary> getAll(PaginatedRequest request);

    SuccessResponse update(CountryEntity entity, UpdateCountryRequest request);

    SuccessResponse delete(Long id);

    List<CountryEntity> getAll(Set<Long> ids);
}
