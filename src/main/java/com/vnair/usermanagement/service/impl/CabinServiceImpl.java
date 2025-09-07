package com.vnair.usermanagement.service.impl;

import com.vnair.usermanagement.dto.request.CabinCreateRequestDTO;
import com.vnair.usermanagement.dto.request.CabinPageRequestDTO;
import com.vnair.usermanagement.dto.request.CabinUpdateRequestDTO;
import com.vnair.usermanagement.dto.response.CabinResponseDTO;
import com.vnair.usermanagement.dto.response.PageResponseDTO;
import com.vnair.usermanagement.entity.Cabin;
import com.vnair.usermanagement.common.CabinPosition;
import com.vnair.usermanagement.exception.AircraftNotFoundException;
import com.vnair.usermanagement.exception.CabinNotFoundException;
import com.vnair.usermanagement.repository.CabinRepository;
import com.vnair.usermanagement.service.CabinService;
import com.vnair.usermanagement.entity.Aircraft;
import com.vnair.usermanagement.repository.AircraftRepository;
import com.vnair.usermanagement.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        Aircraft aircraft = aircraftRepository.findById(request.getAircraftId())
                .orElseThrow(() -> new AircraftNotFoundException(request.getAircraftId()));
        cabin.setAircraft(aircraft);
        
        cabin = cabinRepository.save(cabin);
        return toResponseDTO(cabin);
    }

    @Override
    public CabinResponseDTO updateCabin(Long id, CabinUpdateRequestDTO request) {
        Cabin cabin = cabinRepository.findById(id)
                .orElseThrow(() -> new CabinNotFoundException(id));
        
        cabin.setName(request.getName());
        cabin.setPosition(CabinPosition.valueOf(request.getPosition()));
        cabin.setSeatCount(request.getSeatCount());
        cabin.setDescription(request.getDescription());
        
        // Cập nhật liên kết máy bay
        Aircraft aircraft = aircraftRepository.findById(request.getAircraftId())
                .orElseThrow(() -> new AircraftNotFoundException(request.getAircraftId()));
        cabin.setAircraft(aircraft);
        
        cabin = cabinRepository.save(cabin);
        return toResponseDTO(cabin);
    }

    @Override
    public void deleteCabin(Long id) {
        if (!cabinRepository.existsById(id)) {
            throw new CabinNotFoundException(id);
        }
        cabinRepository.deleteById(id);
    }

    @Override
    public CabinResponseDTO getCabin(Long id) {
        Cabin cabin = cabinRepository.findById(id)
                .orElseThrow(() -> new CabinNotFoundException(id));
        return toResponseDTO(cabin);
    }

    @Override
    public List<CabinResponseDTO> getAllCabins() {
        return cabinRepository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<CabinResponseDTO> getCabinsByAircraft(Long aircraftId) {
        return cabinRepository.findByAircraftId(aircraftId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponseDTO<CabinResponseDTO> getAllCabinsWithPagination(CabinPageRequestDTO request) {
        Pageable pageable = PaginationUtil.createPageable(request);
        Page<Cabin> cabinPage = cabinRepository.findAll(pageable);
        
        List<CabinResponseDTO> content = cabinPage.getContent().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        
        return PaginationUtil.createPageResponse(cabinPage, content);
    }

    @Override
    public PageResponseDTO<CabinResponseDTO> searchCabins(CabinPageRequestDTO request) {
        Pageable pageable = PaginationUtil.createPageable(request);
        CabinPosition position = request.getPosition() != null ? CabinPosition.valueOf(request.getPosition()) : null;
        
        Page<Cabin> cabinPage = cabinRepository.findByKeywordAndAircraftIdAndPosition(
            request.getSearchKeyword(), 
            request.getAircraftId(), 
            position, 
            pageable
        );
        
        List<CabinResponseDTO> content = cabinPage.getContent().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        
        return PaginationUtil.createPageResponse(cabinPage, content);
    }

    @Override
    public PageResponseDTO<CabinResponseDTO> getCabinsByAircraftWithPagination(Long aircraftId, CabinPageRequestDTO request) {
        Pageable pageable = PaginationUtil.createPageable(request);
        Page<Cabin> cabinPage = cabinRepository.findByAircraftId(aircraftId, pageable);
        
        List<CabinResponseDTO> content = cabinPage.getContent().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        
        return PaginationUtil.createPageResponse(cabinPage, content);
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
