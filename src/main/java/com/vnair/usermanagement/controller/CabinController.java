package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.request.CabinCreateRequestDTO;
import com.vnair.usermanagement.dto.request.CabinUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.CabinResponseDTO;
import com.vnair.usermanagement.service.CabinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/cabins")
public class CabinController {
    @Autowired
    private CabinService cabinService;

    @PostMapping
    public CabinResponseDTO createCabin(@Valid @RequestBody CabinCreateRequestDTO request) {
        return cabinService.createCabin(request);
    }

    @PutMapping("/{id}")
    public CabinResponseDTO updateCabin(@PathVariable Long id, @Valid @RequestBody CabinUpdateRequestDTO request) {
        return cabinService.updateCabin(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCabin(@PathVariable Long id) {
        cabinService.deleteCabin(id);
    }

    @GetMapping("/{id}")
    public CabinResponseDTO getCabin(@PathVariable Long id) {
        return cabinService.getCabin(id);
    }

    @GetMapping
    public List<CabinResponseDTO> getAllCabins() {
        return cabinService.getAllCabins();
    }

    // Lấy danh sách cabin của một máy bay
    @GetMapping("/aircraft/{aircraftId}")
    public List<CabinResponseDTO> getCabinsByAircraft(@PathVariable Long aircraftId) {
        return cabinService.getCabinsByAircraft(aircraftId);
    }
}
