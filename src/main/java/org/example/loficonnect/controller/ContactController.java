package org.example.loficonnect.controller;

import org.example.loficonnect.config.AppKey;
import org.example.loficonnect.dto.request.contact.ContactCreateRequest;
import org.example.loficonnect.dto.request.contact.ContactUpdateRequest;
import org.example.loficonnect.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @AppKey
    @GetMapping("/contacts/{contact-id}")
    public ResponseEntity<?> getContact(@PathVariable("contact-id") String contactId) {
        return ResponseEntity.ok(contactService.getContact(contactId));
    }

    @AppKey
    @PutMapping("/contacts/{contact-id}")
    public ResponseEntity<?> updateContact(
            @PathVariable("contact-id") String contactId,
            @RequestBody ContactUpdateRequest request
    ) {
        return ResponseEntity.ok(contactService.updateContact(contactId, request));
    }

    @AppKey
    @DeleteMapping("/contacts/{contact-id}")
    public ResponseEntity<?> deleteContact(@PathVariable("contact-id") String contactId) {
        return ResponseEntity.ok(contactService.deleteContact(contactId));
    }

    @AppKey
    @GetMapping("/contacts/business/{business-id}")
    public ResponseEntity<?> getBusinessContacts(@PathVariable("business-id") String businessId) {
        return ResponseEntity.ok(contactService.getBusinessContacts(businessId));
    }

    @AppKey
    @PostMapping("/contacts")
    public ResponseEntity<?> createContact(@RequestBody ContactCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.createContact(request));
    }


}
