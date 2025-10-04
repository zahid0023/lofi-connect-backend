package org.example.loficonnect.dto.mapper.store;

import lombok.Data;
import org.example.loficonnect.dto.request.store.StoreUpdateRequest;

import java.util.List;

@Data
public class GoHighLevelStoreUpdateRequest {
    private String action;
    private List<String> productIds;

    public static GoHighLevelStoreUpdateRequest fromRequest(StoreUpdateRequest request) {
        GoHighLevelStoreUpdateRequest ghl = new GoHighLevelStoreUpdateRequest();
        ghl.setAction(request.getAction());
        ghl.setProductIds(request.getProduct_ids());
        return ghl;
    }
}
