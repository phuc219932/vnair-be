package com.vnair.usermanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/web/users/dashboard";
    }
    
    @GetMapping("/web")
    public String webHome() {
        return "redirect:/web/users/dashboard";
    }
}
