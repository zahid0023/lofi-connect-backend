package org.example.loficonnect.commons.dto.response.cities;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.commons.model.dto.CityDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CityResponse {
    private final CityDto city;

    public CityResponse(CityDto city) {
        this.city = city;
    }
}
