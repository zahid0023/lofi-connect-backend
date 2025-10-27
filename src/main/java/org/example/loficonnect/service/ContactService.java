package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.contact.ContactCreateRequest;
import org.example.loficonnect.dto.request.contact.ContactUpdateRequest;
import org.example.loficonnect.dto.request.contact.ContactUpsertRequest;
import org.example.loficonnect.dto.response.contacts.CreateContactResponse;

public interface ContactService {
    JsonNode getContact(String contactId);

    JsonNode updateContact(String contactId, ContactUpdateRequest request);

    JsonNode deleteContact(String contactId);

    JsonNode getBusinessContacts(String businessId);

    CreateContactResponse createContact(ContactCreateRequest request);

    JsonNode upsertContact(ContactUpsertRequest request);

}
