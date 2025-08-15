package com.vnair.usermanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthSSRController {
    @GetMapping("/auth/login")
    public String loginPage() {
        return "auth/login";
    }

        @org.springframework.web.bind.annotation.PostMapping("/auth/login")
        public String loginSubmit(
                @org.springframework.web.bind.annotation.RequestParam String username,
                @org.springframework.web.bind.annotation.RequestParam String password,
                org.springframework.ui.Model model
        ) {
            // Xác thực user in-memory (admin/admin)
            if ("admin".equals(username) && "admin".equals(password)) {
                return "redirect:/api/web/users#settings";
            } else {
                model.addAttribute("error", true);
                return "auth/login";
            }
        }
}
