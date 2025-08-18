package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface CompanyService {
    JsonNode getCompany(String companyId);
}
