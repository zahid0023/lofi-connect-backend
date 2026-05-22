package org.example.loficonnect.commons.dto.response.countries;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.commons.model.dto.CountryDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CountryResponse {
    private final CountryDto country;

    public CountryResponse(CountryDto country) {
        this.country = country;
    }
}
