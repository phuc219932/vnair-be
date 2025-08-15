package com.vnair.usermanagement.repository;

import com.vnair.usermanagement.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    // Find UserRole by user and role
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
    
    // Check if user has role
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
    
    // Check if user has active role
    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId " +
           "AND ur.isActive = true AND (ur.expiresAt IS NULL OR ur.expiresAt > :currentTime)")
    boolean existsActiveUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId, 
                                @Param("currentTime") LocalDateTime currentTime);
    
    // Find all roles for a user
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
    List<UserRole> findByUserId(@Param("userId") Long userId);
    
    // Find active roles for a user
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.isActive = true " +
           "AND (ur.expiresAt IS NULL OR ur.expiresAt > :currentTime)")
    List<UserRole> findActiveByUserId(@Param("userId") Long userId, @Param("currentTime") LocalDateTime currentTime);
    
    // Find all users for a role
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId")
    List<UserRole> findByRoleId(@Param("roleId") Long roleId);
    
    // Find active users for a role
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId AND ur.isActive = true " +
           "AND (ur.expiresAt IS NULL OR ur.expiresAt > :currentTime)")
    List<UserRole> findActiveByRoleId(@Param("roleId") Long roleId, @Param("currentTime") LocalDateTime currentTime);
    
    // Find UserRoles with pagination
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
    Page<UserRole> findByUserIdWithPagination(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId")
    Page<UserRole> findByRoleIdWithPagination(@Param("roleId") Long roleId, Pageable pageable);
    
    // Find expired roles
    @Query("SELECT ur FROM UserRole ur WHERE ur.expiresAt IS NOT NULL AND ur.expiresAt <= :currentTime")
    List<UserRole> findExpiredUserRoles(@Param("currentTime") LocalDateTime currentTime);
    
    // Find roles expiring soon
    @Query("SELECT ur FROM UserRole ur WHERE ur.expiresAt IS NOT NULL " +
           "AND ur.expiresAt > :currentTime AND ur.expiresAt <= :expiryThreshold")
    List<UserRole> findUserRolesExpiringSoon(@Param("currentTime") LocalDateTime currentTime,
                                            @Param("expiryThreshold") LocalDateTime expiryThreshold);
    
    // Find by assigned by
    @Query("SELECT ur FROM UserRole ur WHERE ur.assignedBy = :assignedBy")
    List<UserRole> findByAssignedBy(@Param("assignedBy") String assignedBy);
    
    // Search UserRoles
    @Query("SELECT ur FROM UserRole ur WHERE " +
           "(LOWER(ur.user.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ur.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(ur.role.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<UserRole> searchUserRoles(@Param("keyword") String keyword, Pageable pageable);
    
    // Count active user roles
    @Query("SELECT COUNT(ur) FROM UserRole ur WHERE ur.isActive = true " +
           "AND (ur.expiresAt IS NULL OR ur.expiresAt > :currentTime)")
    long countActiveUserRoles(@Param("currentTime") LocalDateTime currentTime);
    
    // Count user roles by role
    @Query("SELECT COUNT(ur) FROM UserRole ur WHERE ur.role.id = :roleId AND ur.isActive = true " +
           "AND (ur.expiresAt IS NULL OR ur.expiresAt > :currentTime)")
    long countActiveUsersByRole(@Param("roleId") Long roleId, @Param("currentTime") LocalDateTime currentTime);
    
    // Count roles by user
    @Query("SELECT COUNT(ur) FROM UserRole ur WHERE ur.user.id = :userId AND ur.isActive = true " +
           "AND (ur.expiresAt IS NULL OR ur.expiresAt > :currentTime)")
    long countActiveRolesByUser(@Param("userId") Long userId, @Param("currentTime") LocalDateTime currentTime);
    
    // Deactivate expired roles
    @Modifying
    @Query("UPDATE UserRole ur SET ur.isActive = false WHERE ur.expiresAt IS NOT NULL " +
           "AND ur.expiresAt <= :currentTime AND ur.isActive = true")
    int deactivateExpiredUserRoles(@Param("currentTime") LocalDateTime currentTime);
    
    // Bulk deactivate user roles
    @Modifying
    @Query("UPDATE UserRole ur SET ur.isActive = false WHERE ur.user.id = :userId")
    int deactivateAllUserRoles(@Param("userId") Long userId);
    
    // Bulk deactivate role assignments
    @Modifying
    @Query("UPDATE UserRole ur SET ur.isActive = false WHERE ur.role.id = :roleId")
    int deactivateAllRoleAssignments(@Param("roleId") Long roleId);
    
    // Get user role statistics
    @Query("SELECT ur.isActive, COUNT(ur) FROM UserRole ur GROUP BY ur.isActive")
    List<Object[]> getUserRoleStatistics();
    
    // Get role assignment history for user
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId ORDER BY ur.assignedAt DESC")
    List<UserRole> getUserRoleHistory(@Param("userId") Long userId);
    
    // Get role assignment history for role
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId ORDER BY ur.assignedAt DESC")
    List<UserRole> getRoleAssignmentHistory(@Param("roleId") Long roleId);
}
