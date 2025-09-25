package org.example.loficonnect.dto.request.invoice;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecordPaymentRequest {
    private String altId;
    private String altType;
    private String mode;
    private Card card;
    private Cheque cheque;
    private String notes;
    private Double amount;
    private Map<String, Object> meta;
    private List<String> paymentScheduleIds;

    @Data
    public static class Card {
        private String brand;
        private String last4;
    }

    @Data
    public static class Cheque {
        private String number;
    }
}
