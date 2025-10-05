package org.example.loficonnect.dto.mapper.custommenulink;

import lombok.Data;
import org.example.loficonnect.dto.request.custommenulink.CustomMenuLinkCreateRequest;

import java.util.List;

@Data
public class GoHighLevelCustomMenuLinkCreateRequest {
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

    public static GoHighLevelCustomMenuLinkCreateRequest fromRequest(CustomMenuLinkCreateRequest request) {
        GoHighLevelCustomMenuLinkCreateRequest ghlRequest = new GoHighLevelCustomMenuLinkCreateRequest();

        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setUrl(request.getUrl());

        Icon icon = new Icon();
        icon.setName(request.getIcon().getName());
        icon.setFontFamily(request.getIcon().getFont_family());
        ghlRequest.setIcon(icon);

        ghlRequest.setShowOnCompany(request.getShow_on_company());
        ghlRequest.setShowOnLocation(request.getShow_on_location());
        ghlRequest.setShowToAllLocations(request.getShow_to_all_locations());
        ghlRequest.setOpenMode(request.getOpen_mode());
        ghlRequest.setLocations(request.getLocations());
        ghlRequest.setUserRole(request.getUser_role());
        ghlRequest.setAllowCamera(request.getAllow_camera());
        ghlRequest.setAllowMicrophone(request.getAllow_microphone());

        return ghlRequest;
    }
}
