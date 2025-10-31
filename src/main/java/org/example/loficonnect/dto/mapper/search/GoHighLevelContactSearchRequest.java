package org.example.loficonnect.dto.mapper.search;

import lombok.Data;
import org.example.loficonnect.dto.request.search.ContactSearchRequest;
import org.example.loficonnect.util.LocationContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GoHighLevelContactSearchRequest {
    private String locationId;
    private Integer pageLimit;
    private Integer page;
    private List<Map<String, Object>> filters;

    public static GoHighLevelContactSearchRequest fromRequest(ContactSearchRequest request) {
        GoHighLevelContactSearchRequest ghl = new GoHighLevelContactSearchRequest();

        ghl.setLocationId(LocationContext.getLocationId());
        ghl.setPageLimit(request.getPageLimit());
        ghl.setPage(request.getPage());

        List<Map<String, Object>> filterList = new ArrayList<>();

        if (StringUtils.hasText(request.getPhone())) {
            Map<String, Object> phoneFilter = new HashMap<>();
            phoneFilter.put("field", "phone");
            phoneFilter.put("operator", "eq");
            phoneFilter.put("value", request.getPhone());
            filterList.add(phoneFilter);
        }

        if (StringUtils.hasText(request.getEmail())) {
            Map<String, Object> emailFilter = new HashMap<>();
            emailFilter.put("field", "email");
            emailFilter.put("operator", "eq");
            emailFilter.put("value", request.getEmail());
            filterList.add(emailFilter);
        }

        ghl.setFilters(filterList.isEmpty() ? null : filterList);

        return ghl;
    }
}
