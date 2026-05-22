package org.example.loficonnect.commons.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.PaginatedRequest;
import org.example.loficonnect.commons.dto.request.country.CreateCountryRequest;
import org.example.loficonnect.commons.dto.request.country.UpdateCountryRequest;
import org.example.loficonnect.commons.dto.response.PaginatedResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.dto.response.countries.CountryResponse;
import org.example.loficonnect.commons.model.dto.CountryDto;
import org.example.loficonnect.commons.model.entity.CountryEntity;
import org.example.loficonnect.commons.model.enums.CountrySortField;
import org.example.loficonnect.commons.model.mapper.CountryMapper;
import org.example.loficonnect.commons.model.projection.CountrySummary;
import org.example.loficonnect.commons.repository.CountryRepository;
import org.example.loficonnect.commons.service.CountryService;
import org.example.loficonnect.util.Pagination;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
public class CountryServiceImpl implements CountryService {

    private static final Set<String> ALLOWED_SORT_FIELDS = CountrySortField.allowedFields();

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional
    @Override
    public SuccessResponse create(CreateCountryRequest request) {
        CountryEntity entity = CountryMapper.create(request);
        countryRepository.save(entity);
        log.info("Country created with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CountryResponse getById(Long id) {
        CountryEntity entity = getEntityById(id);
        CountryDto dto = CountryMapper.toDto(entity);
        return new CountryResponse(dto);
    }

    @Override
    public PaginatedResponse<CountrySummary> getAll(PaginatedRequest request) {
        Page<@NonNull CountrySummary> page = countryRepository.findAllByIsActiveAndIsDeleted(
                true, false, request.toPageable(ALLOWED_SORT_FIELDS)
        );
        return Pagination.buildPaginatedResponse(page);
    }

    @Transactional
    @Override
    public SuccessResponse update(CountryEntity entity, UpdateCountryRequest request) {
        CountryMapper.update(entity, request);
        countryRepository.save(entity);
        log.info("Country updated with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Transactional
    @Override
    public SuccessResponse delete(CountryEntity entity) {
        entity.setIsDeleted(true);
        entity.setIsActive(false);
        countryRepository.save(entity);
        log.info("Country soft-deleted with id: {}", entity.getId());
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    public CountryEntity getEntityById(Long id) {
        return countryRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + id));
    }
}
