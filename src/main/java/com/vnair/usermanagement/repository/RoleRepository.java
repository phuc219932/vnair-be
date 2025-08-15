package com.vnair.usermanagement.repository;

import com.vnair.usermanagement.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // Find role by name
    Optional<Role> findByName(String name);
    
    // Find role by name (case insensitive)
    Optional<Role> findByNameIgnoreCase(String name);
    
    // Check if role exists by name
    boolean existsByName(String name);
    
    // Check if role exists by name (case insensitive)
    boolean existsByNameIgnoreCase(String name);
    
    // Find roles by active status
    List<Role> findByIsActive(Boolean isActive);
    
    // Find roles by active status with pagination
    Page<Role> findByIsActive(Boolean isActive, Pageable pageable);
    
    // Search roles by name containing keyword
    @Query("SELECT r FROM Role r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Role> findByNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);
    
    // Search roles by name or description containing keyword
    @Query("SELECT r FROM Role r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Role> searchRoles(@Param("keyword") String keyword, Pageable pageable);
    
    // Count roles by active status
    long countByIsActive(Boolean isActive);
    
    // Find roles assigned to a specific user
    @Query("SELECT r FROM Role r JOIN r.userRoles ur WHERE ur.user.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
    
    // Find roles not assigned to a specific user
    @Query("SELECT r FROM Role r WHERE r.id NOT IN " +
           "(SELECT ur.role.id FROM UserRole ur WHERE ur.user.id = :userId)")
    List<Role> findRolesNotAssignedToUser(@Param("userId") Long userId);
    
    // Find active roles
    @Query("SELECT r FROM Role r WHERE r.isActive = true ORDER BY r.name")
    List<Role> findActiveRoles();
    
    // Get role statistics
    @Query("SELECT r.isActive, COUNT(r) FROM Role r GROUP BY r.isActive")
    List<Object[]> getRoleStatistics();
}
