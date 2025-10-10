package org.example.loficonnect.dto.mapper.invoice;

import lombok.Data;
import org.example.loficonnect.dto.request.invoice.InvoiceLateFeesConfigurationRequest;

@Data
public class GoHighLevelInvoiceLateFeesConfigurationRequest {
    private String altId;
    private String altType;
    private LateFeesConfiguration lateFeesConfiguration;

    public static GoHighLevelInvoiceLateFeesConfigurationRequest fromRequest(InvoiceLateFeesConfigurationRequest request) {
        GoHighLevelInvoiceLateFeesConfigurationRequest ghlRequest = new GoHighLevelInvoiceLateFeesConfigurationRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());

        if (request.getLateFeesConfiguration() != null) {
            LateFeesConfiguration ghlLateFeesConfig = new LateFeesConfiguration();
            ghlLateFeesConfig.setEnable(request.getLateFeesConfiguration().getEnable());
            ghlLateFeesConfig.setValue(request.getLateFeesConfiguration().getValue());
            ghlLateFeesConfig.setType(request.getLateFeesConfiguration().getType());

            if (request.getLateFeesConfiguration().getFrequency() != null) {
                Frequency ghlFrequency = new Frequency();
                ghlFrequency.setIntervalCount(request.getLateFeesConfiguration().getFrequency().getIntervalCount());
                ghlFrequency.setInterval(request.getLateFeesConfiguration().getFrequency().getInterval());
                ghlLateFeesConfig.setFrequency(ghlFrequency);
            }

            if (request.getLateFeesConfiguration().getGrace() != null) {
                Grace ghlGrace = new Grace();
                ghlGrace.setIntervalCount(request.getLateFeesConfiguration().getGrace().getIntervalCount());
                ghlGrace.setInterval(request.getLateFeesConfiguration().getGrace().getInterval());
                ghlLateFeesConfig.setGrace(ghlGrace);
            }

            if (request.getLateFeesConfiguration().getMaxLateFees() != null) {
                MaxLateFees ghlMaxLateFees = new MaxLateFees();
                ghlMaxLateFees.setType(request.getLateFeesConfiguration().getMaxLateFees().getType());
                ghlMaxLateFees.setValue(request.getLateFeesConfiguration().getMaxLateFees().getValue());
                ghlLateFeesConfig.setMaxLateFees(ghlMaxLateFees);
            }

            ghlRequest.setLateFeesConfiguration(ghlLateFeesConfig);
        }

        return ghlRequest;
    }

    @Data
    public static class LateFeesConfiguration {
        private Boolean enable;
        private Integer value;
        private String type;
        private Frequency frequency;
        private Grace grace;
        private MaxLateFees maxLateFees;
    }

    @Data
    public static class Frequency {
        private Integer intervalCount;
        private String interval;
    }

    @Data
    public static class Grace {
        private Integer intervalCount;
        private String interval;
    }

    @Data
    public static class MaxLateFees {
        private String type;
        private String value;
    }
}
