package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.common.UserStatus;
import com.vnair.usermanagement.dto.UserRegistrationRequestDTO;
import com.vnair.usermanagement.dto.UserRegistrationResponseDTO;
import com.vnair.usermanagement.entity.User;
import com.vnair.usermanagement.exception.DuplicateUserException;
import com.vnair.usermanagement.repository.UserRepository;
import com.vnair.usermanagement.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller
 * 
 * Previous implementation: Used hardcoded authentication with hashcode
 * Current implementation: Uses username/password authentication from database
 * 
 * Changes made:
 * - Removed hardcoded user authentication (admin/admin)
 * - Now authenticates against User table in database
 * - Validates username and password hash stored in database
 * - Supports role-based authentication through UserRole relationships
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Xác thực", description = "API xác thực người dùng - đăng nhập, đăng ký, đăng xuất")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Login endpoint
     * 
     * Authenticates user against database instead of hardcoded credentials
     * - Fetches user from database by username
     * - Validates password hash using BCrypt
     * - Checks user status (ACTIVE, INACTIVE, SUSPENDED, DELETED)
     * - Returns JWT token for successful authentication
     */
    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", 
               description = "Xác thực người dùng và trả về JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Đăng nhập thành công",
                    content = @Content(mediaType = "application/json",
                               examples = @ExampleObject(value = "{\"accessToken\": \"eyJhbGciOiJIUzM4NCJ9...\"}"))),
        @ApiResponse(responseCode = "401", 
                    description = "Thông tin đăng nhập không chính xác",
                    content = @Content(mediaType = "application/json",
                               examples = @ExampleObject(value = "{\"error\": \"Invalid username or password\"}")))
    })
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Thông tin đăng nhập",
                content = @Content(
                    examples = @ExampleObject(
                        value = "{\"username\": \"admin\", \"password\": \"password123\"}"
                    )
                )
            )
            @RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        System.out.println("=== AuthController.login called");
        System.out.println("=== Username: " + username);
        System.out.println("=== Password length: " + (password != null ? password.length() : "null"));
        
        try {
            System.out.println("=== Attempting authentication...");
            // Database authentication instead of hardcode
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            
            System.out.println("=== Authentication successful!");
            System.out.println("=== Authenticated user: " + authentication.getName());
            System.out.println("=== Authorities: " + authentication.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtUtil.generateToken(userDetails.getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("=== Authentication failed with exception: " + e.getClass().getSimpleName());
            System.out.println("=== Exception message: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(error);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Đăng xuất", 
               description = "Xóa session và đăng xuất người dùng")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Đăng xuất thành công",
                    content = @Content(mediaType = "application/json",
                               examples = @ExampleObject(value = "{\"message\": \"Logged out successfully\"}")))
    })
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * User Registration endpoint
     * 
     * Validates and creates new user account with the following requirements:
     * - Email must be unique and valid format
     * - Phone number must be E.164 format and unique
     * - Password must be at least 8 characters with uppercase, lowercase, and numbers
     * - User must agree to terms and GDPR policy
     */
    @PostMapping("/register")
    @Operation(summary = "Đăng ký tài khoản mới", 
               description = "Tạo tài khoản người dùng mới với đầy đủ thông tin validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
                    description = "Đăng ký thành công",
                    content = @Content(mediaType = "application/json",
                               schema = @Schema(implementation = UserRegistrationResponseDTO.class))),
        @ApiResponse(responseCode = "409", 
                    description = "Thông tin đã tồn tại (username, email hoặc phone)",
                    content = @Content(mediaType = "application/json",
                               examples = @ExampleObject(value = "{\"error\": \"Username already exists\"}"))),
        @ApiResponse(responseCode = "400", 
                    description = "Dữ liệu không hợp lệ",
                    content = @Content(mediaType = "application/json",
                               examples = @ExampleObject(value = "{\"errors\": {\"email\": \"Email format is invalid\"}}")))
    })
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Thông tin đăng ký tài khoản",
                content = @Content(
                    schema = @Schema(implementation = UserRegistrationRequestDTO.class),
                    examples = @ExampleObject(
                        value = "{\n" +
                               "  \"username\": \"johndoe\",\n" +
                               "  \"email\": \"john@example.com\",\n" +
                               "  \"phone\": \"+84901234567\",\n" +
                               "  \"password\": \"Password123\",\n" +
                               "  \"fullName\": \"John Doe\",\n" +
                               "  \"countryRegion\": \"Vietnam\",\n" +
                               "  \"userRoleType\": \"INDIVIDUAL\",\n" +
                               "  \"agreedToTerms\": true\n" +
                               "}"
                    )
                )
            )
            @Valid @RequestBody UserRegistrationRequestDTO registrationRequest) {
        System.out.println("=== Registration request received");
        System.out.println("=== Username: " + registrationRequest.getUsername());
        System.out.println("=== Email: " + registrationRequest.getEmail());
        System.out.println("=== Phone: " + registrationRequest.getPhone());
        System.out.println("=== Full Name: " + registrationRequest.getFullName());
        System.out.println("=== Country/Region: " + registrationRequest.getCountryRegion());
        System.out.println("=== Agreed to Terms: " + registrationRequest.isAgreedToTerms());
        
        try {
            // Check if username already exists
            if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Username already exists");
                return ResponseEntity.status(409).body(error);
            }
            
            // Check if email already exists
            if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email already exists");
                return ResponseEntity.status(409).body(error);
            }
            
            // Check if phone already exists
            if (userRepository.findByPhone(registrationRequest.getPhone()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Phone number already exists");
                return ResponseEntity.status(409).body(error);
            }
            
            // Create new user
            User newUser = new User(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                registrationRequest.getPhone(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                registrationRequest.getFullName(),
                registrationRequest.getCountryRegion(),
                registrationRequest.isAgreedToTerms()
            );
            
            // Set optional fields
            newUser.setCompanyName(registrationRequest.getCompanyName());
            newUser.setTaxId(registrationRequest.getTaxId());
            newUser.setUserRoleType(registrationRequest.getUserRoleType());
            newUser.setStatus(UserStatus.ACTIVE);
            
            // Save user to database
            User savedUser = userRepository.save(newUser);
            
            System.out.println("=== User registration successful! User ID: " + savedUser.getId());
            
            // Create response
            UserRegistrationResponseDTO response = new UserRegistrationResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getFullName(),
                savedUser.getCountryRegion(),
                savedUser.getStatus(),
                savedUser.getCreatedAt(),
                "User registration successful"
            );
            
            response.setCompanyName(savedUser.getCompanyName());
            response.setTaxId(savedUser.getTaxId());
            response.setUserRoleType(savedUser.getUserRoleType());
            
            return ResponseEntity.status(201).body(response);
            
        } catch (Exception e) {
            System.out.println("=== Registration failed with exception: " + e.getClass().getSimpleName());
            System.out.println("=== Exception message: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
