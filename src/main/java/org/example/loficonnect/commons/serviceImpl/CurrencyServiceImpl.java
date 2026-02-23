package org.example.loficonnect.commons.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.commons.dto.request.CurrencyCreateRequest;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.commons.model.entity.CurrencyEntity;
import org.example.loficonnect.commons.model.mapper.CurrencyMapper;
import org.example.loficonnect.commons.repository.CurrencyRepository;
import org.example.loficonnect.commons.service.CurrencyService;
import org.springframework.stereotype.Service;

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
}
