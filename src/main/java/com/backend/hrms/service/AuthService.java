package com.backend.hrms.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.hrms.dto.AuthDTO;
import com.backend.hrms.entity.AuthEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.AuthRepository;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Void login(AuthDTO.LoginDTO data) {
        // Check if email exists
        Optional<AuthEntity> check = authRepository.findByEmail(data.getEmail().toLowerCase());

        if (check.isPresent()) {

            System.out.println("Email found: " + check.get().getEmail());
            boolean matches = passwordEncoder.matches(
                    data.getPassword(), 
                    check.get().getPassword()
            );

            if(!matches) 
                throw HttpException.badRequest("Invalid Credentials");    
            
            System.out.println("Password matches for email: " + check.get().getEmail());
        } else {
            throw HttpException.badRequest("Invalid Credentials");
        }
        return null;
    }
}
