package org.example.loficonnect.dto.mapper.statictics;

import lombok.Data;
import org.example.loficonnect.dto.request.statictics.StaticticsRequest;

import java.util.List;

@Data
public class GoHighLevelStaticticsRequest {
    private List<String> profileIds;
    private List<String> platforms;

    public static GoHighLevelStaticticsRequest fromRequest(StaticticsRequest request) {
        GoHighLevelStaticticsRequest ghl = new GoHighLevelStaticticsRequest();
        ghl.setProfileIds(request.getProfileIds());
        ghl.setPlatforms(request.getPlatforms());
        return ghl;
    }
}
