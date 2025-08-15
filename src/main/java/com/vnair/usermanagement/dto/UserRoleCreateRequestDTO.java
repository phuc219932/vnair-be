package com.vnair.usermanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserRoleCreateRequestDTO {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Role ID is required")
    private Long roleId;
    
    @Size(max = 100, message = "Assigned by cannot exceed 100 characters")
    private String assignedBy;
    
    private LocalDateTime expiresAt;
    
    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
    
    private Boolean isActive = true;
    
    // Constructors
    public UserRoleCreateRequestDTO() {}
    
    public UserRoleCreateRequestDTO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
    
    public UserRoleCreateRequestDTO(Long userId, Long roleId, String assignedBy) {
        this.userId = userId;
        this.roleId = roleId;
        this.assignedBy = assignedBy;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public String getAssignedBy() {
        return assignedBy;
    }
    
    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
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
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return "UserRoleCreateRequestDTO{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", assignedBy='" + assignedBy + '\'' +
                ", expiresAt=" + expiresAt +
                ", notes='" + notes + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
