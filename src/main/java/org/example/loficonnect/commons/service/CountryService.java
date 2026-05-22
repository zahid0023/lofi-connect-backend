package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.request.country.CreateCountryRequest;
import org.example.loficonnect.commons.dto.request.country.UpdateCountryRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.dto.response.countries.CountryResponse;
import org.example.loficonnect.commons.model.entity.CountryEntity;
import org.example.loficonnect.commons.model.projection.CountrySummary;

public interface CountryService {
    SuccessResponse create(CreateCountryRequest request);

    CountryEntity getEntityById(Long id);

    CountryResponse getById(Long id);

    PaginatedResponse<CountrySummary> getAll(PaginatedRequest request);

    SuccessResponse update(CountryEntity entity,
                           UpdateCountryRequest request);

    SuccessResponse delete(CountryEntity entity);
}
