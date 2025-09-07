package com.vnair.usermanagement.service.impl;

import com.vnair.usermanagement.dto.request.AircraftCreateRequestDTO;
import com.vnair.usermanagement.dto.request.AircraftPageRequestDTO;
import com.vnair.usermanagement.dto.request.AircraftUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.AircraftResponseDTO;
import com.vnair.usermanagement.dto.response.PageResponseDTO;
import com.vnair.usermanagement.entity.Aircraft;
import com.vnair.usermanagement.entity.Cabin;
import com.vnair.usermanagement.exception.AircraftNotFoundException;
import com.vnair.usermanagement.repository.AircraftRepository;
import com.vnair.usermanagement.service.AircraftService;
import com.vnair.usermanagement.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException(id));
        
        aircraft.setCode(request.getCode());
        aircraft.setName(request.getName());
        aircraft = aircraftRepository.save(aircraft);
        return toResponseDTO(aircraft);
    }

    @Override
    public void deleteAircraft(Long id) {
        if (!aircraftRepository.existsById(id)) {
            throw new AircraftNotFoundException(id);
        }
        aircraftRepository.deleteById(id);
    }

    @Override
    public AircraftResponseDTO getAircraft(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException(id));
        return toResponseDTO(aircraft);
    }

    @Override
    public List<AircraftResponseDTO> getAllAircrafts() {
        return aircraftRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<AircraftResponseDTO> getAllAircraftsWithPagination(AircraftPageRequestDTO request) {
        Pageable pageable = PaginationUtil.createPageable(request);
        Page<Aircraft> aircraftPage = aircraftRepository.findAll(pageable);
        
        List<AircraftResponseDTO> content = aircraftPage.getContent().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        
        return PaginationUtil.createPageResponse(aircraftPage, content);
    }

    @Override
    public PageResponseDTO<AircraftResponseDTO> searchAircrafts(AircraftPageRequestDTO request) {
        Pageable pageable = PaginationUtil.createPageable(request);
        Page<Aircraft> aircraftPage = aircraftRepository.findByKeyword(request.getSearchKeyword(), pageable);
        
        List<AircraftResponseDTO> content = aircraftPage.getContent().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        
        return PaginationUtil.createPageResponse(aircraftPage, content);
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
