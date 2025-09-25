package org.example.loficonnect.dto.mapper.invoicetemplate;

import lombok.Data;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplatePaymentMethodsConfigurationUpdateRequest;

@Data
public class GoHighLevelPaymentMethodsConfigurationUpdateRequest {
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

    public static GoHighLevelPaymentMethodsConfigurationUpdateRequest fromRequest(InvoiceTemplatePaymentMethodsConfigurationUpdateRequest request) {
        GoHighLevelPaymentMethodsConfigurationUpdateRequest ghlRequest = new GoHighLevelPaymentMethodsConfigurationUpdateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());

        PaymentMethods ghlPaymentMethods = new PaymentMethods();
        InvoiceTemplatePaymentMethodsConfigurationUpdateRequest.PaymentMethods userPaymentMethods = request.getPaymentMethods();

        PaymentMethods.Stripe ghlStripe = new PaymentMethods.Stripe();
        ghlStripe.setEnableBankDebitOnly(userPaymentMethods.getStripe().getEnableBankDebitOnly());
        ghlPaymentMethods.setStripe(ghlStripe);

        ghlRequest.setPaymentMethods(ghlPaymentMethods);

        return ghlRequest;
    }
}

