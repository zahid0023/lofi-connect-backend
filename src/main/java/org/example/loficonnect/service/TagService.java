package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.tag.TagByIdsRequest;

import java.util.Map;

public interface TagService {
    JsonNode getTags(String locationId, Map<String, Object> queryParams);
    JsonNode getTagsByIds(String locationId, TagByIdsRequest request);
}
