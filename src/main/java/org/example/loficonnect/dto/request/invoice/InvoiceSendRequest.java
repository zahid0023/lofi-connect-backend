package org.example.loficonnect.dto.request.invoice;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceSendRequest {
    private String altId;
    private String altType;
    private String userId;
    private String action;
    private Boolean liveMode;
    private SentFrom sentFrom;
    private AutoPayment autoPayment;

    @Data
    public static class SentFrom {
        private String fromName;
        private String fromEmail;
    }

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
