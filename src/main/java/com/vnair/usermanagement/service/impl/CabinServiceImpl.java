package com.vnair.usermanagement.service.impl;

import com.vnair.usermanagement.dto.request.CabinCreateRequestDTO;
import com.vnair.usermanagement.dto.request.CabinUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.CabinResponseDTO;
import com.vnair.usermanagement.entity.Cabin;
import com.vnair.usermanagement.common.CabinPosition;
import com.vnair.usermanagement.repository.CabinRepository;
import com.vnair.usermanagement.service.CabinService;
import com.vnair.usermanagement.entity.Aircraft;
import com.vnair.usermanagement.repository.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class CabinServiceImpl implements CabinService {
    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Override
    public CabinResponseDTO createCabin(CabinCreateRequestDTO request) {
    Cabin cabin = new Cabin();
    cabin.setName(request.getName());
    cabin.setPosition(CabinPosition.valueOf(request.getPosition()));
    cabin.setSeatCount(request.getSeatCount());
    cabin.setDescription(request.getDescription());
    // Liên kết cabin với máy bay
    Aircraft aircraft = aircraftRepository.findById(request.getAircraftId()).orElse(null);
    cabin.setAircraft(aircraft);
    cabin = cabinRepository.save(cabin);
    return toResponseDTO(cabin);
    }

    @Override
    public CabinResponseDTO updateCabin(Long id, CabinUpdateRequestDTO request) {
        Optional<Cabin> optionalCabin = cabinRepository.findById(id);
        if (optionalCabin.isPresent()) {
            Cabin cabin = optionalCabin.get();
            cabin.setName(request.getName());
            cabin.setPosition(CabinPosition.valueOf(request.getPosition()));
            cabin.setSeatCount(request.getSeatCount());
            cabin.setDescription(request.getDescription());
            // Cập nhật liên kết máy bay
            Aircraft aircraft = aircraftRepository.findById(request.getAircraftId()).orElse(null);
            cabin.setAircraft(aircraft);
            cabin = cabinRepository.save(cabin);
            return toResponseDTO(cabin);
        }
        return null;
    }

    @Override
    public void deleteCabin(Long id) {
        cabinRepository.deleteById(id);
    }

    @Override
    public CabinResponseDTO getCabin(Long id) {
        Optional<Cabin> optionalCabin = cabinRepository.findById(id);
        return optionalCabin.map(this::toResponseDTO).orElse(null);
    }

    @Override
    public List<CabinResponseDTO> getAllCabins() {
    return cabinRepository.findAll().stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());

    }

    @Override
    public List<CabinResponseDTO> getCabinsByAircraft(Long aircraftId) {
    return cabinRepository.findAll().stream()
        .filter(cabin -> cabin.getAircraft() != null && cabin.getAircraft().getId().equals(aircraftId))
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
    }

    private CabinResponseDTO toResponseDTO(Cabin cabin) {
        CabinResponseDTO dto = new CabinResponseDTO();
        dto.setId(cabin.getId());
        dto.setName(cabin.getName());
        dto.setPosition(cabin.getPosition().name());
        dto.setSeatCount(cabin.getSeatCount());
        dto.setDescription(cabin.getDescription());
        if (cabin.getAircraft() != null) {
            dto.setAircraftId(cabin.getAircraft().getId());
            dto.setAircraftCode(cabin.getAircraft().getCode());
            dto.setAircraftName(cabin.getAircraft().getName());
        }
        return dto;
    }
}
