package org.example.loficonnect.dto.mapper.searchobjectrecords;

import lombok.Data;
import org.example.loficonnect.dto.request.searchobjectrecords.SearchObjectRecordsRequest;

import java.util.List;

@Data
public class GoHighLevelSearchObjectRecordsRequest {
    private String locationId;
    private int page;
    private int pageLimit;
    private String query;
    private List<String> searchAfter;

    public static GoHighLevelSearchObjectRecordsRequest fromRequest(SearchObjectRecordsRequest request) {
        GoHighLevelSearchObjectRecordsRequest ghlRequest = new GoHighLevelSearchObjectRecordsRequest();
        ghlRequest.setLocationId(request.getLocationId());
        ghlRequest.setPage(request.getPage());
        ghlRequest.setPageLimit(request.getPageLimit());
        ghlRequest.setQuery(request.getQuery());
        ghlRequest.setSearchAfter(request.getSearchAfter());
        return ghlRequest;
    }
}
