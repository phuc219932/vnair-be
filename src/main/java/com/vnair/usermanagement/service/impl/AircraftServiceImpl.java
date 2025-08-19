package com.vnair.usermanagement.service.impl;

import com.vnair.usermanagement.dto.request.AircraftCreateRequestDTO;
import com.vnair.usermanagement.dto.request.AircraftUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.AircraftResponseDTO;
import com.vnair.usermanagement.entity.Aircraft;
import com.vnair.usermanagement.entity.Cabin;
import com.vnair.usermanagement.repository.AircraftRepository;
import com.vnair.usermanagement.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AircraftServiceImpl implements AircraftService {
    @Autowired
    private AircraftRepository aircraftRepository;

    @Override
    public AircraftResponseDTO createAircraft(AircraftCreateRequestDTO request) {
        Aircraft aircraft = new Aircraft();
        aircraft.setCode(request.getCode());
        aircraft.setName(request.getName());
        aircraft = aircraftRepository.save(aircraft);
        return toResponseDTO(aircraft);
    }

    @Override
    public AircraftResponseDTO updateAircraft(Long id, AircraftUpdateRequestDTO request) {
        Optional<Aircraft> optionalAircraft = aircraftRepository.findById(id);
        if (optionalAircraft.isPresent()) {
            Aircraft aircraft = optionalAircraft.get();
            aircraft.setCode(request.getCode());
            aircraft.setName(request.getName());
            aircraft = aircraftRepository.save(aircraft);
            return toResponseDTO(aircraft);
        }
        return null;
    }

    @Override
    public void deleteAircraft(Long id) {
        aircraftRepository.deleteById(id);
    }

    @Override
    public AircraftResponseDTO getAircraft(Long id) {
        Optional<Aircraft> optionalAircraft = aircraftRepository.findById(id);
        return optionalAircraft.map(this::toResponseDTO).orElse(null);
    }

    @Override
    public List<AircraftResponseDTO> getAllAircrafts() {
        return aircraftRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private AircraftResponseDTO toResponseDTO(Aircraft aircraft) {
        AircraftResponseDTO dto = new AircraftResponseDTO();
        dto.setId(aircraft.getId());
        dto.setCode(aircraft.getCode());
        dto.setName(aircraft.getName());
        if (aircraft.getCabins() != null) {
            List<Long> cabinIds = aircraft.getCabins().stream()
                    .map(Cabin::getId)
                    .collect(Collectors.toList());
            dto.setCabinIds(cabinIds);
        }
        return dto;
    }
}
