package com.vnair.usermanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Web Controller for Authentication pages
 * Handles web-based login/logout for SSR (Server-Side Rendered) pages
 * Separate from AuthController which handles API endpoints
 */
@Controller
@RequestMapping("/auth")
public class AuthWebController {

    /**
     * Display login page
     * This handles the /auth/login route referenced in SecurityConfig
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
        }
        
        if (logout != null) {
            model.addAttribute("message", "Đã đăng xuất thành công!");
        }
        
        return "auth/login"; // Returns the login.html template
    }
}
