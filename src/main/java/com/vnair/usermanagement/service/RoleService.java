package com.vnair.usermanagement.service;

import com.vnair.usermanagement.dto.RoleCreateRequestDTO;
import com.vnair.usermanagement.dto.RoleResponseDTO;
import com.vnair.usermanagement.dto.RoleUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RoleService {
    
    // CRUD Operations
    RoleResponseDTO createRole(RoleCreateRequestDTO roleCreateRequestDTO);
    
    RoleResponseDTO getRoleById(Long id);
    
    RoleResponseDTO getRoleByName(String name);
    
    Page<RoleResponseDTO> getAllRoles(Pageable pageable);
    
    RoleResponseDTO updateRole(Long id, RoleUpdateRequestDTO roleUpdateRequestDTO);
    
    void deleteRole(Long id);
    
    // Search and Filter Operations
    Page<RoleResponseDTO> getRolesByIsActive(Boolean isActive, Pageable pageable);
    
    Page<RoleResponseDTO> searchRoles(String keyword, Pageable pageable);
    
    List<RoleResponseDTO> getActiveRoles();
    
    // Role-User Management
    void assignRoleToUser(Long userId, Long roleId);
    
    void removeRoleFromUser(Long userId, Long roleId);
    
    List<RoleResponseDTO> getUserRoles(Long userId);
    
    List<RoleResponseDTO> getAvailableRolesForUser(Long userId);
    
    // Statistics and Counts
    long countRolesByIsActive(Boolean isActive);
    
    Map<String, Long> getRoleStatistics();
    
    // Validation
    boolean existsByName(String name);
    
    boolean existsByNameAndNotId(String name, Long id);
    
    // Batch Operations
    void assignMultipleRolesToUser(Long userId, List<Long> roleIds);
    
    void removeMultipleRolesFromUser(Long userId, List<Long> roleIds);
}
