package com.vnair.usermanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.vnair.usermanagement.common.UserStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @Column(nullable = false)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format (e.g., +1234567890)")
    private String phone;
    
    @Column(name = "password_hash", nullable = false)
    @NotBlank(message = "Password is required")
    private String passwordHash;
    
    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @Column(name = "company_name")
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    private String companyName;
    
    @Column(name = "country_region", nullable = false)
    @NotBlank(message = "Country/Region is required")
    @Size(max = 50, message = "Country/Region cannot exceed 50 characters")
    private String countryRegion;
    
    @Column(name = "tax_id")
    @Size(max = 50, message = "Tax ID/Business ID cannot exceed 50 characters")
    private String taxId;
    
    @Column(name = "user_role_type")
    @Enumerated(EnumType.STRING)
    private UserRoleType userRoleType;
    
    @Column(name = "agreed_to_terms", nullable = false)
    private boolean agreedToTerms = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // One-to-many relationship with UserRole
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();
    
    // Constructors
    public User() {}
    
    public User(String username, String email, String phone, String passwordHash, String fullName, String countryRegion, boolean agreedToTerms) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.countryRegion = countryRegion;
        this.agreedToTerms = agreedToTerms;
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
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCountryRegion() {
        return countryRegion;
    }
    
    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
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
    
    public boolean isAgreedToTerms() {
        return agreedToTerms;
    }
    
    public void setAgreedToTerms(boolean agreedToTerms) {
        this.agreedToTerms = agreedToTerms;
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
    
    public Set<UserRole> getUserRoles() {
        return userRoles;
    }
    
    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    
    // Helper methods for role management
    public void addUserRole(UserRole userRole) {
        this.userRoles.add(userRole);
        userRole.setUser(this);
    }
    
    public void removeUserRole(UserRole userRole) {
        this.userRoles.remove(userRole);
        userRole.setUser(null);
    }
    
    public boolean hasRole(String roleName) {
        return this.userRoles.stream()
                .filter(ur -> ur.getIsActive() && !ur.isExpired())
                .anyMatch(ur -> ur.getRole().getName().equalsIgnoreCase(roleName));
    }
    
    public boolean hasActiveRole(Long roleId) {
        return this.userRoles.stream()
                .filter(ur -> ur.getIsActive() && !ur.isExpired())
                .anyMatch(ur -> ur.getRole().getId().equals(roleId));
    }
    
    public long getActiveRoleCount() {
        return userRoles.stream()
                .filter(ur -> ur.getIsActive() && !ur.isExpired())
                .count();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
