package org.example.loficonnect.commons.model.mapper;

import lombok.experimental.UtilityClass;
import org.example.loficonnect.commons.dto.request.city.CityRequest;
import org.example.loficonnect.commons.dto.request.city.CreateCityRequest;
import org.example.loficonnect.commons.dto.request.city.UpdateCityRequest;
import org.example.loficonnect.commons.model.dto.CityDto;
import org.example.loficonnect.commons.model.entity.CityEntity;
import org.example.loficonnect.commons.model.entity.CountryEntity;

@UtilityClass
public class CityMapper {

    public CityEntity create(CreateCityRequest request, CountryEntity countryEntity) {
        CityEntity entity = new CityEntity();
        entity.setCountryEntity(countryEntity);
        entity.setCode(request.getCode());
        applyCommonFields(entity, request);
        return entity;
    }

    public void update(CityEntity entity, UpdateCityRequest request) {
        applyCommonFields(entity, request);
    }

    private void applyCommonFields(CityEntity entity, CityRequest request) {

        entity.setName(request.getName());
        entity.setDescription(request.getDescription() != null ? request.getDescription() : "");
        entity.setSortOrder(request.getSortOrder());
    }

    public CityDto toDto(CityEntity entity) {
        return CityDto.builder()
                .id(entity.getId())
                .countryId(entity.getCountryEntity().getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
