package com.vnair.usermanagement.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserRoleUpdateRequestDTO {
    
    @Size(max = 100, message = "Assigned by cannot exceed 100 characters")
    private String assignedBy;
    
    private Boolean isActive;
    
    private LocalDateTime expiresAt;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    // Constructors
    public UserRoleUpdateRequestDTO() {}
    
    public UserRoleUpdateRequestDTO(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public UserRoleUpdateRequestDTO(Boolean isActive, LocalDateTime expiresAt) {
        this.isActive = isActive;
        this.expiresAt = expiresAt;
    }
    
    // Getters and Setters
    public String getAssignedBy() {
        return assignedBy;
    }
    
    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "UserRoleUpdateRequestDTO{" +
                "assignedBy='" + assignedBy + '\'' +
                ", isActive=" + isActive +
                ", expiresAt=" + expiresAt +
                ", notes='" + notes + '\'' +
                '}';
    }
}
