package org.example.loficonnect.dto.mapper.tag;

import lombok.Data;
import org.example.loficonnect.dto.request.tag.TagByIdsRequest;

import java.util.List;

@Data
public class GoHighLevelTagByIdsRequest {
    private List<String> tagIds;

    public static GoHighLevelTagByIdsRequest fromRequest(TagByIdsRequest request) {
        GoHighLevelTagByIdsRequest ghlRequest = new GoHighLevelTagByIdsRequest();
        ghlRequest.setTagIds(request.getTagIds());
        return ghlRequest;
    }
}
