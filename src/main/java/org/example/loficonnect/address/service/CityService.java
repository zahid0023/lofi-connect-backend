package org.example.loficonnect.address.service;

import org.example.loficonnect.address.dto.request.city.CreateCityRequest;
import org.example.loficonnect.address.dto.request.city.UpdateCityRequest;
import org.example.loficonnect.address.dto.response.CityResponse;
import org.example.loficonnect.address.model.entity.CityEntity;
import org.example.loficonnect.address.model.entity.CountryEntity;
import org.example.loficonnect.address.model.projection.CitySummary;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;

public interface CityService {
    SuccessResponse create(CreateCityRequest request, CountryEntity countryEntity);

    CityEntity getEntityById(Long countryId, Long id);

    CityResponse getById(Long countryId, Long id);

    PaginatedResponse<CitySummary> getAll(Long countryId, PaginatedRequest request);

    SuccessResponse update(CityEntity entity, UpdateCityRequest request);

    SuccessResponse delete(CityEntity entity);
}
