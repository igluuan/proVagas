package com.devluan.proVagas.infrastructure.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "pages/home";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "user/dashboard";
    }
    
    @GetMapping("/profile")
    public String profile() {
        return "user/profile";
    }
    
    @GetMapping("/user/profile")
    public String userProfile() {
        return "user/profile";
    }
    
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }
}
