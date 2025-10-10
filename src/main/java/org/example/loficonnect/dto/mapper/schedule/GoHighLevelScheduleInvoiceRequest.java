package org.example.loficonnect.dto.mapper.schedule;

import lombok.Data;
import org.example.loficonnect.dto.request.schedule.ScheduleInvoiceRequest;

@Data
public class GoHighLevelScheduleInvoiceRequest {
    private String altId;
    private String altType;
    private Boolean liveMode;
    private AutoPayment autoPayment;

    public static GoHighLevelScheduleInvoiceRequest fromRequest(ScheduleInvoiceRequest request) {
        GoHighLevelScheduleInvoiceRequest ghlRequest = new GoHighLevelScheduleInvoiceRequest();
        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setLiveMode(request.getLiveMode());

        AutoPayment autoPayment = new AutoPayment();
        ScheduleInvoiceRequest.AutoPayment requestAutoPayment = request.getAutoPayment();

        if (requestAutoPayment != null) {
            autoPayment.setEnable(requestAutoPayment.getEnable());
            autoPayment.setType(requestAutoPayment.getType());
            autoPayment.setPaymentMethodId(requestAutoPayment.getPaymentMethodId());
            autoPayment.setCustomerId(requestAutoPayment.getCustomerId());

            if (requestAutoPayment.getCard() != null) {
                Card card = new Card();
                card.setBrand(requestAutoPayment.getCard().getBrand());
                card.setLast4(requestAutoPayment.getCard().getLast4());
                autoPayment.setCard(card);
            }

            if (requestAutoPayment.getUsBankAccount() != null) {
                UsBankAccount usBankAccount = new UsBankAccount();
                usBankAccount.setBankName(requestAutoPayment.getUsBankAccount().getBankName());
                usBankAccount.setLast4(requestAutoPayment.getUsBankAccount().getLast4());
                autoPayment.setUsBankAccount(usBankAccount);
            }

            if (requestAutoPayment.getSepaDirectDebit() != null) {
                SepaDirectDebit sepaDirectDebit = new SepaDirectDebit();
                sepaDirectDebit.setBankCode(requestAutoPayment.getSepaDirectDebit().getBankCode());
                sepaDirectDebit.setLast4(requestAutoPayment.getSepaDirectDebit().getLast4());
                sepaDirectDebit.setBranchCode(requestAutoPayment.getSepaDirectDebit().getBranchCode());
                autoPayment.setSepaDirectDebit(sepaDirectDebit);
            }

            if (requestAutoPayment.getBacsDirectDebit() != null) {
                BacsDirectDebit bacsDirectDebit = new BacsDirectDebit();
                bacsDirectDebit.setSortCode(requestAutoPayment.getBacsDirectDebit().getSortCode());
                bacsDirectDebit.setLast4(requestAutoPayment.getBacsDirectDebit().getLast4());
                autoPayment.setBacsDirectDebit(bacsDirectDebit);
            }

            if (requestAutoPayment.getBecsDirectDebit() != null) {
                BecsDirectDebit becsDirectDebit = new BecsDirectDebit();
                becsDirectDebit.setBsbNumber(requestAutoPayment.getBecsDirectDebit().getBsbNumber());
                becsDirectDebit.setLast4(requestAutoPayment.getBecsDirectDebit().getLast4());
                autoPayment.setBecsDirectDebit(becsDirectDebit);
            }

            autoPayment.setCardId(requestAutoPayment.getCardId());
        }

        ghlRequest.setAutoPayment(autoPayment);
        return ghlRequest;
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
