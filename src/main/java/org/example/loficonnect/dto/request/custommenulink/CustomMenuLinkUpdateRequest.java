package org.example.loficonnect.dto.request.custommenulink;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomMenuLinkUpdateRequest {
    private String title;
    private String url;
    private Icon icon;
    private Boolean showOnCompany;
    private Boolean showOnLocation;
    private Boolean showToAllLocations;
    private String openMode;
    private List<String> locations;
    private String userRole;
    private Boolean allowCamera;
    private Boolean allowMicrophone;

    @Data
    public static class Icon {
        private String name;
        private String fontFamily;
    }
}
