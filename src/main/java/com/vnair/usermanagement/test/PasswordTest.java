package com.vnair.usermanagement.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hash for known password
        String password = "password123";
        String newHash = encoder.encode(password);
        System.out.println("New BCrypt hash for 'password123': " + newHash);
        
        // Test the new hash
        boolean matches = encoder.matches(password, newHash);
        System.out.println("New hash matches 'password123': " + matches);
        
        // Also generate hash for 'admin'
        String adminHash = encoder.encode("admin");
        System.out.println("BCrypt hash for 'admin': " + adminHash);
    }
}
