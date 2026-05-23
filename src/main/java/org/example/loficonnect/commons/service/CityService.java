package org.example.loficonnect.commons.service;

import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.request.city.CreateCityRequest;
import org.example.loficonnect.commons.dto.request.city.UpdateCityRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.dto.response.cities.CityResponse;
import org.example.loficonnect.commons.model.entity.CityEntity;
import org.example.loficonnect.commons.model.entity.CountryEntity;
import org.example.loficonnect.commons.model.projection.CitySummary;

public interface CityService {
    SuccessResponse create(CreateCityRequest request,
                           CountryEntity countryEntity);

    CityEntity getEntityById(Long countryId, Long id);

    CityResponse getById(Long countryId, Long id);

    PaginatedResponse<CitySummary> getAll(Long countryId,
                                          PaginatedRequest request);

    SuccessResponse update(CityEntity entity,
                           UpdateCityRequest request);

    SuccessResponse delete(CityEntity entity);
}
