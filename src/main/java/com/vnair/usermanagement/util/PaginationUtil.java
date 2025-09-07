package com.vnair.usermanagement.util;

import com.vnair.usermanagement.dto.request.PageableRequest;
import com.vnair.usermanagement.dto.response.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utility class để xử lý pagination
 */
public class PaginationUtil {

    /**
     * Chuyển đổi PageableRequest thành Spring Pageable
     */
    public static Pageable createPageable(PageableRequest request) {
        Sort sort = createSort(request.sortByOrDefault(), request.sortDirOrDefault());
        return PageRequest.of(request.pageNumberOrDefault(), request.pageSizeOrDefault(), sort);
    }

    /**
     * Tạo Sort object từ sortBy và sortDir
     */
    public static Sort createSort(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
    }

    /**
     * Chuyển đổi Spring Page thành PageResponseDTO
     */
    public static <T> PageResponseDTO<T> createPageResponse(Page<T> page) {
        return new PageResponseDTO<>(
            page.getContent(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize()
        );
    }

    /**
     * Chuyển đổi với custom content (khi cần mapping từ entity sang DTO)
     */
    public static <T> PageResponseDTO<T> createPageResponse(Page<?> page, java.util.List<T> content) {
        return new PageResponseDTO<>(
            content,
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize()
        );
    }
}
