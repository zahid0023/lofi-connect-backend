package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.triggerlinks.LinkCreateRequest;
import org.example.loficonnect.dto.request.triggerlinks.LinkUpdateRequest;

import java.util.Map;

public interface TriggerLinkService {
    JsonNode updateLink(String linkId, LinkUpdateRequest request);
    JsonNode deleteLink(String linkId);
    JsonNode getLinks(Map<String, Object> queryParams);
    JsonNode createLink(LinkCreateRequest request);
}
