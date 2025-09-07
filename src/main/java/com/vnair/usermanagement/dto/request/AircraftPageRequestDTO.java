package com.vnair.usermanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

/**
 * DTO cho request lấy danh sách Aircraft với phân trang
 */
@Schema(description = "Request phân trang cho danh sách máy bay")
public class AircraftPageRequestDTO implements PageableRequest {
    
    @Schema(description = "Số trang (bắt đầu từ 0)", example = "0")
    @Min(value = 0, message = "Số trang phải >= 0")
    private Integer pageNumber;

    @Schema(description = "Số lượng bản ghi trên mỗi trang", example = "20")
    @Min(value = 1, message = "Kích thước trang phải >= 1")
    private Integer pageSize;

    @Schema(description = "Trường sắp xếp", example = "code", allowableValues = {"id", "code", "name", "createdAt"})
    private String sortBy;

    @Schema(description = "Hướng sắp xếp", example = "asc", allowableValues = {"asc", "desc"})
    @Pattern(regexp = "^(?i)(asc|desc)$", message = "Hướng sắp xếp phải là 'asc' hoặc 'desc'")
    private String sortDir;

    @Schema(description = "Từ khóa tìm kiếm theo mã hoặc tên máy bay", example = "A321")
    private String searchKeyword;

    // Constructors
    public AircraftPageRequestDTO() {}

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

    public String getSearchKeyword() { 
        return searchKeyword; 
    }
    
    public void setSearchKeyword(String searchKeyword) { 
        this.searchKeyword = searchKeyword; 
    }
}
