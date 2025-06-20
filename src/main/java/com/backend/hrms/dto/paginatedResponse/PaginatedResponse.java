package com.backend.hrms.dto.paginatedResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedResponse<T> {
    private List<T> data;
    private Pagination pagination;

    public PaginatedResponse(List<T> data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    @Getter
    @Setter
    public static class Pagination {
        private int page;
        private int limit;
        private long total;
        private int totalPages;

        public Pagination(int page, int limit, long total, int totalPages) {
            this.page = page;
            this.limit = limit;
            this.total = total;
            this.totalPages = totalPages;
        }
    }
}
