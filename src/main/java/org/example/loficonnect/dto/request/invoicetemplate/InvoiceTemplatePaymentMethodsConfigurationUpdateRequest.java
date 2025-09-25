package org.example.loficonnect.dto.request.invoicetemplate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceTemplatePaymentMethodsConfigurationUpdateRequest {
    private String altId;
    private String altType;
    private PaymentMethods paymentMethods;

    @Data
    public static class PaymentMethods {
        private Stripe stripe;

        @Data
        public static class Stripe {
            private Boolean enableBankDebitOnly;
        }
    }
}
