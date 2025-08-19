package com.vnair.usermanagement.service;

import com.vnair.usermanagement.dto.request.AircraftCreateRequestDTO;
import com.vnair.usermanagement.dto.request.AircraftUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.AircraftResponseDTO;

import java.util.List;

public interface AircraftService {
    AircraftResponseDTO createAircraft(AircraftCreateRequestDTO request);
    AircraftResponseDTO updateAircraft(Long id, AircraftUpdateRequestDTO request);
    void deleteAircraft(Long id);
    AircraftResponseDTO getAircraft(Long id);
    List<AircraftResponseDTO> getAllAircrafts();
}
