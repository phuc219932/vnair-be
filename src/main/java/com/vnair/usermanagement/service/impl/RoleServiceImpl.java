package com.vnair.usermanagement.service.impl;

import com.vnair.usermanagement.dto.RoleCreateRequestDTO;
import com.vnair.usermanagement.dto.RoleResponseDTO;
import com.vnair.usermanagement.dto.RoleUpdateRequestDTO;
import com.vnair.usermanagement.entity.Role;
import com.vnair.usermanagement.entity.User;
import com.vnair.usermanagement.entity.UserRole;
import com.vnair.usermanagement.exception.DuplicateUserException;
import com.vnair.usermanagement.exception.RoleNotFoundException;
import com.vnair.usermanagement.exception.UserNotFoundException;
import com.vnair.usermanagement.repository.RoleRepository;
import com.vnair.usermanagement.repository.UserRepository;
import com.vnair.usermanagement.repository.UserRoleRepository;
import com.vnair.usermanagement.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Override
    public RoleResponseDTO createRole(RoleCreateRequestDTO roleCreateRequestDTO) {
        logger.info("Creating new role with name: {}", roleCreateRequestDTO.getName());
        
        // Check if role name already exists
        if (roleRepository.existsByNameIgnoreCase(roleCreateRequestDTO.getName())) {
            throw new DuplicateUserException("Role with name '" + roleCreateRequestDTO.getName() + "' already exists");
        }
        
        Role role = new Role();
        role.setName(roleCreateRequestDTO.getName());
        role.setDescription(roleCreateRequestDTO.getDescription());
        role.setIsActive(roleCreateRequestDTO.getIsActive() != null ? roleCreateRequestDTO.getIsActive() : true);
        
        Role savedRole = roleRepository.save(role);
        logger.info("Role created successfully with ID: {}", savedRole.getId());
        
        return convertToResponseDTO(savedRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(Long id) {
        logger.info("Fetching role with ID: {}", id);
        
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + id));
        
        return convertToResponseDTO(role);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleByName(String name) {
        logger.info("Fetching role with name: {}", name);
        
        Role role = roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new UserNotFoundException("Role not found with name: " + name));
        
        return convertToResponseDTO(role);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponseDTO> getAllRoles(Pageable pageable) {
        logger.info("Fetching all roles with pagination: {}", pageable);
        
        Page<Role> roles = roleRepository.findAll(pageable);
        return roles.map(this::convertToResponseDTO);
    }
    
    @Override
    public RoleResponseDTO updateRole(Long id, RoleUpdateRequestDTO roleUpdateRequestDTO) {
        logger.info("Updating role with ID: {}", id);
        
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Role not found with ID: " + id));
        
        // Check if new name conflicts with existing roles (excluding current role)
        if (roleUpdateRequestDTO.getName() != null && 
            !roleUpdateRequestDTO.getName().equalsIgnoreCase(existingRole.getName()) &&
            roleRepository.existsByNameIgnoreCase(roleUpdateRequestDTO.getName())) {
            throw new DuplicateUserException("Role with name '" + roleUpdateRequestDTO.getName() + "' already exists");
        }
        
        // Update fields if provided
        if (roleUpdateRequestDTO.getName() != null) {
            existingRole.setName(roleUpdateRequestDTO.getName());
        }
        if (roleUpdateRequestDTO.getDescription() != null) {
            existingRole.setDescription(roleUpdateRequestDTO.getDescription());
        }
        if (roleUpdateRequestDTO.getIsActive() != null) {
            existingRole.setIsActive(roleUpdateRequestDTO.getIsActive());
        }
        
        Role updatedRole = roleRepository.save(existingRole);
        logger.info("Role updated successfully with ID: {}", updatedRole.getId());
        
        return convertToResponseDTO(updatedRole);
    }
    
    @Override
    public void deleteRole(Long id) {
        logger.info("Deleting role with ID: {}", id);
        
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + id));
        
        // Note: UserRole relationships will be handled by cascade delete if configured,
        // or should be handled by UserRoleService if needed
        
        roleRepository.delete(role);
        logger.info("Role deleted successfully with ID: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponseDTO> getRolesByIsActive(Boolean isActive, Pageable pageable) {
        logger.info("Fetching roles by isActive: {} with pagination: {}", isActive, pageable);
        
        Page<Role> roles = roleRepository.findByIsActive(isActive, pageable);
        return roles.map(this::convertToResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponseDTO> searchRoles(String keyword, Pageable pageable) {
        logger.info("Searching roles with keyword: {} and pagination: {}", keyword, pageable);
        
        Page<Role> roles = roleRepository.searchRoles(keyword, pageable);
        return roles.map(this::convertToResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getActiveRoles() {
        logger.info("Fetching all active roles");
        
        List<Role> roles = roleRepository.findActiveRoles();
        return roles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void assignRoleToUser(Long userId, Long roleId) {
        logger.info("Assigning role {} to user {}", roleId, userId);
        
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        // Validate role exists
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));
        
        // Check if assignment already exists
        if (userRoleRepository.findByUserIdAndRoleId(userId, roleId).isPresent()) {
            logger.warn("User {} already has role {}", userId, roleId);
            return;
        }
        
        // Create new UserRole assignment
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setIsActive(true);
        
        userRoleRepository.save(userRole);
        logger.info("Role {} assigned successfully to user {}", roleId, userId);
    }
    
    @Override
    public void removeRoleFromUser(Long userId, Long roleId) {
        logger.info("Removing role {} from user {}", roleId, userId);
        
        // Validate user and role exist
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException("Role not found with ID: " + roleId);
        }
        
        // Find and remove UserRole assignment
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new RoleNotFoundException("User " + userId + " does not have role " + roleId));
        
        userRoleRepository.delete(userRole);
        logger.info("Role {} removed successfully from user {}", roleId, userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getUserRoles(Long userId) {
        logger.info("Fetching roles for user: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        List<Role> roles = roleRepository.findRolesByUserId(userId);
        return roles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAvailableRolesForUser(Long userId) {
        logger.info("Fetching available roles for user: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        List<Role> roles = roleRepository.findRolesNotAssignedToUser(userId);
        return roles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countRolesByIsActive(Boolean isActive) {
        return roleRepository.countByIsActive(isActive);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getRoleStatistics() {
        logger.info("Fetching role statistics");
        
        List<Object[]> results = roleRepository.getRoleStatistics();
        Map<String, Long> statistics = new HashMap<>();
        
        // Initialize with default values
        statistics.put("active", 0L);
        statistics.put("inactive", 0L);
        
        // Fill with actual counts
        for (Object[] result : results) {
            Boolean isActive = (Boolean) result[0];
            Long count = (Long) result[1];
            if (isActive != null && isActive) {
                statistics.put("active", count);
            } else {
                statistics.put("inactive", count);
            }
        }
        
        return statistics;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return roleRepository.existsByNameIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndNotId(String name, Long id) {
        return roleRepository.findByNameIgnoreCase(name)
                .map(role -> !role.getId().equals(id))
                .orElse(false);
    }
    
    @Override
    public void assignMultipleRolesToUser(Long userId, List<Long> roleIds) {
        logger.info("Assigning multiple roles {} to user {}", roleIds, userId);
        
        // Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        for (Long roleId : roleIds) {
            // Validate role exists
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));
            
            // Check if assignment already exists
            if (userRoleRepository.findByUserIdAndRoleId(userId, roleId).isEmpty()) {
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);
                userRole.setIsActive(true);
                userRoleRepository.save(userRole);
            }
        }
        
        logger.info("Multiple roles assigned successfully to user {}", userId);
    }
    
    @Override
    public void removeMultipleRolesFromUser(Long userId, List<Long> roleIds) {
        logger.info("Removing multiple roles {} from user {}", roleIds, userId);
        
        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        for (Long roleId : roleIds) {
            // Validate role exists
            if (!roleRepository.existsById(roleId)) {
                throw new RoleNotFoundException("Role not found with ID: " + roleId);
            }
            
            // Find and remove UserRole assignment if exists
            userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                    .ifPresent(userRoleRepository::delete);
        }
        
        logger.info("Multiple roles removed successfully from user {}", userId);
    }
    
    // Helper method to convert Entity to ResponseDTO
    private RoleResponseDTO convertToResponseDTO(Role role) {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setIsActive(role.getIsActive());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());
        dto.setUserCount(role.getUserRoles() != null ? role.getUserRoles().size() : 0);
        
        return dto;
    }
}
