package com.vnair.usermanagement.dto;

import com.vnair.usermanagement.entity.UserRoleType;
import jakarta.validation.constraints.*;

public class UserRegistrationRequestDTO {
    
    // Required fields
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format (e.g., +1234567890)")
    private String phone;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", 
             message = "Password must contain at least one uppercase letter, one lowercase letter, and one number")
    private String password;
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @NotBlank(message = "Country/Region is required")
    @Size(max = 50, message = "Country/Region cannot exceed 50 characters")
    private String countryRegion;
    
    @AssertTrue(message = "You must agree to the terms and GDPR policy")
    private boolean agreedToTerms;
    
    // Optional fields
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    private String companyName;
    
    @Size(max = 50, message = "Tax ID/Business ID cannot exceed 50 characters")
    private String taxId;
    
    private UserRoleType userRoleType;
    
    // Constructors
    public UserRegistrationRequestDTO() {}
    
    public UserRegistrationRequestDTO(String username, String email, String phone, String password, 
                                    String fullName, String countryRegion, boolean agreedToTerms) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fullName = fullName;
        this.countryRegion = countryRegion;
        this.agreedToTerms = agreedToTerms;
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
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getCountryRegion() {
        return countryRegion;
    }
    
    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }
    
    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }
    
    public void setAgreedToTerms(boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getTaxId() {
        return taxId;
    }
    
    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }
    
    public UserRoleType getUserRoleType() {
        return userRoleType;
    }
    
    public void setUserRoleType(UserRoleType userRoleType) {
        this.userRoleType = userRoleType;
    }
    
    @Override
    public String toString() {
        return "UserRegistrationRequestDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", fullName='" + fullName + '\'' +
                ", countryRegion='" + countryRegion + '\'' +
                ", companyName='" + companyName + '\'' +
                ", taxId='" + taxId + '\'' +
                ", userRoleType=" + userRoleType +
                ", agreedToTerms=" + agreedToTerms +
                '}';
    }
}
