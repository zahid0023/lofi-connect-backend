package org.example.loficonnect.dto.mapper.invoice;

import lombok.Data;
import org.example.loficonnect.dto.request.invoice.InvoiceSendRequest;

@Data
public class GoHighLevelInvoiceSendRequest {
    private String altId;
    private String altType;
    private String userId;
    private String action;
    private Boolean liveMode;
    private SentFrom sentFrom;
    private AutoPayment autoPayment;

    public static GoHighLevelInvoiceSendRequest fromRequest(InvoiceSendRequest request) {
        GoHighLevelInvoiceSendRequest ghlRequest = new GoHighLevelInvoiceSendRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setUserId(request.getUserId());
        ghlRequest.setAction(request.getAction());
        ghlRequest.setLiveMode(request.getLiveMode());

        if (request.getSentFrom() != null) {
            SentFrom sentFrom = new SentFrom();
            sentFrom.setFromName(request.getSentFrom().getFromName());
            sentFrom.setFromEmail(request.getSentFrom().getFromEmail());
            ghlRequest.setSentFrom(sentFrom);
        }

        if (request.getAutoPayment() != null) {
            AutoPayment autoPayment = new AutoPayment();
            autoPayment.setEnable(request.getAutoPayment().getEnable());
            autoPayment.setType(request.getAutoPayment().getType());
            autoPayment.setPaymentMethodId(request.getAutoPayment().getPaymentMethodId());
            autoPayment.setCustomerId(request.getAutoPayment().getCustomerId());

            if (request.getAutoPayment().getCard() != null) {
                AutoPayment.Card card = new AutoPayment.Card();
                card.setBrand(request.getAutoPayment().getCard().getBrand());
                card.setLast4(request.getAutoPayment().getCard().getLast4());
                autoPayment.setCard(card);
            }

            if (request.getAutoPayment().getUsBankAccount() != null) {
                AutoPayment.UsBankAccount usBankAccount = new AutoPayment.UsBankAccount();
                usBankAccount.setBankName(request.getAutoPayment().getUsBankAccount().getBankName());
                usBankAccount.setLast4(request.getAutoPayment().getUsBankAccount().getLast4());
                autoPayment.setUsBankAccount(usBankAccount);
            }

            if (request.getAutoPayment().getSepaDirectDebit() != null) {
                AutoPayment.SepaDirectDebit sepaDirectDebit = new AutoPayment.SepaDirectDebit();
                sepaDirectDebit.setBankCode(request.getAutoPayment().getSepaDirectDebit().getBankCode());
                sepaDirectDebit.setLast4(request.getAutoPayment().getSepaDirectDebit().getLast4());
                sepaDirectDebit.setBranchCode(request.getAutoPayment().getSepaDirectDebit().getBranchCode());
                autoPayment.setSepaDirectDebit(sepaDirectDebit);
            }

            if (request.getAutoPayment().getBacsDirectDebit() != null) {
                AutoPayment.BacsDirectDebit bacsDirectDebit = new AutoPayment.BacsDirectDebit();
                bacsDirectDebit.setSortCode(request.getAutoPayment().getBacsDirectDebit().getSortCode());
                bacsDirectDebit.setLast4(request.getAutoPayment().getBacsDirectDebit().getLast4());
                autoPayment.setBacsDirectDebit(bacsDirectDebit);
            }

            if (request.getAutoPayment().getBecsDirectDebit() != null) {
                AutoPayment.BecsDirectDebit becsDirectDebit = new AutoPayment.BecsDirectDebit();
                becsDirectDebit.setBsbNumber(request.getAutoPayment().getBecsDirectDebit().getBsbNumber());
                becsDirectDebit.setLast4(request.getAutoPayment().getBecsDirectDebit().getLast4());
                autoPayment.setBecsDirectDebit(becsDirectDebit);
            }

            autoPayment.setCardId(request.getAutoPayment().getCardId());

            ghlRequest.setAutoPayment(autoPayment);
        }

        return ghlRequest;
    }


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
}
