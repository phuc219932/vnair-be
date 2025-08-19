package com.vnair.usermanagement.dto;

import com.vnair.usermanagement.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "<h2>👤 User Information Response</h2>" +
        "<p>Complete user profile information returned by the API after successful operations.</p>" +
        "<h3>📊 Contains:</h3>" +
        "<ul>" +
        "<li>🆔 <strong>System-generated ID</strong> - Unique database identifier</li>" +
        "<li>👤 <strong>Profile details</strong> - Username, email, phone</li>" +
        "<li>📊 <strong>Account status</strong> - Current user state</li>" +
        "<li>⏰ <strong>Timestamps</strong> - Creation and last update times</li>" +
        "</ul>" +
        "<p><em>🔒 Note: Passwords are never included in responses for security.</em></p>")
public class UserResponseDTO {
    
    @Schema(description = "<h4>🆔 User ID</h4>" +
            "<p><strong>Database identifier:</strong> Auto-generated unique number</p>" +
            "<ul><li>🔢 System-generated sequential ID</li><li>🎯 Used for all API operations</li></ul>", 
            example = "1")
    private Long id;
    
    @Schema(description = "<h4>👤 Username</h4>" +
            "<p><strong>User's chosen display name:</strong> Unique identifier for login</p>" +
            "<ul><li>📝 6-50 characters</li><li>🔑 Primary login credential</li></ul>", 
            example = "john_doe")
    private String username;
    
    @Schema(description = "<h4>📧 Email Address</h4>" +
            "<p><strong>Contact email:</strong> Primary communication channel</p>" +
            "<ul><li>✉️ Valid email format</li><li>🔐 Alternative login method</li></ul>", 
            example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "<h4>📱 Phone Number</h4>" +
            "<p><strong>Contact number:</strong> Optional communication method</p>" +
            "<ul><li>🌍 International format supported</li><li>🔘 Can be empty if not provided</li></ul>", 
            example = "+84-123-456-789")
    private String phone;
    
    @Schema(description = "<h4>📊 Account Status</h4>" +
            "<p><strong>Current user state:</strong> Determines access permissions</p>" +
            "<ul>" +
            "<li>✅ <code>ACTIVE</code> - Full access</li>" +
            "<li>⏸️ <code>INACTIVE</code> - Disabled</li>" +
            "<li>⚠️ <code>SUSPENDED</code> - Under review</li>" +
            "<li>🗑️ <code>DELETED</code> - Marked for deletion</li>" +
            "</ul>", 
            example = "ACTIVE")
    private UserStatus status;
    
    @Schema(description = "<h4>⏰ Creation Time</h4>" +
            "<p><strong>When user was created:</strong> System timestamp</p>" +
            "<ul><li>🕒 ISO 8601 format</li><li>📅 Timezone: Server local time</li></ul>", 
            example = "2023-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "<h4>🔄 Last Updated</h4>" +
            "<p><strong>When user was modified:</strong> Most recent change timestamp</p>" +
            "<ul><li>📝 Updates on any profile change</li><li>⚡ Helps track user activity</li></ul>", 
            example = "2023-01-16T14:45:00")
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserResponseDTO() {}
    
    public UserResponseDTO(Long id, String username, String email, String phone, 
                          UserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
