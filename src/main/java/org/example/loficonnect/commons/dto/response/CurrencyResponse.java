package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.commons.model.dto.CurrencyDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrencyResponse {
    private CurrencyDto data;

    public CurrencyResponse(CurrencyDto currency) {
        this.data = currency;
    }
}
