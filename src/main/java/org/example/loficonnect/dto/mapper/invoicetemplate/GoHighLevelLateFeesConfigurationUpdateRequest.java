package org.example.loficonnect.dto.mapper.invoicetemplate;

import lombok.Data;
import org.example.loficonnect.dto.request.invoicetemplate.InvoiceTemplateLateFeesConfigurationUpdateRequest;

@Data
public class GoHighLevelLateFeesConfigurationUpdateRequest {
    private String altId;
    private String altType;
    private LateFeesConfiguration lateFeesConfiguration;

    @Data
    public static class LateFeesConfiguration {
        private Boolean enable;
        private Double value;
        private String type;
        private Frequency frequency;
        private Frequency grace;
        private MaxLateFees maxLateFees;

        @Data
        public static class Frequency {
            private Integer intervalCount;
            private String interval;
        }

        @Data
        public static class MaxLateFees {
            private String type;
            private String value;
        }
    }

    public static GoHighLevelLateFeesConfigurationUpdateRequest fromRequest(InvoiceTemplateLateFeesConfigurationUpdateRequest request) {
        GoHighLevelLateFeesConfigurationUpdateRequest ghlRequest = new GoHighLevelLateFeesConfigurationUpdateRequest();

        ghlRequest.setAltId(request.getAltId());
        ghlRequest.setAltType(request.getAltType());

        LateFeesConfiguration ghlLateFeesConfig = new LateFeesConfiguration();
        InvoiceTemplateLateFeesConfigurationUpdateRequest.LateFeesConfiguration userLateFeesConfig = request.getLateFeesConfiguration();

        ghlLateFeesConfig.setEnable(userLateFeesConfig.getEnable());
        ghlLateFeesConfig.setValue(userLateFeesConfig.getValue());
        ghlLateFeesConfig.setType(userLateFeesConfig.getType());

        LateFeesConfiguration.Frequency frequency = new LateFeesConfiguration.Frequency();
        frequency.setIntervalCount(userLateFeesConfig.getFrequency().getIntervalCount());
        frequency.setInterval(userLateFeesConfig.getFrequency().getInterval());
        ghlLateFeesConfig.setFrequency(frequency);

        LateFeesConfiguration.Frequency grace = new LateFeesConfiguration.Frequency();
        grace.setIntervalCount(userLateFeesConfig.getGrace().getIntervalCount());
        grace.setInterval(userLateFeesConfig.getGrace().getInterval());
        ghlLateFeesConfig.setGrace(grace);

        LateFeesConfiguration.MaxLateFees maxLateFees = new LateFeesConfiguration.MaxLateFees();
        maxLateFees.setType(userLateFeesConfig.getMaxLateFees().getType());
        maxLateFees.setValue(userLateFeesConfig.getMaxLateFees().getValue());
        ghlLateFeesConfig.setMaxLateFees(maxLateFees);

        ghlRequest.setLateFeesConfiguration(ghlLateFeesConfig);

        return ghlRequest;
    }

}
