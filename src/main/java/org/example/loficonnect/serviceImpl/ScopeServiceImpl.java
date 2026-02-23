package org.example.loficonnect.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.loficonnect.dto.request.scope.CreateScopeRequest;
import org.example.loficonnect.model.mapper.ScopeMapper;
import org.example.loficonnect.repository.ScopeRepository;
import org.example.loficonnect.service.ScopeService;
import org.example.loficonnect.commons.dto.response.SuccessResponse;
import org.example.loficonnect.model.entity.ScopeEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ScopeServiceImpl implements ScopeService {
    private final ScopeRepository scopeRepository;

    public ScopeServiceImpl(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    @Override
    public SuccessResponse createScope(CreateScopeRequest request) {
        ScopeEntity scopeEntity = ScopeMapper.fromRequest(request);
        scopeEntity = scopeRepository.save(scopeEntity);
        return new SuccessResponse(true, scopeEntity.getId());
    }

    @Override
    public List<ScopeEntity> getAllScopes() {
        return List.of();
    }

    @Override
    public List<String> getAllScopesNames() {
        return scopeRepository.findAllNames();
    }
}
