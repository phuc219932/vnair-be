package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.UserRoleCreateRequestDTO;
import com.vnair.usermanagement.dto.UserRoleResponseDTO;
import com.vnair.usermanagement.dto.UserRoleUpdateRequestDTO;
import com.vnair.usermanagement.service.UserRoleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user-roles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserRoleController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRoleController.class);
    
    @Autowired
    private UserRoleService userRoleService;
    
    // Assign role to user
    @PostMapping
    public ResponseEntity<UserRoleResponseDTO> assignRoleToUser(@Valid @RequestBody UserRoleCreateRequestDTO userRoleCreateRequestDTO) {
        logger.info("POST /user-roles - Assigning role {} to user {}", 
                    userRoleCreateRequestDTO.getRoleId(), userRoleCreateRequestDTO.getUserId());
        
        UserRoleResponseDTO userRole = userRoleService.assignRoleToUser(userRoleCreateRequestDTO);
        return new ResponseEntity<>(userRole, HttpStatus.CREATED);
    }
    
    // Get UserRole by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserRoleResponseDTO> getUserRoleById(@PathVariable Long id) {
        logger.info("GET /user-roles/{} - Fetching UserRole by ID", id);
        
        UserRoleResponseDTO userRole = userRoleService.getUserRoleById(id);
        return ResponseEntity.ok(userRole);
    }
    
    // Get UserRole by user and role
    @GetMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<UserRoleResponseDTO> getUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        logger.info("GET /user-roles/users/{}/roles/{} - Fetching UserRole", userId, roleId);
        
        UserRoleResponseDTO userRole = userRoleService.getUserRole(userId, roleId);
        return ResponseEntity.ok(userRole);
    }
    
    // Get all UserRoles with pagination
    @GetMapping
    public ResponseEntity<Page<UserRoleResponseDTO>> getAllUserRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("GET /user-roles - Fetching all UserRoles with pagination");
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserRoleResponseDTO> userRoles = userRoleService.getAllUserRoles(pageable);
        
        return ResponseEntity.ok(userRoles);
    }
    
    // Update UserRole
    @PutMapping("/{id}")
    public ResponseEntity<UserRoleResponseDTO> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequestDTO userRoleUpdateRequestDTO) {
        
        logger.info("PUT /user-roles/{} - Updating UserRole", id);
        
        UserRoleResponseDTO updatedUserRole = userRoleService.updateUserRole(id, userRoleUpdateRequestDTO);
        return ResponseEntity.ok(updatedUserRole);
    }
    
    // Remove UserRole by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> removeUserRole(@PathVariable Long id) {
        logger.info("DELETE /user-roles/{} - Removing UserRole", id);
        
        userRoleService.removeUserRole(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "UserRole removed successfully");
        response.put("userRoleId", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Remove UserRole by user and role
    @DeleteMapping("/users/{userId}/roles/{roleId}")
    public ResponseEntity<Map<String, String>> removeUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        logger.info("DELETE /user-roles/users/{}/roles/{} - Removing UserRole", userId, roleId);
        
        userRoleService.removeUserRole(userId, roleId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Role removed from user successfully");
        response.put("userId", userId.toString());
        response.put("roleId", roleId.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Get all roles for a user
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<UserRoleResponseDTO>> getUserRoles(@PathVariable Long userId) {
        logger.info("GET /user-roles/users/{} - Fetching roles for user", userId);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getUserRoles(userId);
        return ResponseEntity.ok(userRoles);
    }
    
    // Get active roles for a user
    @GetMapping("/users/{userId}/active")
    public ResponseEntity<List<UserRoleResponseDTO>> getActiveUserRoles(@PathVariable Long userId) {
        logger.info("GET /user-roles/users/{}/active - Fetching active roles for user", userId);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getActiveUserRoles(userId);
        return ResponseEntity.ok(userRoles);
    }
    
    // Get user roles with pagination
    @GetMapping("/users/{userId}/paginated")
    public ResponseEntity<Page<UserRoleResponseDTO>> getUserRolesWithPagination(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "assignedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("GET /user-roles/users/{}/paginated - Fetching user roles with pagination", userId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserRoleResponseDTO> userRoles = userRoleService.getUserRolesWithPagination(userId, pageable);
        
        return ResponseEntity.ok(userRoles);
    }
    
    // Get all users for a role
    @GetMapping("/roles/{roleId}")
    public ResponseEntity<List<UserRoleResponseDTO>> getRoleUsers(@PathVariable Long roleId) {
        logger.info("GET /user-roles/roles/{} - Fetching users for role", roleId);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getRoleUsers(roleId);
        return ResponseEntity.ok(userRoles);
    }
    
    // Get active users for a role
    @GetMapping("/roles/{roleId}/active")
    public ResponseEntity<List<UserRoleResponseDTO>> getActiveRoleUsers(@PathVariable Long roleId) {
        logger.info("GET /user-roles/roles/{}/active - Fetching active users for role", roleId);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getActiveRoleUsers(roleId);
        return ResponseEntity.ok(userRoles);
    }
    
    // Get role users with pagination
    @GetMapping("/roles/{roleId}/paginated")
    public ResponseEntity<Page<UserRoleResponseDTO>> getRoleUsersWithPagination(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "assignedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("GET /user-roles/roles/{}/paginated - Fetching role users with pagination", roleId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserRoleResponseDTO> userRoles = userRoleService.getRoleUsersWithPagination(roleId, pageable);
        
        return ResponseEntity.ok(userRoles);
    }
    
    // Search UserRoles
    @GetMapping("/search")
    public ResponseEntity<Page<UserRoleResponseDTO>> searchUserRoles(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "assignedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("GET /user-roles/search - Searching UserRoles with keyword: {}", keyword);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserRoleResponseDTO> userRoles = userRoleService.searchUserRoles(keyword, pageable);
        
        return ResponseEntity.ok(userRoles);
    }
    
    // Get expired UserRoles
    @GetMapping("/expired")
    public ResponseEntity<List<UserRoleResponseDTO>> getExpiredUserRoles() {
        logger.info("GET /user-roles/expired - Fetching expired UserRoles");
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getExpiredUserRoles();
        return ResponseEntity.ok(userRoles);
    }
    
    // Get UserRoles expiring soon
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<UserRoleResponseDTO>> getUserRolesExpiringSoon(
            @RequestParam(defaultValue = "7") int daysAhead) {
        
        logger.info("GET /user-roles/expiring-soon - Fetching UserRoles expiring in {} days", daysAhead);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getUserRolesExpiringSoon(daysAhead);
        return ResponseEntity.ok(userRoles);
    }
    
    // Get UserRoles by assigned by
    @GetMapping("/assigned-by/{assignedBy}")
    public ResponseEntity<List<UserRoleResponseDTO>> getUserRolesByAssignedBy(@PathVariable String assignedBy) {
        logger.info("GET /user-roles/assigned-by/{} - Fetching UserRoles assigned by", assignedBy);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getUserRolesByAssignedBy(assignedBy);
        return ResponseEntity.ok(userRoles);
    }
    
    // Get user role history
    @GetMapping("/users/{userId}/history")
    public ResponseEntity<List<UserRoleResponseDTO>> getUserRoleHistory(@PathVariable Long userId) {
        logger.info("GET /user-roles/users/{}/history - Fetching role assignment history for user", userId);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getUserRoleHistory(userId);
        return ResponseEntity.ok(userRoles);
    }
    
    // Get role assignment history
    @GetMapping("/roles/{roleId}/history")
    public ResponseEntity<List<UserRoleResponseDTO>> getRoleAssignmentHistory(@PathVariable Long roleId) {
        logger.info("GET /user-roles/roles/{}/history - Fetching assignment history for role", roleId);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getRoleAssignmentHistory(roleId);
        return ResponseEntity.ok(userRoles);
    }
    
    // Check if user has role
    @GetMapping("/users/{userId}/roles/{roleId}/exists")
    public ResponseEntity<Map<String, Boolean>> hasUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
        logger.info("GET /user-roles/users/{}/roles/{}/exists - Checking if user has role", userId, roleId);
        
        boolean hasRole = userRoleService.hasUserRole(userId, roleId);
        boolean hasActiveRole = userRoleService.hasActiveUserRole(userId, roleId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasRole", hasRole);
        response.put("hasActiveRole", hasActiveRole);
        
        return ResponseEntity.ok(response);
    }
    
    // Check if user has role by name
    @GetMapping("/users/{userId}/roles/name/{roleName}/exists")
    public ResponseEntity<Map<String, Boolean>> userHasRole(@PathVariable Long userId, @PathVariable String roleName) {
        logger.info("GET /user-roles/users/{}/roles/name/{}/exists - Checking if user has role by name", userId, roleName);
        
        boolean hasRole = userRoleService.userHasRole(userId, roleName);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("hasRole", hasRole);
        
        return ResponseEntity.ok(response);
    }
    
    // Get UserRole statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserRoleStatistics() {
        logger.info("GET /user-roles/stats - Fetching UserRole statistics");
        
        Map<String, Long> statistics = userRoleService.getUserRoleStatistics();
        long activeCount = userRoleService.countActiveUserRoles();
        
        Map<String, Object> response = new HashMap<>();
        response.putAll(statistics);
        response.put("activeUserRoles", activeCount);
        
        return ResponseEntity.ok(response);
    }
    
    // Get count by role
    @GetMapping("/roles/{roleId}/count")
    public ResponseEntity<Map<String, Long>> countUsersByRole(@PathVariable Long roleId) {
        logger.info("GET /user-roles/roles/{}/count - Counting users for role", roleId);
        
        long count = userRoleService.countUsersByRole(roleId);
        
        Map<String, Long> response = new HashMap<>();
        response.put("userCount", count);
        response.put("roleId", roleId);
        
        return ResponseEntity.ok(response);
    }
    
    // Get count by user
    @GetMapping("/users/{userId}/count")
    public ResponseEntity<Map<String, Long>> countRolesByUser(@PathVariable Long userId) {
        logger.info("GET /user-roles/users/{}/count - Counting roles for user", userId);
        
        long count = userRoleService.countRolesByUser(userId);
        
        Map<String, Long> response = new HashMap<>();
        response.put("roleCount", count);
        response.put("userId", userId);
        
        return ResponseEntity.ok(response);
    }
    
    // Assign multiple roles to user
    @PostMapping("/users/{userId}/batch-assign")
    public ResponseEntity<List<UserRoleResponseDTO>> assignMultipleRolesToUser(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds,
            @RequestParam(required = false) String assignedBy) {
        
        logger.info("POST /user-roles/users/{}/batch-assign - Assigning multiple roles to user", userId);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.assignMultipleRolesToUser(userId, roleIds, assignedBy);
        return new ResponseEntity<>(userRoles, HttpStatus.CREATED);
    }
    
    // Remove multiple roles from user
    @PostMapping("/users/{userId}/batch-remove")
    public ResponseEntity<Map<String, String>> removeMultipleRolesFromUser(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds) {
        
        logger.info("POST /user-roles/users/{}/batch-remove - Removing multiple roles from user", userId);
        
        userRoleService.removeMultipleRolesFromUser(userId, roleIds);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Roles removed successfully");
        response.put("userId", userId.toString());
        response.put("roleCount", String.valueOf(roleIds.size()));
        
        return ResponseEntity.ok(response);
    }
    
    // Deactivate all user roles
    @PostMapping("/users/{userId}/deactivate-all")
    public ResponseEntity<Map<String, String>> deactivateAllUserRoles(@PathVariable Long userId) {
        logger.info("POST /user-roles/users/{}/deactivate-all - Deactivating all roles for user", userId);
        
        userRoleService.deactivateAllUserRoles(userId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "All user roles deactivated successfully");
        response.put("userId", userId.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Deactivate all role assignments
    @PostMapping("/roles/{roleId}/deactivate-all")
    public ResponseEntity<Map<String, String>> deactivateAllRoleAssignments(@PathVariable Long roleId) {
        logger.info("POST /user-roles/roles/{}/deactivate-all - Deactivating all assignments for role", roleId);
        
        userRoleService.deactivateAllRoleAssignments(roleId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "All role assignments deactivated successfully");
        response.put("roleId", roleId.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Deactivate expired UserRoles
    @PostMapping("/deactivate-expired")
    public ResponseEntity<Map<String, Object>> deactivateExpiredUserRoles() {
        logger.info("POST /user-roles/deactivate-expired - Deactivating expired UserRoles");
        
        int deactivatedCount = userRoleService.deactivateExpiredUserRoles();
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Expired UserRoles deactivated successfully");
        response.put("deactivatedCount", deactivatedCount);
        
        return ResponseEntity.ok(response);
    }
    
    // Set UserRole expiry
    @PutMapping("/{id}/expiry")
    public ResponseEntity<Map<String, String>> setUserRoleExpiry(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDate) {
        
        logger.info("PUT /user-roles/{}/expiry - Setting expiry for UserRole", id);
        
        userRoleService.setUserRoleExpiry(id, expiryDate);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "UserRole expiry set successfully");
        response.put("userRoleId", id.toString());
        response.put("expiryDate", expiryDate.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Remove UserRole expiry
    @DeleteMapping("/{id}/expiry")
    public ResponseEntity<Map<String, String>> removeUserRoleExpiry(@PathVariable Long id) {
        logger.info("DELETE /user-roles/{}/expiry - Removing expiry for UserRole", id);
        
        userRoleService.removeUserRoleExpiry(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "UserRole expiry removed successfully");
        response.put("userRoleId", id.toString());
        
        return ResponseEntity.ok(response);
    }
    
    // Reactivate UserRole
    @PostMapping("/{id}/reactivate")
    public ResponseEntity<UserRoleResponseDTO> reactivateUserRole(@PathVariable Long id) {
        logger.info("POST /user-roles/{}/reactivate - Reactivating UserRole", id);
        
        UserRoleResponseDTO userRole = userRoleService.reactivateUserRole(id);
        return ResponseEntity.ok(userRole);
    }
    
    // Get inactive user roles
    @GetMapping("/users/{userId}/inactive")
    public ResponseEntity<List<UserRoleResponseDTO>> getInactiveUserRoles(@PathVariable Long userId) {
        logger.info("GET /user-roles/users/{}/inactive - Fetching inactive roles for user", userId);
        
        List<UserRoleResponseDTO> userRoles = userRoleService.getInactiveUserRoles(userId);
        return ResponseEntity.ok(userRoles);
    }
    
    // Transfer user roles
    @PostMapping("/users/{fromUserId}/transfer-to/{toUserId}")
    public ResponseEntity<Map<String, String>> transferUserRoles(
            @PathVariable Long fromUserId,
            @PathVariable Long toUserId,
            @RequestParam(required = false) String transferredBy) {
        
        logger.info("POST /user-roles/users/{}/transfer-to/{} - Transferring roles", fromUserId, toUserId);
        
        userRoleService.transferUserRoles(fromUserId, toUserId, transferredBy);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User roles transferred successfully");
        response.put("fromUserId", fromUserId.toString());
        response.put("toUserId", toUserId.toString());
        
        return ResponseEntity.ok(response);
    }
}
