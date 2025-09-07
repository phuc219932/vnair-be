package com.vnair.usermanagement.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AircraftUpdateRequestDTO {
    @NotBlank(message = "Mã máy bay không được để trống")
    private String code;
    
    @NotBlank(message = "Tên máy bay không được để trống")
    private String name;

    // Getters & Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
