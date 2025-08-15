package com.vnair.usermanagement.controller;

import com.vnair.usermanagement.dto.RoleResponseDTO;
import com.vnair.usermanagement.service.RoleService;
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
public class RoleSSRController {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleSSRController.class);
    
    @Autowired
    private RoleService roleService;
    
    @GetMapping("/roles")
    public String rolesList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isActive,
            Model model) {
        
        logger.info("GET /web/roles - Displaying roles list page");
        
        try {
            // Create sort object
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<RoleResponseDTO> rolesPage;
            
            // Apply filters
            if (keyword != null && !keyword.trim().isEmpty()) {
                rolesPage = roleService.searchRoles(keyword.trim(), pageable);
                model.addAttribute("keyword", keyword.trim());
            } else if (isActive != null) {
                rolesPage = roleService.getRolesByIsActive(isActive, pageable);
                model.addAttribute("selectedIsActive", isActive);
            } else {
                rolesPage = roleService.getAllRoles(pageable);
            }
            
            // Add pagination info
            model.addAttribute("roles", rolesPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", rolesPage.getTotalPages());
            model.addAttribute("totalElements", rolesPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("isFirst", rolesPage.isFirst());
            model.addAttribute("isLast", rolesPage.isLast());
            
            // Add filter options
            model.addAttribute("activeOptions", new Boolean[]{true, false});
            model.addAttribute("pageSizes", new int[]{5, 10, 20, 50});
            
            // Add role statistics
            model.addAttribute("activeCount", roleService.countRolesByIsActive(true));
            model.addAttribute("inactiveCount", roleService.countRolesByIsActive(false));
            
            return "roles/list";
            
        } catch (Exception e) {
            logger.error("Error loading roles list", e);
            model.addAttribute("errorMessage", "Không thể tải danh sách vai trò: " + e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/roles/dashboard")
    public String rolesDashboard(Model model) {
        logger.info("GET /web/roles/dashboard - Displaying roles dashboard");
        
        try {
            // Get statistics
            long activeRoles = roleService.countRolesByIsActive(true);
            long inactiveRoles = roleService.countRolesByIsActive(false);
            long totalRoles = activeRoles + inactiveRoles;
            
            model.addAttribute("totalRoles", totalRoles);
            model.addAttribute("activeCount", activeRoles);
            model.addAttribute("inactiveCount", inactiveRoles);
            
            // Get recent roles (latest 5)
            Pageable recentRolesPageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
            Page<RoleResponseDTO> recentRoles = roleService.getAllRoles(recentRolesPageable);
            model.addAttribute("recentRoles", recentRoles.getContent());
            
            // Get active roles list for quick access
            model.addAttribute("activeRoles", roleService.getActiveRoles());
            
            return "roles/dashboard";
            
        } catch (Exception e) {
            logger.error("Error loading roles dashboard", e);
            model.addAttribute("errorMessage", "Không thể tải dashboard vai trò: " + e.getMessage());
            return "error";
        }
    }
}
