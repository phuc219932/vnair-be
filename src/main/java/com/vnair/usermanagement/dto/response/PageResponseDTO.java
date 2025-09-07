package com.vnair.usermanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO để trả về dữ liệu phân trang
 */
@Schema(description = "Dữ liệu phân trang")
public class PageResponseDTO<T> {
    
    @Schema(description = "Danh sách dữ liệu trong trang hiện tại")
    private List<T> content;
    
    @Schema(description = "Tổng số bản ghi", example = "100")
    private long totalElements;
    
    @Schema(description = "Tổng số trang", example = "10")
    private int totalPages;
    
    @Schema(description = "Số trang hiện tại (bắt đầu từ 0)", example = "0")
    private int pageNumber;
    
    @Schema(description = "Kích thước trang", example = "20")
    private int pageSize;
    
    @Schema(description = "Có trang đầu tiên hay không", example = "true")
    private boolean first;
    
    @Schema(description = "Có trang cuối cùng hay không", example = "false")
    private boolean last;
    
    @Schema(description = "Số lượng bản ghi trong trang hiện tại", example = "20")
    private int numberOfElements;

    // Constructors
    public PageResponseDTO() {}

    public PageResponseDTO(List<T> content, long totalElements, int totalPages, int pageNumber, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.numberOfElements = content != null ? content.size() : 0;
        this.first = pageNumber == 0;
        this.last = pageNumber >= totalPages - 1;
    }

    // Getters and Setters
    public List<T> getContent() { 
        return content; 
    }
    
    public void setContent(List<T> content) { 
        this.content = content;
        this.numberOfElements = content != null ? content.size() : 0;
    }

    public long getTotalElements() { 
        return totalElements; 
    }
    
    public void setTotalElements(long totalElements) { 
        this.totalElements = totalElements; 
    }

    public int getTotalPages() { 
        return totalPages; 
    }
    
    public void setTotalPages(int totalPages) { 
        this.totalPages = totalPages;
        this.last = pageNumber >= totalPages - 1;
    }

    public int getPageNumber() { 
        return pageNumber; 
    }
    
    public void setPageNumber(int pageNumber) { 
        this.pageNumber = pageNumber;
        this.first = pageNumber == 0;
        this.last = pageNumber >= totalPages - 1;
    }

    public int getPageSize() { 
        return pageSize; 
    }
    
    public void setPageSize(int pageSize) { 
        this.pageSize = pageSize; 
    }

    public boolean isFirst() { 
        return first; 
    }
    
    public void setFirst(boolean first) { 
        this.first = first; 
    }

    public boolean isLast() { 
        return last; 
    }
    
    public void setLast(boolean last) { 
        this.last = last; 
    }

    public int getNumberOfElements() { 
        return numberOfElements; 
    }
    
    public void setNumberOfElements(int numberOfElements) { 
        this.numberOfElements = numberOfElements; 
    }
}
