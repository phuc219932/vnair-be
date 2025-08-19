package com.vnair.usermanagement.service;

import com.vnair.usermanagement.dto.LoginRequestDTO;
import com.vnair.usermanagement.dto.LoginResponseDTO;
import com.vnair.usermanagement.entity.User;
import com.vnair.usermanagement.entity.UserStatus;
import com.vnair.usermanagement.repository.UserRepository;
import com.vnair.usermanagement.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        // Find user by username
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }
        
        User user = userOptional.get();
        
        // Check if user is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("User account is not active");
        }
        
        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        // Generate JWT token
        String token = jwtUtils.generateJwtToken(user.getUsername(), user.getId());
        
        // Calculate expiration time in seconds
        long expiresIn = jwtUtils.getJwtExpirationMs() / 1000;
        
        return new LoginResponseDTO(token, user.getUsername(), user.getId(), expiresIn);
    }
}
