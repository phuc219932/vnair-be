package com.vnair.usermanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/my/profile")
@Tag(name = "Profile", description = "User profile management APIs (Protected)")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    
    @GetMapping("/me")
    @Operation(
        summary = "Get current user profile", 
        description = "Get the profile of the currently authenticated user. Requires valid JWT token.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Access denied"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getCurrentUserProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            
            // Simple response for testing
            ProfileResponse response = new ProfileResponse();
            response.setUsername(username);
            response.setMessage("Hello " + username + "! This is a protected endpoint.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Internal server error", "An unexpected error occurred"));
        }
    }
    
    // Response class
    public static class ProfileResponse {
        private String username;
        private String message;
        
        // Getters and Setters
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    // Error response class
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
