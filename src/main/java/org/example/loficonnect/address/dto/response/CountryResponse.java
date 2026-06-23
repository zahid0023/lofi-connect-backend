package org.example.loficonnect.address.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.address.model.dto.CountryDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CountryResponse {
    private final CountryDto country;

    public CountryResponse(CountryDto country) {
        this.country = country;
    }
}
