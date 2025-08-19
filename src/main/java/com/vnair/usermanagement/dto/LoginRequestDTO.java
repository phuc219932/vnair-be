package com.vnair.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request containing user credentials")
public class LoginRequestDTO {
    
    @NotBlank(message = "Username is required")
    @Schema(description = "Username for authentication", example = "adminuser", required = true)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "Password for authentication", example = "password123", required = true)
    private String password;
    
    // Constructors
    public LoginRequestDTO() {}
    
    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return "LoginRequestDTO{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}
