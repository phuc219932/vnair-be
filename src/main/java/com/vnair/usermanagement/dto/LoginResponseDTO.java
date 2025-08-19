package com.vnair.usermanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login response containing JWT token and user information")
public class LoginResponseDTO {
    
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbnVzZXIiLCJ1c2VySWQiOjUsImlhdCI6MTc1NTI3MjIzNiwiZXhwIjoxNzU1MzU4NjM2fQ.8pUGYYMOCU1QiIAzsxQtbSmqKWc9GpNkltRsT55R8W0")
    private String token;
    
    @Schema(description = "Token type", example = "Bearer", defaultValue = "Bearer")
    private String type = "Bearer";
    
    @Schema(description = "Username of authenticated user", example = "adminuser")
    private String username;
    
    @Schema(description = "User ID", example = "5")
    private Long userId;
    
    @Schema(description = "Token expiration time in seconds", example = "86400")
    private long expiresIn; // in seconds
    
    // Constructors
    public LoginResponseDTO() {}
    
    public LoginResponseDTO(String token, String username, Long userId, long expiresIn) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.expiresIn = expiresIn;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public long getExpiresIn() {
        return expiresIn;
    }
    
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "token='[PROTECTED]'" +
                ", type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", userId=" + userId +
                ", expiresIn=" + expiresIn +
                '}';
    }
}
