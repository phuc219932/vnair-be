package com.vnair.usermanagement.dto;

import com.vnair.usermanagement.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "<h2>✏️ User Update Request</h2>" +
        "<p>Modify existing user information. All fields are optional - only provided fields will be updated.</p>" +
        "<h3>🎯 Features:</h3>" +
        "<ul>" +
        "<li>🔄 <strong>Partial Updates</strong> - Update only what you need</li>" +
        "<li>✅ <strong>Validation</strong> - Same rules as creation apply</li>" +
        "<li>🔒 <strong>Secure</strong> - ID-based targeting, safe updates</li>" +
        "</ul>" +
        "<p><em>💡 Tip: Send only the fields you want to change!</em></p>")
public class UserUpdateRequestDTO {
    
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "<h4>👤 Username</h4>" +
            "<p><strong>Optional:</strong> New username for the account</p>" +
            "<ul>" +
            "<li>📏 Length: 3-50 characters</li>" +
            "<li>🔤 Same format rules as creation</li>" +
            "<li>⚠️ Must be unique if changed</li>" +
            "</ul>", 
            example = "new_username", 
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String username;
    
    @Email(message = "Email should be valid")
    @Schema(description = "<h4>📧 Email Address</h4>" +
            "<p><strong>Optional:</strong> New email for contact and login</p>" +
            "<ul>" +
            "<li>✉️ Must be valid email format</li>" +
            "<li>🔄 Updates primary contact</li>" +
            "<li>⚠️ Should be unique if changed</li>" +
            "</ul>", 
            example = "newemail@example.com", 
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String email;
    
    @Schema(description = "<h4>📱 Phone Number</h4>" +
            "<p><strong>Optional:</strong> Update contact phone number</p>" +
            "<ul>" +
            "<li>🌍 International formats supported</li>" +
            "<li>🔘 Can be set to empty to remove</li>" +
            "<li>📞 Same format flexibility as creation</li>" +
            "</ul>", 
            example = "+1-555-123-4567", 
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String phone;
    
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "<h4>🔒 Password</h4>" +
            "<p><strong>Optional:</strong> New password for account security</p>" +
            "<ul>" +
            "<li>🔐 Minimum 6 characters required</li>" +
            "<li>💪 Strong passwords recommended</li>" +
            "<li>🔄 Replaces current password</li>" +
            "</ul>", 
            example = "newSecurePass123", 
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String password;
    
    @Schema(description = "<h4>📊 User Status</h4>" +
            "<p><strong>Optional:</strong> Change user's account status</p>" +
            "<ul>" +
            "<li>✅ <code>ACTIVE</code> - Enable full access</li>" +
            "<li>⏸️ <code>INACTIVE</code> - Temporarily disable</li>" +
            "<li>⚠️ <code>SUSPENDED</code> - Account under review</li>" +
            "<li>🗑️ <code>DELETED</code> - Mark for deletion</li>" +
            "</ul>", 
            example = "ACTIVE", 
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "DELETED"})
    private UserStatus status;
    
    // Constructors
    public UserUpdateRequestDTO() {}
    
    // Getters and Setters
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public UserStatus getStatus() {
        return status;
    }
    
    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
