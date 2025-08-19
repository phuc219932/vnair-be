
package com.vnair.usermanagement.entity;

import jakarta.persistence.Entity;
import com.vnair.usermanagement.common.CabinPosition;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cabin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Tên khoang

    @Enumerated(EnumType.STRING)
    private CabinPosition position; // Vị trí khoang: FRONT, MIDDLE, REAR

    private int seatCount; // Số lượng ghế trong khoang

    private String description; // Mô tả khoang

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft; // Máy bay chứa khoang này

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CabinPosition getPosition() { return position; }
    public void setPosition(CabinPosition position) { this.position = position; }

    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Aircraft getAircraft() { return aircraft; }
    public void setAircraft(Aircraft aircraft) { this.aircraft = aircraft; }
}
