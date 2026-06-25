package com.nanolap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }

    @GetMapping("/shop")
    public String shop() {
        return "redirect:/shop.html";
    }

    @GetMapping("/cart")
    public String cart() {
        return "redirect:/cart.html";
    }

    @GetMapping("/admin")
    public String admin() {
        return "redirect:/admin.html";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    }

    @GetMapping("/customer-dashboard")
    public String customerDashboard() {
        return "redirect:/customer-dashboard.html";
    }
}
