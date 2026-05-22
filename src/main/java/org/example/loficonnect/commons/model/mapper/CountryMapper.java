package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.commons.dto.request.country.CountryRequest;
import org.example.loficonnect.commons.dto.request.country.CreateCountryRequest;
import org.example.loficonnect.commons.dto.request.country.UpdateCountryRequest;
import org.example.loficonnect.commons.model.dto.CountryDto;
import org.example.loficonnect.commons.model.entity.CountryEntity;

@UtilityClass
public class CountryMapper {

    public CountryEntity create(CreateCountryRequest request) {
        CountryEntity entity = new CountryEntity();
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(CountryEntity entity,
                       UpdateCountryRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(CountryEntity entity,
                                   CountryRequest request) {
        entity.setIso3Code(request.getIso3Code());
        entity.setPhoneCode(request.getPhoneCode());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
    }

    public CountryDto toDto(CountryEntity entity) {

        return CountryDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .iso3Code(entity.getIso3Code())
                .phoneCode(entity.getPhoneCode())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
