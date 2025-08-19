package com.vnair.usermanagement.dto.response;

import java.util.List;

public class AircraftResponseDTO {
    private Long id;
    private String code;
    private String name;
    private List<Long> cabinIds;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Long> getCabinIds() { return cabinIds; }
    public void setCabinIds(List<Long> cabinIds) { this.cabinIds = cabinIds; }
}
