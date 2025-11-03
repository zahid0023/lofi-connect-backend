package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.notes.ContactNoteCreateRequest;
import org.example.loficonnect.dto.request.notes.ContactNoteUpdateRequest;
import org.example.loficonnect.service.NotesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ghl")
public class NotesController {
    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @AppKey
    @GetMapping("/contacts/{contact-id}/notes")
    public ResponseEntity<?> getContactNotes(
            @PathVariable("contact-id") String contactId
    ) {
        return ResponseEntity.ok(notesService.getContactNotes(contactId));
    }

    @AppKey
    @PostMapping("/contacts/{contact-id}/notes")
    public ResponseEntity<?> createContactNote(
            @PathVariable("contact-id") String contactId,
            @RequestBody ContactNoteCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notesService.createContactNote(contactId, request));
    }

    @AppKey
    @GetMapping("/contacts/{contact-id}/notes/{note-id}")
    public ResponseEntity<?> getContactNoteById(
            @PathVariable("contact-id") String contactId,
            @PathVariable("note-id") String noteId
    ) {
        return ResponseEntity.ok(notesService.getContactNoteById(contactId, noteId));
    }

    @AppKey
    @PutMapping("/contacts/{contact-id}/notes/{note-id}")
    public ResponseEntity<?> updateContactNote(
            @PathVariable("contact-id") String contactId,
            @PathVariable("note-id") String noteId,
            @RequestBody ContactNoteUpdateRequest request
    ) {
        return ResponseEntity.ok(notesService.updateContactNote(contactId, noteId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}/notes/{note-id}")
    public ResponseEntity<?> deleteContactNote(
            @PathVariable("contact-id") String contactId,
            @PathVariable("note-id") String noteId
    ) {
        return ResponseEntity.ok(notesService.deleteContactNote(contactId, noteId));
    }
}
