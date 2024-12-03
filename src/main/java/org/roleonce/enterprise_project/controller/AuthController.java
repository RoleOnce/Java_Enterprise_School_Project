package org.roleonce.enterprise_project.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.roleonce.enterprise_project.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String test() {
        return "login";
    }

    @GetMapping("/logout-user")
    public String showLogoutPage() {
        return "logout"; // returnerar logout.html
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        authService.logout(request);
        return "redirect:/login";
    }

}
