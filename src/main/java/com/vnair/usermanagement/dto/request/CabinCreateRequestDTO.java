
package com.vnair.usermanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CabinCreateRequestDTO {
    @NotBlank(message = "Tên khoang không được để trống")
    private String name;

    @NotBlank(message = "Vị trí khoang không được để trống")
    private String position;

    @NotNull(message = "Số lượng ghế không được để trống")
    @Positive(message = "Số lượng ghế phải lớn hơn 0")
    private Integer seatCount;

    private String description;

    @NotNull(message = "Mã máy bay không được để trống")
    private Long aircraftId;

    public Long getAircraftId() { return aircraftId; }
    public void setAircraftId(Long aircraftId) { this.aircraftId = aircraftId; }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
