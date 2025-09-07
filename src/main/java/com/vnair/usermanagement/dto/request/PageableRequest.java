package com.vnair.usermanagement.dto.request;

/**
 * Interface cho các request có hỗ trợ phân trang
 */
public interface PageableRequest {
    Integer getPageNumber();
    Integer getPageSize();
    String getSortBy();
    String getSortDir();

    /**
     * Trả về page number mặc định là 0 nếu null hoặc âm
     */
    default int pageNumberOrDefault() {
        Integer p = getPageNumber();
        return (p == null || p < 0) ? 0 : p;
    }

    /**
     * Trả về page size mặc định là 20 nếu null hoặc không hợp lệ
     */
    default int pageSizeOrDefault() {
        Integer s = getPageSize();
        return (s == null || s <= 0) ? 20 : Math.min(s, 100); // Giới hạn tối đa 100
    }

    /**
     * Trả về sort field mặc định là "id" nếu null hoặc rỗng
     */
    default String sortByOrDefault() {
        String s = getSortBy();
        return (s == null || s.isBlank()) ? "id" : s;
    }

    /**
     * Trả về sort direction mặc định là "asc" nếu không hợp lệ
     */
    default String sortDirOrDefault() {
        String d = getSortDir();
        return (d == null || (!d.equalsIgnoreCase("asc") && !d.equalsIgnoreCase("desc"))) ? "asc" : d.toLowerCase();
    }
}
