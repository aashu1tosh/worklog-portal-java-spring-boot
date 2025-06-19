package com.backend.hrms.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/public/login")
    public String login() {
        System.out.println("Login endpoint hit");
        return "Login endpoint is not implemented yet.";
    }
}
