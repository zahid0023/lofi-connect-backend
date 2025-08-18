package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.contact.ContactCreateRequest;
import org.example.loficonnect.dto.request.contact.ContactUpdateRequest;

public interface ContactService {
    JsonNode getContact(String contactId);

    JsonNode updateContact(String contactId, ContactUpdateRequest request);

    JsonNode deleteContact(String contactId);

    JsonNode getBusinessContacts(String businessId);

    JsonNode createContact(ContactCreateRequest request);


}
