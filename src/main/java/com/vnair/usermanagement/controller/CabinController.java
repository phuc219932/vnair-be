package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.request.CabinCreateRequestDTO;
import com.vnair.usermanagement.dto.request.CabinPageRequestDTO;
import com.vnair.usermanagement.dto.request.CabinUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.CabinResponseDTO;
import com.vnair.usermanagement.dto.response.PageResponseDTO;
import com.vnair.usermanagement.service.CabinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/cabins")
@Tag(name = "Quản lý khoang máy bay", description = "API quản lý các khoang (cabin) trong máy bay")
@SecurityRequirement(name = "Bearer Authentication")
public class CabinController {
    @Autowired
    private CabinService cabinService;

    @PostMapping
    @Operation(summary = "Tạo khoang máy bay mới", 
               description = "Tạo một khoang mới cho máy bay")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Tạo khoang thành công",
                    content = @Content(schema = @Schema(implementation = CabinResponseDTO.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Dữ liệu không hợp lệ"),
        @ApiResponse(responseCode = "404", 
                    description = "Không tìm thấy máy bay")
    })
    public CabinResponseDTO createCabin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Thông tin khoang mới",
                content = @Content(
                    schema = @Schema(implementation = CabinCreateRequestDTO.class),
                    examples = {
                        @ExampleObject(name = "Economy", 
                                      value = "{\n" +
                                             "  \"name\": \"Economy\",\n" +
                                             "  \"position\": \"REAR\",\n" +
                                             "  \"seatCount\": 180,\n" +
                                             "  \"description\": \"Standard economy cabin\",\n" +
                                             "  \"aircraftId\": 1\n" +
                                             "}"),
                        @ExampleObject(name = "Business", 
                                      value = "{\n" +
                                             "  \"name\": \"Business\",\n" +
                                             "  \"position\": \"FRONT\",\n" +
                                             "  \"seatCount\": 24,\n" +
                                             "  \"description\": \"Business class cabin\",\n" +
                                             "  \"aircraftId\": 1\n" +
                                             "}")
                    }
                )
            )
            @Valid @RequestBody CabinCreateRequestDTO request) {
        return cabinService.createCabin(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin khoang", 
               description = "Cập nhật thông tin khoang máy bay theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Cập nhật thành công",
                    content = @Content(schema = @Schema(implementation = CabinResponseDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Không tìm thấy khoang hoặc máy bay"),
        @ApiResponse(responseCode = "400", 
                    description = "Dữ liệu không hợp lệ")
    })
    public CabinResponseDTO updateCabin(
            @Parameter(description = "ID của khoang cần cập nhật", required = true)
            @PathVariable Long id, 
            @Valid @RequestBody CabinUpdateRequestDTO request) {
        return cabinService.updateCabin(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa khoang", 
               description = "Xóa khoang máy bay theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", 
                    description = "Không tìm thấy khoang")
    })
    public void deleteCabin(
            @Parameter(description = "ID của khoang cần xóa", required = true)
            @PathVariable Long id) {
        cabinService.deleteCabin(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin khoang", 
               description = "Lấy thông tin chi tiết một khoang theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lấy thông tin thành công",
                    content = @Content(schema = @Schema(implementation = CabinResponseDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Không tìm thấy khoang")
    })
    public CabinResponseDTO getCabin(
            @Parameter(description = "ID của khoang", required = true)
            @PathVariable Long id) {
        return cabinService.getCabin(id);
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả khoang", 
               description = "Lấy danh sách tất cả các khoang trong hệ thống")
    @ApiResponse(responseCode = "200", 
                description = "Lấy danh sách thành công",
                content = @Content(schema = @Schema(implementation = CabinResponseDTO.class)))
    public List<CabinResponseDTO> getAllCabins() {
        return cabinService.getAllCabins();
    }

    // Lấy danh sách cabin của một máy bay
    @GetMapping("/aircraft/{aircraftId}")
    @Operation(summary = "Lấy danh sách khoang theo máy bay", 
               description = "Lấy danh sách tất cả các khoang của một máy bay cụ thể")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = CabinResponseDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Không tìm thấy máy bay")
    })
    public List<CabinResponseDTO> getCabinsByAircraft(
            @Parameter(description = "ID của máy bay", required = true)
            @PathVariable Long aircraftId) {
        return cabinService.getCabinsByAircraft(aircraftId);
    }

    @GetMapping("/page")
    @Operation(summary = "Lấy danh sách khoang với phân trang", 
               description = "Lấy danh sách khoang có phân trang và sắp xếp")
    @ApiResponse(responseCode = "200", 
                description = "Lấy danh sách thành công với phân trang")
    public PageResponseDTO<CabinResponseDTO> getAllCabinsWithPagination(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") Integer pageNumber,
            @Parameter(description = "Số lượng bản ghi trên mỗi trang") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "Trường sắp xếp") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Hướng sắp xếp") @RequestParam(defaultValue = "asc") String sortDir) {
        
        CabinPageRequestDTO request = new CabinPageRequestDTO();
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setSortBy(sortBy);
        request.setSortDir(sortDir);
        
        return cabinService.getAllCabinsWithPagination(request);
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm khoang", 
               description = "Tìm kiếm khoang theo các tiêu chí với phân trang")
    @ApiResponse(responseCode = "200", 
                description = "Tìm kiếm thành công")
    public PageResponseDTO<CabinResponseDTO> searchCabins(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword,
            @Parameter(description = "ID máy bay") @RequestParam(required = false) Long aircraftId,
            @Parameter(description = "Vị trí khoang") @RequestParam(required = false) String position,
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") Integer pageNumber,
            @Parameter(description = "Số lượng bản ghi trên mỗi trang") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "Trường sắp xếp") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Hướng sắp xếp") @RequestParam(defaultValue = "asc") String sortDir) {
        
        CabinPageRequestDTO request = new CabinPageRequestDTO();
        request.setSearchKeyword(keyword);
        request.setAircraftId(aircraftId);
        request.setPosition(position);
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setSortBy(sortBy);
        request.setSortDir(sortDir);
        
        return cabinService.searchCabins(request);
    }

    @GetMapping("/aircraft/{aircraftId}/page")
    @Operation(summary = "Lấy danh sách khoang theo máy bay với phân trang", 
               description = "Lấy danh sách khoang của một máy bay cụ thể với phân trang")
    @ApiResponse(responseCode = "200", 
                description = "Lấy danh sách thành công với phân trang")
    public PageResponseDTO<CabinResponseDTO> getCabinsByAircraftWithPagination(
            @Parameter(description = "ID của máy bay", required = true) @PathVariable Long aircraftId,
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") Integer pageNumber,
            @Parameter(description = "Số lượng bản ghi trên mỗi trang") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "Trường sắp xếp") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Hướng sắp xếp") @RequestParam(defaultValue = "asc") String sortDir) {
        
        CabinPageRequestDTO request = new CabinPageRequestDTO();
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setSortBy(sortBy);
        request.setSortDir(sortDir);
        
        return cabinService.getCabinsByAircraftWithPagination(aircraftId, request);
    }
}
