package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.RoleCreateRequestDTO;
import com.vnair.usermanagement.dto.RoleResponseDTO;
import com.vnair.usermanagement.dto.RoleUpdateRequestDTO;
import com.vnair.usermanagement.service.RoleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    
    @Autowired
    private RoleService roleService;
    
    // Create new role
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleCreateRequestDTO roleCreateRequestDTO) {
        logger.info("POST /roles - Creating new role: {}", roleCreateRequestDTO.getName());
        
        RoleResponseDTO createdRole = roleService.createRole(roleCreateRequestDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }
    
    // Get role by ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        logger.info("GET /roles/{} - Fetching role by ID", id);
        
        RoleResponseDTO role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }
    
    // Get role by name
    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String name) {
        logger.info("GET /roles/name/{} - Fetching role by name", name);
        
        RoleResponseDTO role = roleService.getRoleByName(name);
        return ResponseEntity.ok(role);
    }
    
    // Get all roles with pagination and sorting
    @GetMapping
    public ResponseEntity<Page<RoleResponseDTO>> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("GET /roles - Fetching all roles with pagination: page={}, size={}, sortBy={}, sortDir={}", 
                    page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RoleResponseDTO> roles = roleService.getAllRoles(pageable);
        
        return ResponseEntity.ok(roles);
    }
    
    // Update role
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequestDTO roleUpdateRequestDTO) {
        
        logger.info("PUT /roles/{} - Updating role", id);
        
        RoleResponseDTO updatedRole = roleService.updateRole(id, roleUpdateRequestDTO);
        return ResponseEntity.ok(updatedRole);
    }
    
    // Delete role
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable Long id) {
        logger.info("DELETE /roles/{} - Deleting role", id);
        
        roleService.deleteRole(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Role deleted successfully");
        response.put("roleId", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Get roles by active status
    @GetMapping("/active/{isActive}")
    public ResponseEntity<Page<RoleResponseDTO>> getRolesByIsActive(
            @PathVariable Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("GET /roles/active/{} - Fetching roles by isActive", isActive);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RoleResponseDTO> roles = roleService.getRolesByIsActive(isActive, pageable);
        
        return ResponseEntity.ok(roles);
    }
    
    // Search roles
    @GetMapping("/search")
    public ResponseEntity<Page<RoleResponseDTO>> searchRoles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("GET /roles/search - Searching roles with keyword: {}", keyword);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RoleResponseDTO> roles = roleService.searchRoles(keyword, pageable);
        
        return ResponseEntity.ok(roles);
    }
    
    // Get active roles
    @GetMapping("/active")
    public ResponseEntity<List<RoleResponseDTO>> getActiveRoles() {
        logger.info("GET /roles/active - Fetching all active roles");
        
        List<RoleResponseDTO> roles = roleService.getActiveRoles();
        return ResponseEntity.ok(roles);
    }
    
    // Assign role to user
    @PostMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Map<String, String>> assignRoleToUser(
            @PathVariable Long roleId,
            @PathVariable Long userId) {
        
        logger.info("POST /roles/{}/users/{} - Assigning role to user", roleId, userId);
        
        roleService.assignRoleToUser(userId, roleId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Role assigned successfully");
        response.put("roleId", roleId.toString());
        response.put("userId", userId.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Remove role from user
    @DeleteMapping("/{roleId}/users/{userId}")
    public ResponseEntity<Map<String, String>> removeRoleFromUser(
            @PathVariable Long roleId,
            @PathVariable Long userId) {
        
        logger.info("DELETE /roles/{}/users/{} - Removing role from user", roleId, userId);
        
        roleService.removeRoleFromUser(userId, roleId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Role removed successfully");
        response.put("roleId", roleId.toString());
        response.put("userId", userId.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Get user roles
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<RoleResponseDTO>> getUserRoles(@PathVariable Long userId) {
        logger.info("GET /roles/users/{} - Fetching roles for user", userId);
        
        List<RoleResponseDTO> roles = roleService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }
    
    // Get available roles for user
    @GetMapping("/users/{userId}/available")
    public ResponseEntity<List<RoleResponseDTO>> getAvailableRolesForUser(@PathVariable Long userId) {
        logger.info("GET /roles/users/{}/available - Fetching available roles for user", userId);
        
        List<RoleResponseDTO> roles = roleService.getAvailableRolesForUser(userId);
        return ResponseEntity.ok(roles);
    }
    
    // Get role statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getRoleStatistics() {
        logger.info("GET /roles/stats - Fetching role statistics");
        
        Map<String, Long> statistics = roleService.getRoleStatistics();
        
        Map<String, Object> response = new HashMap<>();
        response.put("active", statistics.get("active"));
        response.put("inactive", statistics.get("inactive"));
        response.put("total", statistics.values().stream().mapToLong(Long::longValue).sum());
        
        return ResponseEntity.ok(response);
    }
    
    // Batch assign roles to user
    @PostMapping("/users/{userId}/batch-assign")
    public ResponseEntity<Map<String, String>> assignMultipleRolesToUser(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds) {
        
        logger.info("POST /roles/users/{}/batch-assign - Assigning multiple roles to user", userId);
        
        roleService.assignMultipleRolesToUser(userId, roleIds);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Roles assigned successfully");
        response.put("userId", userId.toString());
        response.put("roleCount", String.valueOf(roleIds.size()));
        
        return ResponseEntity.ok(response);
    }
    
    // Batch remove roles from user
    @PostMapping("/users/{userId}/batch-remove")
    public ResponseEntity<Map<String, String>> removeMultipleRolesFromUser(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds) {
        
        logger.info("POST /roles/users/{}/batch-remove - Removing multiple roles from user", userId);
        
        roleService.removeMultipleRolesFromUser(userId, roleIds);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Roles removed successfully");
        response.put("userId", userId.toString());
        response.put("roleCount", String.valueOf(roleIds.size()));
        
        return ResponseEntity.ok(response);
    }
}
