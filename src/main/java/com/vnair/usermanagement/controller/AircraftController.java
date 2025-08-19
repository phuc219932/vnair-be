package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.request.AircraftCreateRequestDTO;
import com.vnair.usermanagement.dto.request.AircraftUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.AircraftResponseDTO;
import com.vnair.usermanagement.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {
    @Autowired
    private AircraftService aircraftService;

    @PostMapping
    public AircraftResponseDTO createAircraft(@RequestBody AircraftCreateRequestDTO request) {
        return aircraftService.createAircraft(request);
    }

    @PutMapping("/{id}")
    public AircraftResponseDTO updateAircraft(@PathVariable Long id, @RequestBody AircraftUpdateRequestDTO request) {
        return aircraftService.updateAircraft(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
    }

    @GetMapping("/{id}")
    public AircraftResponseDTO getAircraft(@PathVariable Long id) {
        return aircraftService.getAircraft(id);
    }

    @GetMapping
    public List<AircraftResponseDTO> getAllAircrafts() {
        return aircraftService.getAllAircrafts();
    }
}
