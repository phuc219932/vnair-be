package com.vnair.usermanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

/**
 * DTO cơ bản cho pagination request
 */
@Schema(description = "Tham số phân trang")
public class PaginationRequestDTO implements PageableRequest {
    
    @Schema(description = "Số trang (bắt đầu từ 0)", example = "0", defaultValue = "0")
    @Min(value = 0, message = "Số trang phải >= 0")
    private Integer pageNumber;

    @Schema(description = "Số lượng bản ghi trên mỗi trang", example = "20", defaultValue = "20")
    @Min(value = 1, message = "Kích thước trang phải >= 1")
    private Integer pageSize;

    @Schema(description = "Trường sắp xếp", example = "id", defaultValue = "id")
    private String sortBy;

    @Schema(description = "Hướng sắp xếp", example = "asc", defaultValue = "asc", allowableValues = {"asc", "desc"})
    @Pattern(regexp = "^(?i)(asc|desc)$", message = "Hướng sắp xếp phải là 'asc' hoặc 'desc'")
    private String sortDir;

    // Constructors
    public PaginationRequestDTO() {}

    public PaginationRequestDTO(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.sortDir = sortDir;
    }

    // Getters and Setters
    @Override
    public Integer getPageNumber() { 
        return pageNumber; 
    }
    
    public void setPageNumber(Integer pageNumber) { 
        this.pageNumber = pageNumber; 
    }

    @Override
    public Integer getPageSize() { 
        return pageSize; 
    }
    
    public void setPageSize(Integer pageSize) { 
        this.pageSize = pageSize; 
    }

    @Override
    public String getSortBy() { 
        return sortBy; 
    }
    
    public void setSortBy(String sortBy) { 
        this.sortBy = sortBy; 
    }

    @Override
    public String getSortDir() { 
        return sortDir; 
    }
    
    public void setSortDir(String sortDir) { 
        this.sortDir = sortDir; 
    }
}
