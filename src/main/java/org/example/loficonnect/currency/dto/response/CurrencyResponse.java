package org.example.loficonnect.currency.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.currency.model.dto.CurrencyDto;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrencyResponse {
    private final CurrencyDto currency;

    public CurrencyResponse(CurrencyDto currency) {
        this.currency = currency;
    }
}
