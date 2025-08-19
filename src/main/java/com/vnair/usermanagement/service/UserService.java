package com.vnair.usermanagement.service;

import com.vnair.usermanagement.dto.UserCreateRequestDTO;
import com.vnair.usermanagement.dto.UserResponseDTO;
import com.vnair.usermanagement.dto.UserUpdateRequestDTO;
import com.vnair.usermanagement.entity.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    
    UserResponseDTO createUser(UserCreateRequestDTO createRequest);
    
    UserResponseDTO getUserById(Long id);
    
    UserResponseDTO getUserByUsername(String username);
    
    UserResponseDTO getUserByEmail(String email);
    
    Page<UserResponseDTO> getAllUsers(Pageable pageable);
    
    Page<UserResponseDTO> getUsersByStatus(UserStatus status, Pageable pageable);
    
    Page<UserResponseDTO> searchUsers(String keyword, Pageable pageable);
    
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO updateRequest);
    
    UserResponseDTO updateUserStatus(Long id, UserStatus status);
    
    void deleteUser(Long id);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    long countUsersByStatus(UserStatus status);
}
