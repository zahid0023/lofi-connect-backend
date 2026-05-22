package org.example.loficonnect.commons.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaginatedResponse<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
