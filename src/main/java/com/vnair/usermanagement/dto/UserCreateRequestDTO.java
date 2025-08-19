package com.vnair.usermanagement.dto;

import com.vnair.usermanagement.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "<h2>📝 User Creation Request</h2>" +
        "<p>This DTO contains all the necessary information to create a new user account in the system.</p>" +
        "<h3>🚀 Key Features:</h3>" +
        "<ul>" +
        "<li>📧 <strong>Email & Username validation</strong> - Required fields with format checks</li>" +
        "<li>🤖 <strong>Auto-password generation</strong> - System creates secure passwords if not provided</li>" +
        "<li>📱 <strong>Flexible phone format</strong> - Supports international phone numbers</li>" +
        "<li>⚙️ <strong>Status management</strong> - Defaults to ACTIVE with multiple status options</li>" +
        "</ul>" +
        "<h3>⚡ Quick Start:</h3>" +
        "<p><em>Minimum required:</em> <code>{\"username\": \"john_doe\", \"email\": \"john@example.com\"}</code></p>")
public class UserCreateRequestDTO {
    
    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
    @Schema(description = "<h4>Username</h4>" +
            "<p><strong>Purpose:</strong> Unique identifier for the user account</p>" +
            "<p><strong>Validation Rules:</strong></p>" +
            "<ul>" +
            "<li>✅ Required field (cannot be null or empty)</li>" +
            "<li>📏 Length: 6-50 characters</li>" +
            "<li>🔤 Allowed characters: letters, numbers, underscore, hyphen</li>" +
            "<li>⚡ Must be unique across all users</li>" +
            "</ul>" +
            "<p><strong>Examples:</strong> <code>john_doe_2023</code>, <code>user-admin</code>, <code>testuser123</code></p>", 
            example = "john_doe_2023", 
            minLength = 5, 
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid format")
    @Schema(description = "<h4>Email Address</h4>" +
            "<p><strong>Purpose:</strong> Primary contact and login credential</p>" +
            "<p><strong>Validation Rules:</strong></p>" +
            "<ul>" +
            "<li>✅ Required field (cannot be null or empty)</li>" +
            "<li>📧 Must follow valid email format: <code>user@domain.com</code></li>" +
            "<li>🔍 Format validation: local-part@domain</li>" +
            "<li>⚡ Should be unique (recommended)</li>" +
            "</ul>" +
            "<p><strong>Valid Examples:</strong></p>" +
            "<ul>" +
            "<li><code>john.doe@example.com</code></li>" +
            "<li><code>user123@gmail.com</code></li>" +
            "<li><code>admin@company.co.uk</code></li>" +
            "</ul>",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "email")
    private String email;
    
    @Schema(description = "<h4>Phone Number</h4>" +
            "<p><strong>Purpose:</strong> Contact number for the user (optional)</p>" +
            "<p><strong>Validation Rules:</strong></p>" +
            "<ul>" +
            "<li>🔘 Optional field (can be empty or null)</li>" +
            "<li>📱 Flexible format support</li>" +
            "<li>🌍 International format accepted</li>" +
            "<li>🔢 Allowed characters: digits (0-9), +, (), -, spaces</li>" +
            "</ul>" +
            "<p><strong>Supported Formats:</strong></p>" +
            "<ul>" +
            "<li><code>+84-123-456-789</code> (International format)</li>" +
            "<li><code>(123) 456-7890</code> (US format)</li>" +
            "<li><code>123 456 7890</code> (Space separated)</li>" +
            "<li><code>1234567890</code> (Plain digits)</li>" +
            "<li><em>Empty string if not provided</em></li>" +
            "</ul>", 
            example = "+84-123-456-789",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            defaultValue = "")
    private String phone = "";
    
    @Schema(description = "<h4>Password</h4>" +
            "<p><strong>Purpose:</strong> Authentication credential for user login</p>" +
            "<p><strong>Auto-Generation Feature:</strong></p>" +
            "<ul>" +
            "<li>🔘 Optional field - can be left empty</li>" +
            "<li>🤖 <strong>Auto-generated if not provided</strong></li>" +
            "<li>🎲 System generates secure 6-character random password</li>" +
            "<li>📝 Generated password returned in response</li>" +
            "</ul>" +
            "<p><strong>Manual Password Rules:</strong></p>" +
            "<ul>" +
            "<li>🔒 Minimum 6 characters recommended</li>" +
            "<li>💪 Strong passwords encouraged</li>" +
            "<li>🔤 Mix of letters, numbers, symbols preferred</li>" +
            "</ul>" +
            "<p><strong>Examples:</strong></p>" +
            "<ul>" +
            "<li><code>mySecurePass123</code> (Manual)</li>" +
            "<li><code>P@ssw0rd!</code> (Manual)</li>" +
            "<li><em>Auto-generated: <code>aB3xK9</code></em></li>" +
            "</ul>", 
            example = "mySecurePass123",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            defaultValue = "Auto-generated 6-character string")
    private String password;
    
    @Schema(description = "<h4>User Status</h4>" +
            "<p><strong>Purpose:</strong> Determines user's access level and account state</p>" +
            "<p><strong>Default Behavior:</strong></p>" +
            "<ul>" +
            "<li>🔘 Optional field</li>" +
            "<li>✅ Defaults to <code>ACTIVE</code> if not specified</li>" +
            "<li>🎯 Controls user permissions and access</li>" +
            "</ul>" +
            "<p><strong>Available Status Options:</strong></p>" +
            "<table border='1' style='border-collapse:collapse; width:100%'>" +
            "<tr><th>Status</th><th>Description</th><th>Access Level</th></tr>" +
            "<tr><td><code>ACTIVE</code></td><td>✅ Full access to system</td><td>🟢 Complete</td></tr>" +
            "<tr><td><code>INACTIVE</code></td><td>⏸️ Temporarily disabled</td><td>🔴 Blocked</td></tr>" +
            "<tr><td><code>SUSPENDED</code></td><td>⚠️ Account under review</td><td>🟡 Limited</td></tr>" +
            "<tr><td><code>DELETED</code></td><td>🗑️ Marked for deletion</td><td>🔴 No access</td></tr>" +
            "</table>", 
            example = "ACTIVE",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            defaultValue = "ACTIVE",
            allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED", "DELETED"})
    private UserStatus status = UserStatus.ACTIVE;
    
    // Constructors
    public UserCreateRequestDTO() {}
    
    public UserCreateRequestDTO(String username, String email) {
        this.username = username;
        this.email = email;
        this.phone = "";
        this.password = null; // Will be auto-generated in service
        this.status = UserStatus.ACTIVE;
    }
    
    public UserCreateRequestDTO(String username, String email, String phone, String password) {
        this.username = username;
        this.email = email;
        this.phone = phone != null ? phone : "";
        this.password = password;
        this.status = UserStatus.ACTIVE;
    }
    
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
