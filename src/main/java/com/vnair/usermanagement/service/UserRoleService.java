package com.vnair.usermanagement.service;

import com.vnair.usermanagement.dto.UserRoleCreateRequestDTO;
import com.vnair.usermanagement.dto.UserRoleResponseDTO;
import com.vnair.usermanagement.dto.UserRoleUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface UserRoleService {
    
    // CRUD Operations
    UserRoleResponseDTO assignRoleToUser(UserRoleCreateRequestDTO userRoleCreateRequestDTO);
    
    UserRoleResponseDTO getUserRoleById(Long id);
    
    UserRoleResponseDTO getUserRole(Long userId, Long roleId);
    
    Page<UserRoleResponseDTO> getAllUserRoles(Pageable pageable);
    
    UserRoleResponseDTO updateUserRole(Long id, UserRoleUpdateRequestDTO userRoleUpdateRequestDTO);
    
    void removeUserRole(Long id);
    
    void removeUserRole(Long userId, Long roleId);
    
    // User-specific operations
    List<UserRoleResponseDTO> getUserRoles(Long userId);
    
    List<UserRoleResponseDTO> getActiveUserRoles(Long userId);
    
    Page<UserRoleResponseDTO> getUserRolesWithPagination(Long userId, Pageable pageable);
    
    // Role-specific operations
    List<UserRoleResponseDTO> getRoleUsers(Long roleId);
    
    List<UserRoleResponseDTO> getActiveRoleUsers(Long roleId);
    
    Page<UserRoleResponseDTO> getRoleUsersWithPagination(Long roleId, Pageable pageable);
    
    // Search and Filter Operations
    Page<UserRoleResponseDTO> searchUserRoles(String keyword, Pageable pageable);
    
    List<UserRoleResponseDTO> getExpiredUserRoles();
    
    List<UserRoleResponseDTO> getUserRolesExpiringSoon(int daysAhead);
    
    List<UserRoleResponseDTO> getUserRolesByAssignedBy(String assignedBy);
    
    List<UserRoleResponseDTO> getUserRoleHistory(Long userId);
    
    List<UserRoleResponseDTO> getRoleAssignmentHistory(Long roleId);
    
    // Validation and Check Operations
    boolean hasUserRole(Long userId, Long roleId);
    
    boolean hasActiveUserRole(Long userId, Long roleId);
    
    boolean userHasRole(Long userId, String roleName);
    
    // Statistics and Counts
    long countActiveUserRoles();
    
    long countUsersByRole(Long roleId);
    
    long countRolesByUser(Long userId);
    
    Map<String, Long> getUserRoleStatistics();
    
    // Batch Operations
    List<UserRoleResponseDTO> assignMultipleRolesToUser(Long userId, List<Long> roleIds, String assignedBy);
    
    void removeMultipleRolesFromUser(Long userId, List<Long> roleIds);
    
    void deactivateAllUserRoles(Long userId);
    
    void deactivateAllRoleAssignments(Long roleId);
    
    // Maintenance Operations
    int deactivateExpiredUserRoles();
    
    void extendUserRoleExpiry(Long userRoleId, LocalDateTime newExpiryDate);
    
    void setUserRoleExpiry(Long userRoleId, LocalDateTime expiryDate);
    
    void removeUserRoleExpiry(Long userRoleId);
    
    // Advanced Operations
    UserRoleResponseDTO reactivateUserRole(Long id);
    
    List<UserRoleResponseDTO> getInactiveUserRoles(Long userId);
    
    void transferUserRoles(Long fromUserId, Long toUserId, String transferredBy);
}
