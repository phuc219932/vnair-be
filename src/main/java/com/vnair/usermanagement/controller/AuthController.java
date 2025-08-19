package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.LoginRequestDTO;
import com.vnair.usermanagement.dto.LoginResponseDTO;
import com.vnair.usermanagement.service.AuthService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and JWT token management")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    @Operation(
        summary = "User Login", 
        description = "Authenticate user with username and password. Returns JWT token with 1 day expiration.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequestDTO.class),
                examples = @ExampleObject(
                    name = "Sample Login",
                    value = "{\n  \"username\": \"adminuser\",\n  \"password\": \"password123\"\n}"
                )
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login successful - Returns JWT token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponseDTO.class),
                examples = @ExampleObject(
                    name = "Successful Login",
                    value = "{\n  \"token\": \"eyJhbGciOiJIUzI1NiJ9...\",\n  \"type\": \"Bearer\",\n  \"username\": \"adminuser\",\n  \"userId\": 5,\n  \"expiresIn\": 86400\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Bad request - Invalid input or validation errors",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Validation Error",
                    value = "{\n  \"error\": \"Authentication failed\",\n  \"message\": \"Username is required\"\n}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Unauthorized - Invalid credentials or inactive user",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Invalid Credentials",
                    value = "{\n  \"error\": \"Authentication failed\",\n  \"message\": \"Invalid username or password\"\n}"
                )
            )
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Authentication failed", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Internal server error", "An unexpected error occurred"));
        }
    }
    
    // Inner class for error response
    public static class ErrorResponse {
        private String error;
        private String message;
        
        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
        
        // Getters
        public String getError() {
            return error;
        }
        
        public String getMessage() {
            return message;
        }
        
        // Setters
        public void setError(String error) {
            this.error = error;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
