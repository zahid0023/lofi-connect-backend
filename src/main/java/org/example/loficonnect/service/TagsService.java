package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.tags.ContactTagsAddRequest;
import org.example.loficonnect.dto.request.tags.ContactTagsRemoveRequest;
import org.example.loficonnect.dto.request.tags.TagCreateRequest;
import org.example.loficonnect.dto.request.tags.TagUpdateRequest;

public interface TagsService {
    JsonNode createContactTags(String contactId, ContactTagsAddRequest request);

    JsonNode deleteContactTags(String contactId, ContactTagsRemoveRequest request);

    JsonNode getLocationTags();

    JsonNode createLocationTag(TagCreateRequest request);

    JsonNode getTagById(String tagId);

    JsonNode updateTag(String tagId, TagUpdateRequest request);

    JsonNode deleteTag(String tagId);
}
