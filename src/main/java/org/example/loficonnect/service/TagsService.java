package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.tags.ContactTagCreateRequest;
import org.example.loficonnect.dto.request.tags.ContactTagDeleteRequest;
import org.example.loficonnect.dto.request.tags.TagCreateRequest;
import org.example.loficonnect.dto.request.tags.TagUpdateRequest;

public interface TagsService {
    JsonNode createContactTags(String contactId, ContactTagCreateRequest request);

    JsonNode deleteContactTags(String contactId, ContactTagDeleteRequest request);

    JsonNode getLocationTags(String locationId);

    JsonNode createLocationTag(String locationId, TagCreateRequest request);

    JsonNode getTagById(String locationId, String tagId);

    JsonNode updateTag(String locationId, String tagId, TagUpdateRequest request);

    JsonNode deleteTag(String locationId, String tagId);


}
