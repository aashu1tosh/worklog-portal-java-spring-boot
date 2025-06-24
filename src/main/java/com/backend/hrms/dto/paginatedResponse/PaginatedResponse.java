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
        private int size;
        private long total;
        private int totalPages;

        public Pagination(int page, int size, long total, int totalPages) {
            this.page = page;
            this.size = size;
            this.total = total;
            this.totalPages = totalPages;
        }
    }
}
