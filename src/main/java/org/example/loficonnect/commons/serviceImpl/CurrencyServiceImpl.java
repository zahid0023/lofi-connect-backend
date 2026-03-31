package org.example.loficonnect.commons.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.request.CurrencyUpdateRequest;
import org.example.loficonnect.commons.dto.response.CurrencyListResponse;
import org.example.loficonnect.commons.dto.response.CurrencyResponse;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.dto.CurrencyDto;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.mapper.CurrencyMapper;
import org.example.loficonnect.commons.repository.CurrencyRepository;
import org.example.loficonnect.commons.service.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public SuccessResponse createCurrency(CurrencyCreateRequest request) {
        CurrencyEntity currencyEntity = CurrencyMapper.fromRequest(request);
        currencyRepository.save(currencyEntity);
        return new SuccessResponse(true, currencyEntity.getId());
    }

    @Override
    public CurrencyEntity getCurrencyEntityById(Long id) {
        return currencyRepository.findByIdAndIsActiveAndIsDeleted(id, true, false)
                .orElseThrow(() -> new EntityNotFoundException("Currency Entity not found"));
    }

    @Override
    public CurrencyResponse getCurrencyById(Long id) {
        CurrencyEntity entity = getCurrencyEntityById(id);
        CurrencyDto currencyDto = CurrencyMapper.toDto(entity);
        return new CurrencyResponse(currencyDto);
    }

    @Override
    public CurrencyListResponse getAllCurrencies() {
        List<CurrencyEntity> allCurrencies = currencyRepository.findAllByIsActiveAndIsDeleted(true, false);
        List<CurrencyDto> currencyDtos = allCurrencies.stream()
                .map(CurrencyMapper::toDto)
                .toList();
        return new CurrencyListResponse(currencyDtos);
    }

    @Override
    @Transactional
    public SuccessResponse updateCurrency(Long id, CurrencyUpdateRequest request) {
        CurrencyEntity entity = getCurrencyEntityById(id);
        CurrencyMapper.update(request, entity);
        currencyRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }

    @Override
    @Transactional
    public SuccessResponse deleteCurrency(Long id) {
        CurrencyEntity entity = getCurrencyEntityById(id);
        entity.setIsActive(false);
        entity.setIsDeleted(true);
        currencyRepository.save(entity);
        return new SuccessResponse(true, entity.getId());
    }
}
