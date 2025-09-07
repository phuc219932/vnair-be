package com.vnair.usermanagement.exception;

public class CabinNotFoundException extends RuntimeException {
    public CabinNotFoundException(String message) {
        super(message);
    }
    
    public CabinNotFoundException(Long id) {
        super("Cabin not found with id: " + id);
    }
}
