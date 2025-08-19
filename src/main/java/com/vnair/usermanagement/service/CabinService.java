package com.vnair.usermanagement.service;

import com.vnair.usermanagement.dto.request.CabinCreateRequestDTO;
import com.vnair.usermanagement.dto.request.CabinUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.CabinResponseDTO;

import java.util.List;

public interface CabinService {
    CabinResponseDTO createCabin(CabinCreateRequestDTO request);
    CabinResponseDTO updateCabin(Long id, CabinUpdateRequestDTO request);
    void deleteCabin(Long id);
    CabinResponseDTO getCabin(Long id);
    List<CabinResponseDTO> getAllCabins();

    // Lấy danh sách cabin của một máy bay
    List<CabinResponseDTO> getCabinsByAircraft(Long aircraftId);
}
