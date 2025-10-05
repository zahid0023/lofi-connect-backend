package org.example.loficonnect.dto.request.custommenulink;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomMenuLinkCreateRequest {
    private String title;
    private String url;
    private Icon icon;
    private Boolean show_on_company;
    private Boolean show_on_location;
    private Boolean show_to_all_locations;
    private String open_mode;
    private List<String> locations;
    private String user_role;
    private Boolean allow_camera;
    private Boolean allow_microphone;

    @Data
    public static class Icon {
        private String name;
        private String font_family;
    }
}
