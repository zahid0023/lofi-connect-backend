package org.example.loficonnect.dto.request.invoicetemplate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InvoiceTemplateLateFeesConfigurationUpdateRequest {
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
}
