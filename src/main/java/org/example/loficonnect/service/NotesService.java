package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.notes.ContactNoteCreateRequest;
import org.example.loficonnect.dto.request.notes.ContactNoteUpdateRequest;

import java.util.Map;

public interface NotesService {
    JsonNode getContactNotes(String contactId);

    JsonNode createContactNote(String contactId, ContactNoteCreateRequest request);

    JsonNode getContactNoteById(String contactId, String noteId);

    JsonNode updateContactNote(String contactId, String noteId, ContactNoteUpdateRequest request);

    JsonNode deleteContactNote(String contactId, String noteId);
}
