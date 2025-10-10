package org.example.loficonnect.dto.mapper.custommenulink;

import lombok.Data;
import org.example.loficonnect.dto.request.custommenulink.CustomMenuLinkUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelCustomMenuLinkUpdateRequest {
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

    public static GoHighLevelCustomMenuLinkUpdateRequest fromRequest(CustomMenuLinkUpdateRequest request) {
        GoHighLevelCustomMenuLinkUpdateRequest ghlRequest = new GoHighLevelCustomMenuLinkUpdateRequest();
        ghlRequest.setTitle(request.getTitle());
        ghlRequest.setUrl(request.getUrl());

        if (request.getIcon() != null) {
            Icon icon = new Icon();
            icon.setName(request.getIcon().getName());
            icon.setFontFamily(request.getIcon().getFontFamily());
            ghlRequest.setIcon(icon);
        }

        ghlRequest.setShowOnCompany(request.getShowOnCompany());
        ghlRequest.setShowOnLocation(request.getShowOnLocation());
        ghlRequest.setShowToAllLocations(request.getShowToAllLocations());
        ghlRequest.setOpenMode(request.getOpenMode());
        ghlRequest.setLocations(request.getLocations());
        ghlRequest.setUserRole(request.getUserRole());
        ghlRequest.setAllowCamera(request.getAllowCamera());
        ghlRequest.setAllowMicrophone(request.getAllowMicrophone());

        return ghlRequest;
    }
}
