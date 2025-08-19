package com.vnair.usermanagement.service.impl;

import com.vnair.usermanagement.dto.UserCreateRequestDTO;
import com.vnair.usermanagement.dto.UserResponseDTO;
import com.vnair.usermanagement.dto.UserUpdateRequestDTO;
import com.vnair.usermanagement.entity.User;
import com.vnair.usermanagement.entity.UserStatus;
import com.vnair.usermanagement.exception.DuplicateUserException;
import com.vnair.usermanagement.exception.UserNotFoundException;
import com.vnair.usermanagement.repository.UserRepository;
import com.vnair.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Random;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Generate a random password with 6 characters
     */
    private String generateRandomPassword() {
        StringBuilder password = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
    
    @Override
    public UserResponseDTO createUser(UserCreateRequestDTO createRequest) {
        // log with context
        logger.info("UserServiceImpl: createUser - username: {}", createRequest.getUsername());
        
        // Check for duplicates
        if (userRepository.existsByUsername(createRequest.getUsername())) {
            throw new DuplicateUserException("Username already exists: " + createRequest.getUsername());
        }
        
        if (userRepository.existsByEmail(createRequest.getEmail())) {
            throw new DuplicateUserException("Email already exists: " + createRequest.getEmail());
        }
        
        // Only check phone duplicates if phone is provided and not empty
        if (createRequest.getPhone() != null && !createRequest.getPhone().trim().isEmpty() 
            && userRepository.existsByPhone(createRequest.getPhone())) {
            throw new DuplicateUserException("Phone number already exists: " + createRequest.getPhone());
        }
        
        // Handle password: generate if not provided
        String password = createRequest.getPassword();
        if (password == null || password.trim().isEmpty()) {
            password = generateRandomPassword();
            logger.info("Auto-generated password for user: {}", createRequest.getUsername());
        }
        
        // Create new user
        User user = new User();
        user.setUsername(createRequest.getUsername());
        user.setEmail(createRequest.getEmail());
        user.setPhone(createRequest.getPhone() != null ? createRequest.getPhone().trim() : "");
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setStatus(createRequest.getStatus() != null ? createRequest.getStatus() : UserStatus.ACTIVE);
        
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with ID: {} and password {}", savedUser.getId(), 
                   createRequest.getPassword() == null ? "auto-generated" : "provided");
        
        return mapToResponseDTO(savedUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToResponseDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username) {
        logger.info("Fetching user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return mapToResponseDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapToResponseDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        logger.info("Fetching all users with pagination: page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::mapToResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getUsersByStatus(UserStatus status, Pageable pageable) {
        logger.info("Fetching users by status: {} with pagination", status);
        Page<User> users = userRepository.findByStatus(status, pageable);
        return users.map(this::mapToResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> searchUsers(String keyword, Pageable pageable) {
        logger.info("Searching users with keyword: {}", keyword);
        Page<User> users = userRepository.findByUsernameContainingOrEmailContaining(keyword, pageable);
        return users.map(this::mapToResponseDTO);
    }
    
    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO updateRequest) {
        logger.info("Updating user with ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        // Check for duplicates when updating
        if (updateRequest.getUsername() != null && !updateRequest.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new DuplicateUserException("Username already exists: " + updateRequest.getUsername());
            }
            user.setUsername(updateRequest.getUsername());
        }
        
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new DuplicateUserException("Email already exists: " + updateRequest.getEmail());
            }
            user.setEmail(updateRequest.getEmail());
        }
        
        if (updateRequest.getPhone() != null && !updateRequest.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(updateRequest.getPhone())) {
                throw new DuplicateUserException("Phone number already exists: " + updateRequest.getPhone());
            }
            user.setPhone(updateRequest.getPhone());
        }
        
        if (updateRequest.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(updateRequest.getPassword()));
        }
        
        if (updateRequest.getStatus() != null) {
            user.setStatus(updateRequest.getStatus());
        }
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return mapToResponseDTO(updatedUser);
    }
    
    @Override
    public UserResponseDTO updateUserStatus(Long id, UserStatus status) {
        logger.info("Updating user status for ID: {} to {}", id, status);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        user.setStatus(status);
        User updatedUser = userRepository.save(user);
        
        return mapToResponseDTO(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        
        userRepository.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countUsersByStatus(UserStatus status) {
        return userRepository.countByStatus(status);
    }
    
    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
