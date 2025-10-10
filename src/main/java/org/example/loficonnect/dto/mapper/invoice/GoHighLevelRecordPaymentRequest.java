package org.example.loficonnect.dto.mapper.invoice;

import lombok.Data;
import org.example.loficonnect.dto.request.invoice.RecordPaymentRequest;

import java.util.List;
import java.util.Map;

@Data
public class GoHighLevelRecordPaymentRequest {
    private String altId;
    private String altType;
    private String mode;
    private Card card;
    private Cheque cheque;
    private String notes;
    private Double amount;
    private Map<String, Object> meta;
    private List<String> paymentScheduleIds;

    public static GoHighLevelRecordPaymentRequest fromRequest(RecordPaymentRequest request) {
        GoHighLevelRecordPaymentRequest ghlRequest = new GoHighLevelRecordPaymentRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());
        ghlRequest.setMode(request.getMode());
        
        if (request.getCard() != null) {
            Card card = new Card();
            card.setBrand(request.getCard().getBrand());
            card.setLast4(request.getCard().getLast4());
            ghlRequest.setCard(card);
        }

        if (request.getCheque() != null) {
            Cheque cheque = new Cheque();
            cheque.setNumber(request.getCheque().getNumber());
            ghlRequest.setCheque(cheque);
        }

        ghlRequest.setNotes(request.getNotes());
        ghlRequest.setAmount(request.getAmount());
        ghlRequest.setMeta(request.getMeta());
        ghlRequest.setPaymentScheduleIds(request.getPaymentScheduleIds());

        return ghlRequest;
    }

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
