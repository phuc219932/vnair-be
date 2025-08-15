package com.vnair.usermanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserRoleResponseDTO {
    
    private Long id;
    private Long userId;
    private String username;
    private String userEmail;
    private Long roleId;
    private String roleName;
    private String roleDescription;
    private String assignedBy;
    private Boolean isActive;
    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private String notes;
    private boolean expired;
    
    // Constructors
    public UserRoleResponseDTO() {}
    
    public UserRoleResponseDTO(Long id, Long userId, String username, String userEmail,
                              Long roleId, String roleName, String roleDescription,
                              String assignedBy, Boolean isActive, LocalDateTime assignedAt,
                              LocalDateTime updatedAt, LocalDateTime expiresAt, String notes) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
        this.assignedBy = assignedBy;
        this.isActive = isActive;
        this.assignedAt = assignedAt;
        this.updatedAt = updatedAt;
        this.expiresAt = expiresAt;
        this.notes = notes;
        this.expired = expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRoleDescription() {
        return roleDescription;
    }
    
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
    
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
    
    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
    
    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
        this.expired = expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public boolean isExpired() {
        return expired;
    }
    
    public void setExpired(boolean expired) {
        this.expired = expired;
    }
    
    public boolean isActiveAndNotExpired() {
        return isActive && !expired;
    }
    
    @Override
    public String toString() {
        return "UserRoleResponseDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", assignedBy='" + assignedBy + '\'' +
                ", isActive=" + isActive +
                ", assignedAt=" + assignedAt +
                ", expiresAt=" + expiresAt +
                ", expired=" + expired +
                '}';
    }
}
