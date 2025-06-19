package com.backend.hrms.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.hrms.dto.AuthDTO;
import com.backend.hrms.entity.AuthEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.AuthRepository;
import com.backend.hrms.security.jwt.JwtService;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Map<String, String> login(AuthDTO.LoginDTO data) {
        // Check if email exists
        Optional<AuthEntity> check = authRepository.findByEmail(data.getEmail().toLowerCase());
        String newAccess;
        if (check.isPresent()) {

            System.out.println("Email found: " + check.get().getEmail());
            boolean matches = passwordEncoder.matches(
                    data.getPassword(),
                    check.get().getPassword());

            if (!matches)
                throw HttpException.badRequest("Invalid Credentials");

            newAccess = jwtService.generateAccessToken(check.get(), Map.of(
                    "id", check.get().getId(),
                    "role", check.get().getRole()));
        } else
            throw HttpException.badRequest("Invalid Credentials");

        return Map.of(
                "access", newAccess,
                "refresh", newAccess);
    }
}
