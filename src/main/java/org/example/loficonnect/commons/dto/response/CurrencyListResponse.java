package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.example.loficonnect.commons.model.dto.CurrencyDto;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CurrencyListResponse {
    private List<CurrencyDto> currencies;

    public CurrencyListResponse(List<CurrencyDto> currencies) {
        this.currencies = currencies;
    }
}
