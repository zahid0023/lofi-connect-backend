package org.example.loficonnect.dto.mapper.search;

import lombok.Data;
import org.example.loficonnect.dto.request.search.ContactSearchRequest;

@Data
public class GoHighLevelContactSearchRequest {

    public static GoHighLevelContactSearchRequest fromRequest(ContactSearchRequest request) {
        return new GoHighLevelContactSearchRequest(); // empty payload
    }
}
