package org.example.loficonnect.dto.request.schedule;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleAutoPaymentRequest {

    private String altId;
    private String altType;
    private String id;
    private AutoPayment autoPayment;

    @Data
    public static class AutoPayment {
        private Boolean enable;
        private String type;
        private String paymentMethodId;
        private String customerId;
        private Card card;
        private UsBankAccount usBankAccount;
        private SepaDirectDebit sepaDirectDebit;
        private BacsDirectDebit bacsDirectDebit;
        private BecsDirectDebit becsDirectDebit;
        private String cardId;
    }

    @Data
    public static class Card {
        private String brand;
        private String last4;
    }

    @Data
    public static class UsBankAccount {
        private String bankName;
        private String last4;
    }

    @Data
    public static class SepaDirectDebit {
        private String bankCode;
        private String last4;
        private String branchCode;
    }

    @Data
    public static class BacsDirectDebit {
        private String sortCode;
        private String last4;
    }

    @Data
    public static class BecsDirectDebit {
        private String bsbNumber;
        private String last4;
    }
}
