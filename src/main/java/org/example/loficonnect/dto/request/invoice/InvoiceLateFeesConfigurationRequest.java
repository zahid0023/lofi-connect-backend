package org.example.loficonnect.dto.request.invoice;

import lombok.Data;

@Data
public class InvoiceLateFeesConfigurationRequest {
    private String altId;
    private String altType;
    private LateFeesConfiguration lateFeesConfiguration;

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
