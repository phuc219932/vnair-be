package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.RoleResponseDTO;
import com.vnair.usermanagement.dto.UserResponseDTO;
import com.vnair.usermanagement.dto.UserRoleResponseDTO;
import com.vnair.usermanagement.service.RoleService;
import com.vnair.usermanagement.service.UserRoleService;
import com.vnair.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/web")
public class UserRoleSSRController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRoleSSRController.class);
    
    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping("/user-roles")
    public String userRolesList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String assignedBy,
            Model model) {
        
        logger.info("GET /web/user-roles - Displaying user roles list page");
        
        try {
            // Create sort object
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<UserRoleResponseDTO> userRolesPage;
            
            // Apply filters
            if (userId != null) {
                userRolesPage = userRoleService.getUserRolesWithPagination(userId, pageable);
                model.addAttribute("selectedUserId", userId);
                
                // Get user info for display
                try {
                    UserResponseDTO user = userService.getUserById(userId);
                    model.addAttribute("selectedUser", user);
                } catch (Exception e) {
                    logger.warn("Could not load user info for ID: {}", userId);
                }
            } else if (roleId != null) {
                userRolesPage = userRoleService.getRoleUsersWithPagination(roleId, pageable);
                model.addAttribute("selectedRoleId", roleId);
                
                // Get role info for display
                try {
                    RoleResponseDTO role = roleService.getRoleById(roleId);
                    model.addAttribute("selectedRole", role);
                } catch (Exception e) {
                    logger.warn("Could not load role info for ID: {}", roleId);
                }
            } else if (isActive != null) {
                // Filter by active status - we'll need to implement this in service or filter on client side
                userRolesPage = userRoleService.getAllUserRoles(pageable);
                // Filter the results if needed
                model.addAttribute("selectedIsActive", isActive);
            } else if (assignedBy != null && !assignedBy.trim().isEmpty()) {
                List<UserRoleResponseDTO> userRolesList = userRoleService.getUserRolesByAssignedBy(assignedBy.trim());
                // Convert list to page for consistency
                int start = (int) pageable.getOffset();
                int end = Math.min((start + pageable.getPageSize()), userRolesList.size());
                List<UserRoleResponseDTO> pageContent = userRolesList.subList(start, end);
                userRolesPage = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, userRolesList.size());
                model.addAttribute("selectedAssignedBy", assignedBy.trim());
            } else {
                userRolesPage = userRoleService.getAllUserRoles(pageable);
            }
            
            // Add pagination info
            model.addAttribute("userRoles", userRolesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userRolesPage.getTotalPages());
            model.addAttribute("totalElements", userRolesPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("isFirst", userRolesPage.isFirst());
            model.addAttribute("isLast", userRolesPage.isLast());
            
            // Add filter options
            model.addAttribute("activeOptions", new Boolean[]{true, false});
            model.addAttribute("pageSizes", new int[]{5, 10, 20, 50});
            
            // Add dropdown data for filters
            Page<UserResponseDTO> allUsers = userService.getAllUsers(PageRequest.of(0, 100));
            Page<RoleResponseDTO> allRoles = roleService.getAllRoles(PageRequest.of(0, 100));
            model.addAttribute("users", allUsers.getContent());
            model.addAttribute("roles", allRoles.getContent());
            
            // Add statistics
            Map<String, Long> statistics = userRoleService.getUserRoleStatistics();
            model.addAttribute("totalAssignments", statistics.get("total"));
            model.addAttribute("activeAssignments", statistics.get("active"));
            model.addAttribute("inactiveAssignments", statistics.get("inactive"));
            
            return "user-roles/list";
            
        } catch (Exception e) {
            logger.error("Error loading user roles list", e);
            model.addAttribute("errorMessage", "Không thể tải danh sách phân quyền: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/user-roles/dashboard")
    public String userRolesDashboard(Model model) {
        logger.info("GET /web/user-roles/dashboard - Displaying user roles dashboard");
        
        try {
            // Get statistics
            Map<String, Long> statistics = userRoleService.getUserRoleStatistics();
            model.addAttribute("totalAssignments", statistics.get("total"));
            model.addAttribute("activeAssignments", statistics.get("active"));
            model.addAttribute("inactiveAssignments", statistics.get("inactive"));
            
            // Get recent assignments (latest 10)
            Pageable recentPageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            Page<UserRoleResponseDTO> recentAssignments = userRoleService.getAllUserRoles(recentPageable);
            model.addAttribute("recentAssignments", recentAssignments.getContent());
            
            // Get expiring roles (next 30 days)
            List<UserRoleResponseDTO> expiringRoles = userRoleService.getUserRolesExpiringSoon(30);
            model.addAttribute("expiringRoles", expiringRoles);
            
            // Get top assigners - using recent assignments to calculate
            Map<String, Long> assignerCounts = recentAssignments.getContent().stream()
                .filter(ur -> ur.getAssignedBy() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                    UserRoleResponseDTO::getAssignedBy,
                    java.util.stream.Collectors.counting()
                ));
            model.addAttribute("assignerCounts", assignerCounts);
            
            // Get role usage statistics from recent assignments
            Map<String, Long> roleUsage = recentAssignments.getContent().stream()
                .filter(ur -> ur.getRoleName() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                    UserRoleResponseDTO::getRoleName,
                    java.util.stream.Collectors.counting()
                ));
            model.addAttribute("roleUsage", roleUsage);
            
            return "user-roles/dashboard";
            
        } catch (Exception e) {
            logger.error("Error loading user roles dashboard", e);
            model.addAttribute("errorMessage", "Không thể tải dashboard phân quyền: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/user-roles/user/{userId}")
    public String userRolesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        logger.info("GET /web/user-roles/user/{} - Displaying roles for specific user", userId);
        
        try {
            // Get user info
            UserResponseDTO user = userService.getUserById(userId);
            model.addAttribute("user", user);
            
            // Get user roles with pagination
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<UserRoleResponseDTO> userRolesPage = userRoleService.getUserRolesWithPagination(userId, pageable);
            
            // Add pagination info
            model.addAttribute("userRoles", userRolesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", userRolesPage.getTotalPages());
            model.addAttribute("totalElements", userRolesPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("isFirst", userRolesPage.isFirst());
            model.addAttribute("isLast", userRolesPage.isLast());
            
            // Get available roles for assignment
            List<RoleResponseDTO> availableRoles = roleService.getAvailableRolesForUser(userId);
            model.addAttribute("availableRoles", availableRoles);
            
            return "user-roles/user-detail";
            
        } catch (Exception e) {
            logger.error("Error loading user roles for user {}", userId, e);
            model.addAttribute("errorMessage", "Không thể tải phân quyền của người dùng: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/user-roles/role/{roleId}")
    public String userRolesByRole(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        
        logger.info("GET /web/user-roles/role/{} - Displaying users for specific role", roleId);
        
        try {
            // Get role info
            RoleResponseDTO role = roleService.getRoleById(roleId);
            model.addAttribute("role", role);
            
            // Get role users with pagination
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<UserRoleResponseDTO> roleUsersPage = userRoleService.getRoleUsersWithPagination(roleId, pageable);
            
            // Add pagination info
            model.addAttribute("roleUsers", roleUsersPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", roleUsersPage.getTotalPages());
            model.addAttribute("totalElements", roleUsersPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("isFirst", roleUsersPage.isFirst());
            model.addAttribute("isLast", roleUsersPage.isLast());
            
            // Get all users for potential assignment
            Page<UserResponseDTO> allUsers = userService.getAllUsers(PageRequest.of(0, 100));
            model.addAttribute("allUsers", allUsers.getContent());
            
            return "user-roles/role-detail";
            
        } catch (Exception e) {
            logger.error("Error loading role users for role {}", roleId, e);
            model.addAttribute("errorMessage", "Không thể tải người dùng của vai trò: " + e.getMessage());
            return "error";
        }
    }
}
