package com.backend.hrms.service.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.hrms.dto.auth.AdminDTO;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.entity.AdminEntity;
import com.backend.hrms.entity.auth.AuthEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.auth.AuthRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
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

    public AuthEntity me(UUID id) {
        return authRepository.findById(id)
                .orElseThrow(() -> HttpException.notFound("Details not found for this user"));
    }

    public void registerAdmin(AdminDTO.RegisterDTO data, AdminEntity adminEntity) {
        
        if (authRepository.existsByEmail(data.getEmail().toLowerCase()))
            throw HttpException.badRequest("Email already exists");

        AuthEntity authEntity = new AuthEntity();
        authEntity.setEmail(data.getEmail().toLowerCase());
        authEntity.setPassword(passwordEncoder.encode(data.getPassword()));
        authEntity.setRole(data.getRole());
        authEntity.setAdmin(adminEntity);
        authRepository.save(authEntity);
    }
}
