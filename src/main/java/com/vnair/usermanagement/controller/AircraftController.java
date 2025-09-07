package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.request.AircraftCreateRequestDTO;
import com.vnair.usermanagement.dto.request.AircraftPageRequestDTO;
import com.vnair.usermanagement.dto.request.AircraftUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.AircraftResponseDTO;
import com.vnair.usermanagement.dto.response.PageResponseDTO;
import com.vnair.usermanagement.service.AircraftService;
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
@RequestMapping("/api/aircrafts")
@Tag(name = "Quản lý máy bay", description = "API quản lý thông tin máy bay")
@SecurityRequirement(name = "Bearer Authentication")
public class AircraftController {
    @Autowired
    private AircraftService aircraftService;

    @PostMapping
    @Operation(summary = "Tạo máy bay mới", 
               description = "Tạo một máy bay mới với mã và tên máy bay")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Tạo máy bay thành công",
                    content = @Content(mediaType = "application/json",
                               schema = @Schema(implementation = AircraftResponseDTO.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Dữ liệu không hợp lệ")
    })
    public AircraftResponseDTO createAircraft(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Thông tin máy bay mới",
                content = @Content(
                    schema = @Schema(implementation = AircraftCreateRequestDTO.class),
                    examples = @ExampleObject(
                        value = "{\n" +
                               "  \"code\": \"VN-A321\",\n" +
                               "  \"name\": \"Airbus A321\"\n" +
                               "}"
                    )
                )
            )
            @Valid @RequestBody AircraftCreateRequestDTO request) {
        return aircraftService.createAircraft(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin máy bay", 
               description = "Cập nhật thông tin máy bay theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Cập nhật thành công",
                    content = @Content(schema = @Schema(implementation = AircraftResponseDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Không tìm thấy máy bay"),
        @ApiResponse(responseCode = "400", 
                    description = "Dữ liệu không hợp lệ")
    })
    public AircraftResponseDTO updateAircraft(
            @Parameter(description = "ID của máy bay cần cập nhật", required = true)
            @PathVariable Long id, 
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Thông tin cập nhật máy bay",
                content = @Content(
                    schema = @Schema(implementation = AircraftUpdateRequestDTO.class),
                    examples = @ExampleObject(
                        value = "{\n" +
                               "  \"code\": \"VN-A321-Updated\",\n" +
                               "  \"name\": \"Airbus A321 Neo\"\n" +
                               "}"
                    )
                )
            )
            @Valid @RequestBody AircraftUpdateRequestDTO request) {
        return aircraftService.updateAircraft(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa máy bay", 
               description = "Xóa máy bay theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", 
                    description = "Không tìm thấy máy bay")
    })
    public void deleteAircraft(
            @Parameter(description = "ID của máy bay cần xóa", required = true)
            @PathVariable Long id) {
        aircraftService.deleteAircraft(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin máy bay", 
               description = "Lấy thông tin chi tiết một máy bay theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lấy thông tin thành công",
                    content = @Content(schema = @Schema(implementation = AircraftResponseDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Không tìm thấy máy bay")
    })
    public AircraftResponseDTO getAircraft(
            @Parameter(description = "ID của máy bay", required = true)
            @PathVariable Long id) {
        return aircraftService.getAircraft(id);
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả máy bay", 
               description = "Lấy danh sách tất cả các máy bay trong hệ thống")
    @ApiResponse(responseCode = "200", 
                description = "Lấy danh sách thành công",
                content = @Content(schema = @Schema(implementation = AircraftResponseDTO.class)))
    public List<AircraftResponseDTO> getAllAircrafts() {
        return aircraftService.getAllAircrafts();
    }

    @GetMapping("/page")
    @Operation(summary = "Lấy danh sách máy bay với phân trang", 
               description = "Lấy danh sách máy bay có phân trang và sắp xếp")
    @ApiResponse(responseCode = "200", 
                description = "Lấy danh sách thành công với phân trang")
    public PageResponseDTO<AircraftResponseDTO> getAllAircraftsWithPagination(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") Integer pageNumber,
            @Parameter(description = "Số lượng bản ghi trên mỗi trang") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "Trường sắp xếp") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Hướng sắp xếp") @RequestParam(defaultValue = "asc") String sortDir) {
        
        AircraftPageRequestDTO request = new AircraftPageRequestDTO();
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setSortBy(sortBy);
        request.setSortDir(sortDir);
        
        return aircraftService.getAllAircraftsWithPagination(request);
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm máy bay", 
               description = "Tìm kiếm máy bay theo mã hoặc tên với phân trang")
    @ApiResponse(responseCode = "200", 
                description = "Tìm kiếm thành công")
    public PageResponseDTO<AircraftResponseDTO> searchAircrafts(
            @Parameter(description = "Từ khóa tìm kiếm", required = true) @RequestParam String keyword,
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") Integer pageNumber,
            @Parameter(description = "Số lượng bản ghi trên mỗi trang") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "Trường sắp xếp") @RequestParam(defaultValue = "code") String sortBy,
            @Parameter(description = "Hướng sắp xếp") @RequestParam(defaultValue = "asc") String sortDir) {
        
        AircraftPageRequestDTO request = new AircraftPageRequestDTO();
        request.setSearchKeyword(keyword);
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setSortBy(sortBy);
        request.setSortDir(sortDir);
        
        return aircraftService.searchAircrafts(request);
    }
}
