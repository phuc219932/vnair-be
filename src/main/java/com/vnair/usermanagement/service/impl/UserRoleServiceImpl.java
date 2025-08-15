package com.vnair.usermanagement.service.impl;

import com.vnair.usermanagement.dto.UserRoleCreateRequestDTO;
import com.vnair.usermanagement.dto.UserRoleResponseDTO;
import com.vnair.usermanagement.dto.UserRoleUpdateRequestDTO;
import com.vnair.usermanagement.entity.Role;
import com.vnair.usermanagement.entity.User;
import com.vnair.usermanagement.entity.UserRole;
import com.vnair.usermanagement.exception.DuplicateUserException;
import com.vnair.usermanagement.exception.RoleNotFoundException;
import com.vnair.usermanagement.exception.UserNotFoundException;
import com.vnair.usermanagement.repository.RoleRepository;
import com.vnair.usermanagement.repository.UserRepository;
import com.vnair.usermanagement.repository.UserRoleRepository;
import com.vnair.usermanagement.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRoleServiceImpl.class);
    
    @Autowired
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Override
    public UserRoleResponseDTO assignRoleToUser(UserRoleCreateRequestDTO userRoleCreateRequestDTO) {
        logger.info("Assigning role {} to user {}", 
                    userRoleCreateRequestDTO.getRoleId(), userRoleCreateRequestDTO.getUserId());
        
        // Validate user exists
        User user = userRepository.findById(userRoleCreateRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userRoleCreateRequestDTO.getUserId()));
        
        // Validate role exists
        Role role = roleRepository.findById(userRoleCreateRequestDTO.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + userRoleCreateRequestDTO.getRoleId()));
        
        // Check if user already has this role
        if (userRoleRepository.existsByUserIdAndRoleId(user.getId(), role.getId())) {
            throw new DuplicateUserException("User already has this role assigned");
        }
        
        // Create UserRole
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setAssignedBy(userRoleCreateRequestDTO.getAssignedBy());
        userRole.setIsActive(userRoleCreateRequestDTO.getIsActive());
        userRole.setExpiresAt(userRoleCreateRequestDTO.getExpiresAt());
        userRole.setNotes(userRoleCreateRequestDTO.getNotes());
        
        UserRole savedUserRole = userRoleRepository.save(userRole);
        logger.info("Role {} assigned successfully to user {} with ID: {}", 
                    role.getId(), user.getId(), savedUserRole.getId());
        
        return convertToResponseDTO(savedUserRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserRoleResponseDTO getUserRoleById(Long id) {
        logger.info("Fetching UserRole with ID: {}", id);
        
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found with ID: " + id));
        
        return convertToResponseDTO(userRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserRoleResponseDTO getUserRole(Long userId, Long roleId) {
        logger.info("Fetching UserRole for user {} and role {}", userId, roleId);
        
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found for user " + userId + " and role " + roleId));
        
        return convertToResponseDTO(userRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserRoleResponseDTO> getAllUserRoles(Pageable pageable) {
        logger.info("Fetching all UserRoles with pagination: {}", pageable);
        
        Page<UserRole> userRoles = userRoleRepository.findAll(pageable);
        return userRoles.map(this::convertToResponseDTO);
    }
    
    @Override
    public UserRoleResponseDTO updateUserRole(Long id, UserRoleUpdateRequestDTO userRoleUpdateRequestDTO) {
        logger.info("Updating UserRole with ID: {}", id);
        
        UserRole existingUserRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found with ID: " + id));
        
        // Update fields if provided
        if (userRoleUpdateRequestDTO.getAssignedBy() != null) {
            existingUserRole.setAssignedBy(userRoleUpdateRequestDTO.getAssignedBy());
        }
        if (userRoleUpdateRequestDTO.getIsActive() != null) {
            existingUserRole.setIsActive(userRoleUpdateRequestDTO.getIsActive());
        }
        if (userRoleUpdateRequestDTO.getExpiresAt() != null) {
            existingUserRole.setExpiresAt(userRoleUpdateRequestDTO.getExpiresAt());
        }
        if (userRoleUpdateRequestDTO.getNotes() != null) {
            existingUserRole.setNotes(userRoleUpdateRequestDTO.getNotes());
        }
        
        UserRole updatedUserRole = userRoleRepository.save(existingUserRole);
        logger.info("UserRole updated successfully with ID: {}", updatedUserRole.getId());
        
        return convertToResponseDTO(updatedUserRole);
    }
    
    @Override
    public void removeUserRole(Long id) {
        logger.info("Removing UserRole with ID: {}", id);
        
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found with ID: " + id));
        
        userRoleRepository.delete(userRole);
        logger.info("UserRole removed successfully with ID: {}", id);
    }
    
    @Override
    public void removeUserRole(Long userId, Long roleId) {
        logger.info("Removing role {} from user {}", roleId, userId);
        
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found for user " + userId + " and role " + roleId));
        
        userRoleRepository.delete(userRole);
        logger.info("Role {} removed successfully from user {}", roleId, userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getUserRoles(Long userId) {
        logger.info("Fetching all roles for user: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getActiveUserRoles(Long userId) {
        logger.info("Fetching active roles for user: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        List<UserRole> userRoles = userRoleRepository.findActiveByUserId(userId, LocalDateTime.now());
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserRoleResponseDTO> getUserRolesWithPagination(Long userId, Pageable pageable) {
        logger.info("Fetching roles for user {} with pagination: {}", userId, pageable);
        
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        Page<UserRole> userRoles = userRoleRepository.findByUserIdWithPagination(userId, pageable);
        return userRoles.map(this::convertToResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getRoleUsers(Long roleId) {
        logger.info("Fetching all users for role: {}", roleId);
        
        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException("Role not found with ID: " + roleId);
        }
        
        List<UserRole> userRoles = userRoleRepository.findByRoleId(roleId);
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getActiveRoleUsers(Long roleId) {
        logger.info("Fetching active users for role: {}", roleId);
        
        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException("Role not found with ID: " + roleId);
        }
        
        List<UserRole> userRoles = userRoleRepository.findActiveByRoleId(roleId, LocalDateTime.now());
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserRoleResponseDTO> getRoleUsersWithPagination(Long roleId, Pageable pageable) {
        logger.info("Fetching users for role {} with pagination: {}", roleId, pageable);
        
        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException("Role not found with ID: " + roleId);
        }
        
        Page<UserRole> userRoles = userRoleRepository.findByRoleIdWithPagination(roleId, pageable);
        return userRoles.map(this::convertToResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserRoleResponseDTO> searchUserRoles(String keyword, Pageable pageable) {
        logger.info("Searching UserRoles with keyword: {} and pagination: {}", keyword, pageable);
        
        Page<UserRole> userRoles = userRoleRepository.searchUserRoles(keyword, pageable);
        return userRoles.map(this::convertToResponseDTO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getExpiredUserRoles() {
        logger.info("Fetching expired UserRoles");
        
        List<UserRole> userRoles = userRoleRepository.findExpiredUserRoles(LocalDateTime.now());
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getUserRolesExpiringSoon(int daysAhead) {
        logger.info("Fetching UserRoles expiring in {} days", daysAhead);
        
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiryThreshold = currentTime.plusDays(daysAhead);
        
        List<UserRole> userRoles = userRoleRepository.findUserRolesExpiringSoon(currentTime, expiryThreshold);
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getUserRolesByAssignedBy(String assignedBy) {
        logger.info("Fetching UserRoles assigned by: {}", assignedBy);
        
        List<UserRole> userRoles = userRoleRepository.findByAssignedBy(assignedBy);
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getUserRoleHistory(Long userId) {
        logger.info("Fetching role assignment history for user: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        List<UserRole> userRoles = userRoleRepository.getUserRoleHistory(userId);
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getRoleAssignmentHistory(Long roleId) {
        logger.info("Fetching assignment history for role: {}", roleId);
        
        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException("Role not found with ID: " + roleId);
        }
        
        List<UserRole> userRoles = userRoleRepository.getRoleAssignmentHistory(roleId);
        return userRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserRole(Long userId, Long roleId) {
        return userRoleRepository.existsByUserIdAndRoleId(userId, roleId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasActiveUserRole(Long userId, Long roleId) {
        return userRoleRepository.existsActiveUserRole(userId, roleId, LocalDateTime.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean userHasRole(Long userId, String roleName) {
        List<UserRole> userRoles = userRoleRepository.findActiveByUserId(userId, LocalDateTime.now());
        return userRoles.stream()
                .anyMatch(ur -> ur.getRole().getName().equalsIgnoreCase(roleName));
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveUserRoles() {
        return userRoleRepository.countActiveUserRoles(LocalDateTime.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countUsersByRole(Long roleId) {
        return userRoleRepository.countActiveUsersByRole(roleId, LocalDateTime.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countRolesByUser(Long userId) {
        return userRoleRepository.countActiveRolesByUser(userId, LocalDateTime.now());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getUserRoleStatistics() {
        logger.info("Fetching UserRole statistics");
        
        List<Object[]> results = userRoleRepository.getUserRoleStatistics();
        Map<String, Long> statistics = new HashMap<>();
        
        // Initialize with default values
        statistics.put("active", 0L);
        statistics.put("inactive", 0L);
        
        // Fill with actual counts
        for (Object[] result : results) {
            Boolean isActive = (Boolean) result[0];
            Long count = (Long) result[1];
            statistics.put(isActive ? "active" : "inactive", count);
        }
        
        statistics.put("total", statistics.get("active") + statistics.get("inactive"));
        statistics.put("expired", (long) getExpiredUserRoles().size());
        
        return statistics;
    }
    
    @Override
    public List<UserRoleResponseDTO> assignMultipleRolesToUser(Long userId, List<Long> roleIds, String assignedBy) {
        logger.info("Assigning multiple roles {} to user {}", roleIds, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> {
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new RoleNotFoundException("Role not found with ID: " + roleId));
                    
                    // Skip if user already has this role
                    if (userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
                        logger.warn("User {} already has role {}, skipping", userId, roleId);
                        return null;
                    }
                    
                    UserRole userRole = new UserRole();
                    userRole.setUser(user);
                    userRole.setRole(role);
                    userRole.setAssignedBy(assignedBy);
                    return userRole;
                })
                .filter(ur -> ur != null)
                .collect(Collectors.toList());
        
        List<UserRole> savedUserRoles = userRoleRepository.saveAll(userRoles);
        logger.info("Multiple roles assigned successfully to user {}", userId);
        
        return savedUserRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void removeMultipleRolesFromUser(Long userId, List<Long> roleIds) {
        logger.info("Removing multiple roles {} from user {}", roleIds, userId);
        
        for (Long roleId : roleIds) {
            userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                    .ifPresent(userRoleRepository::delete);
        }
        
        logger.info("Multiple roles removed successfully from user {}", userId);
    }
    
    @Override
    public void deactivateAllUserRoles(Long userId) {
        logger.info("Deactivating all roles for user: {}", userId);
        
        int updatedCount = userRoleRepository.deactivateAllUserRoles(userId);
        logger.info("Deactivated {} roles for user {}", updatedCount, userId);
    }
    
    @Override
    public void deactivateAllRoleAssignments(Long roleId) {
        logger.info("Deactivating all assignments for role: {}", roleId);
        
        int updatedCount = userRoleRepository.deactivateAllRoleAssignments(roleId);
        logger.info("Deactivated {} assignments for role {}", updatedCount, roleId);
    }
    
    @Override
    public int deactivateExpiredUserRoles() {
        logger.info("Deactivating expired UserRoles");
        
        int deactivatedCount = userRoleRepository.deactivateExpiredUserRoles(LocalDateTime.now());
        logger.info("Deactivated {} expired UserRoles", deactivatedCount);
        
        return deactivatedCount;
    }
    
    @Override
    public void extendUserRoleExpiry(Long userRoleId, LocalDateTime newExpiryDate) {
        logger.info("Extending expiry for UserRole {} to {}", userRoleId, newExpiryDate);
        
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found with ID: " + userRoleId));
        
        userRole.setExpiresAt(newExpiryDate);
        userRoleRepository.save(userRole);
        
        logger.info("Expiry extended successfully for UserRole {}", userRoleId);
    }
    
    @Override
    public void setUserRoleExpiry(Long userRoleId, LocalDateTime expiryDate) {
        logger.info("Setting expiry for UserRole {} to {}", userRoleId, expiryDate);
        
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found with ID: " + userRoleId));
        
        userRole.setExpiresAt(expiryDate);
        userRoleRepository.save(userRole);
        
        logger.info("Expiry set successfully for UserRole {}", userRoleId);
    }
    
    @Override
    public void removeUserRoleExpiry(Long userRoleId) {
        logger.info("Removing expiry for UserRole {}", userRoleId);
        
        UserRole userRole = userRoleRepository.findById(userRoleId)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found with ID: " + userRoleId));
        
        userRole.setExpiresAt(null);
        userRoleRepository.save(userRole);
        
        logger.info("Expiry removed successfully for UserRole {}", userRoleId);
    }
    
    @Override
    public UserRoleResponseDTO reactivateUserRole(Long id) {
        logger.info("Reactivating UserRole with ID: {}", id);
        
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("UserRole not found with ID: " + id));
        
        userRole.setIsActive(true);
        UserRole reactivatedUserRole = userRoleRepository.save(userRole);
        
        logger.info("UserRole reactivated successfully with ID: {}", id);
        return convertToResponseDTO(reactivatedUserRole);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> getInactiveUserRoles(Long userId) {
        logger.info("Fetching inactive roles for user: {}", userId);
        
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        List<UserRole> allUserRoles = userRoleRepository.findByUserId(userId);
        List<UserRole> inactiveUserRoles = allUserRoles.stream()
                .filter(ur -> !ur.getIsActive() || ur.isExpired())
                .collect(Collectors.toList());
        
        return inactiveUserRoles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void transferUserRoles(Long fromUserId, Long toUserId, String transferredBy) {
        logger.info("Transferring roles from user {} to user {}", fromUserId, toUserId);
        
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new UserNotFoundException("From user not found with ID: " + fromUserId));
        
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new UserNotFoundException("To user not found with ID: " + toUserId));
        
        List<UserRole> fromUserRoles = userRoleRepository.findActiveByUserId(fromUserId, LocalDateTime.now());
        
        for (UserRole userRole : fromUserRoles) {
            // Deactivate old assignment
            userRole.setIsActive(false);
            
            // Create new assignment for target user
            if (!userRoleRepository.existsByUserIdAndRoleId(toUserId, userRole.getRole().getId())) {
                UserRole newUserRole = new UserRole();
                newUserRole.setUser(toUser);
                newUserRole.setRole(userRole.getRole());
                newUserRole.setAssignedBy(transferredBy);
                newUserRole.setNotes("Transferred from user " + fromUserId);
                userRoleRepository.save(newUserRole);
            }
        }
        
        userRoleRepository.saveAll(fromUserRoles);
        logger.info("Roles transferred successfully from user {} to user {}", fromUserId, toUserId);
    }
    
    // Helper method to convert Entity to ResponseDTO
    private UserRoleResponseDTO convertToResponseDTO(UserRole userRole) {
        UserRoleResponseDTO dto = new UserRoleResponseDTO();
        dto.setId(userRole.getId());
        dto.setUserId(userRole.getUser().getId());
        dto.setUsername(userRole.getUser().getUsername());
        dto.setUserEmail(userRole.getUser().getEmail());
        dto.setRoleId(userRole.getRole().getId());
        dto.setRoleName(userRole.getRole().getName());
        dto.setRoleDescription(userRole.getRole().getDescription());
        dto.setAssignedBy(userRole.getAssignedBy());
        dto.setIsActive(userRole.getIsActive());
        dto.setAssignedAt(userRole.getAssignedAt());
        dto.setUpdatedAt(userRole.getUpdatedAt());
        dto.setExpiresAt(userRole.getExpiresAt());
        dto.setNotes(userRole.getNotes());
        dto.setExpired(userRole.isExpired());
        
        return dto;
    }
}
