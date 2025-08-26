package org.example.loficonnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.loficonnect.dto.request.bulk.BulkBusinessUpdateRequest;
import org.example.loficonnect.dto.request.bulk.BulkTagUpdateRequest;

public interface BulkService {
    JsonNode updateBulkTags(String type, BulkTagUpdateRequest request);

    JsonNode updateBulkBusiness(BulkBusinessUpdateRequest request);


}
