package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.tags.ContactTagCreateRequest;
import org.example.loficonnect.dto.request.tags.ContactTagDeleteRequest;

public interface TagsService {
    JsonNode createContactTags(String contactId, ContactTagCreateRequest request);

    JsonNode deleteContactTags(String contactId, ContactTagDeleteRequest request);

}
