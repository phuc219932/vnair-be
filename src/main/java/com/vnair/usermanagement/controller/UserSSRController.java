package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.UserResponseDTO;
import com.vnair.usermanagement.entity.UserStatus;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
public class UserSSRController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserSSRController.class);
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/users")
    public String usersList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UserStatus status,
            Model model) {
        
        logger.info("GET /web/users - Displaying users list page");
        
        try {
            // Create sort object
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<UserResponseDTO> usersPage;
            
            // Apply filters
            if (keyword != null && !keyword.trim().isEmpty()) {
                usersPage = userService.searchUsers(keyword.trim(), pageable);
                model.addAttribute("keyword", keyword.trim());
            } else if (status != null) {
                usersPage = userService.getUsersByStatus(status, pageable);
                model.addAttribute("selectedStatus", status);
            } else {
                usersPage = userService.getAllUsers(pageable);
            }
            
            // Add pagination info
            model.addAttribute("users", usersPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", usersPage.getTotalPages());
            model.addAttribute("totalElements", usersPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("isFirst", usersPage.isFirst());
            model.addAttribute("isLast", usersPage.isLast());
            
            // Add filter options
            model.addAttribute("userStatuses", UserStatus.values());
            model.addAttribute("pageSizes", new int[]{5, 10, 20, 50});
            
            // Add user statistics
            model.addAttribute("activeCount", userService.countUsersByStatus(UserStatus.ACTIVE));
            model.addAttribute("inactiveCount", userService.countUsersByStatus(UserStatus.INACTIVE));
            model.addAttribute("suspendedCount", userService.countUsersByStatus(UserStatus.SUSPENDED));
            model.addAttribute("deletedCount", userService.countUsersByStatus(UserStatus.DELETED));
            
            return "users/list";
            
        } catch (Exception e) {
            logger.error("Error loading users list", e);
            model.addAttribute("errorMessage", "Không thể tải danh sách người dùng: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/users/dashboard")
    public String usersDashboard(Model model) {
        logger.info("GET /web/users/dashboard - Displaying users dashboard");
        
        try {
            // Get statistics
            long totalUsers = userService.countUsersByStatus(UserStatus.ACTIVE) + 
                            userService.countUsersByStatus(UserStatus.INACTIVE) + 
                            userService.countUsersByStatus(UserStatus.SUSPENDED) + 
                            userService.countUsersByStatus(UserStatus.DELETED);
            
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("activeCount", userService.countUsersByStatus(UserStatus.ACTIVE));
            model.addAttribute("inactiveCount", userService.countUsersByStatus(UserStatus.INACTIVE));
            model.addAttribute("suspendedCount", userService.countUsersByStatus(UserStatus.SUSPENDED));
            model.addAttribute("deletedCount", userService.countUsersByStatus(UserStatus.DELETED));
            
            // Get recent users (latest 5)
            Pageable recentUsersPageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
            Page<UserResponseDTO> recentUsers = userService.getAllUsers(recentUsersPageable);
            model.addAttribute("recentUsers", recentUsers.getContent());
            
            return "users/dashboard";
            
        } catch (Exception e) {
            logger.error("Error loading users dashboard", e);
            model.addAttribute("errorMessage", "Không thể tải dashboard: " + e.getMessage());
            return "error";
        }
    }
}
