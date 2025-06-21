package com.backend.hrms.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.auth.AuthRepository;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthEntity login(AuthDTO.LoginDTO data) {
        Optional<AuthEntity> check = authRepository.findByEmail(data.getEmail().toLowerCase());
        if (check.isPresent()) {

            boolean matches = passwordEncoder.matches(
                    data.getPassword(),
                    check.get().getPassword());

            if (!matches)
                throw HttpException.badRequest("Invalid Credentials");

            return check.get();
        } else
            throw HttpException.badRequest("Invalid Credentials");
    }
}
