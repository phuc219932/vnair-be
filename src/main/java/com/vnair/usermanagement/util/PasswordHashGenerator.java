package com.vnair.usermanagement.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String hashedPassword = encoder.encode(password);
        System.out.println("Password: " + password);
        System.out.println("Hashed: " + hashedPassword);
        
        // Test if they match
        boolean matches = encoder.matches(password, hashedPassword);
        System.out.println("Matches: " + matches);
        
        // Test against the current hash in init.sql
        String currentHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5lgHJo7QbqCpYpZ4Y2cm";
        boolean currentMatches = encoder.matches(password, currentHash);
        System.out.println("Current hash matches: " + currentMatches);
    }
}
