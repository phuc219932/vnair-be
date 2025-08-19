package com.vnair.usermanagement.dto.response;

public class CabinResponseDTO {
    private Long id;
    private String name;
    private String position;
    private int seatCount;
    private String description;

    private Long aircraftId;
    private String aircraftCode;
    private String aircraftName;

    public Long getAircraftId() { return aircraftId; }
    public void setAircraftId(Long aircraftId) { this.aircraftId = aircraftId; }
    public String getAircraftCode() { return aircraftCode; }
    public void setAircraftCode(String aircraftCode) { this.aircraftCode = aircraftCode; }
    public String getAircraftName() { return aircraftName; }
    public void setAircraftName(String aircraftName) { this.aircraftName = aircraftName; }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
