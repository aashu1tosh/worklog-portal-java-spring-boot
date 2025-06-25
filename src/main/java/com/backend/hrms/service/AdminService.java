package com.backend.hrms.service;

import org.springframework.stereotype.Service;

import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.entity.AdminEntity;
import com.backend.hrms.repository.AdminRepository;
import com.backend.hrms.service.auth.AuthService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;
    private final AuthService authService;

    public void register(AuthDTO.RegisterAdminDTO data) {

        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setFirstName(data.getFirstName());
        adminEntity.setLastName(data.getLastName());
        adminEntity.setMiddleName(data.getMiddleName());
        adminRepository.save(adminEntity);

        authService.registerAdmin(data, adminEntity);
    }
}
