package com.vnair.usermanagement.exception;

public class AircraftNotFoundException extends RuntimeException {
    public AircraftNotFoundException(String message) {
        super(message);
    }
    
    public AircraftNotFoundException(Long id) {
        super("Aircraft not found with id: " + id);
    }
}
