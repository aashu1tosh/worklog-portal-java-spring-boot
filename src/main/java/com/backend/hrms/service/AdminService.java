package com.backend.hrms.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.hrms.contracts.admin.IAdminService;
import com.backend.hrms.contracts.auth.IAuthService;
import com.backend.hrms.dto.auth.AdminDTO;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.entity.AdminEntity;
import com.backend.hrms.exception.HttpException;
import com.backend.hrms.repository.AdminRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AdminService implements IAdminService {

    private final AdminRepository adminRepository;
    private final IAuthService authService;

    public void register(AdminDTO.RegisterDTO data) {

        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setFirstName(data.getFirstName());
        adminEntity.setLastName(data.getLastName());
        adminEntity.setMiddleName(data.getMiddleName());
        adminRepository.save(adminEntity);

        authService.registerAdmin(data, adminEntity);
    }

    public Page<AdminEntity> get(Pageable pageable, String search) {
        if (search == null || search.trim().isEmpty()) {
            return adminRepository.findAll(pageable);
        } else {
            return adminRepository.findByFirstNameContainingIgnoreCase(search, pageable);
        }
    }

    public void update(AuthDTO.ProfileUpdateDTO data, UUID id) {
        var auth = authService.checkById(id);

        if (auth.getAdmin().getId() == null) {
            throw HttpException.notFound("Admin not found for the given ID.");
        }

        var adminEntity = auth.getAdmin();
        adminEntity.setFirstName(data.getFirstName());
        adminEntity.setLastName(data.getLastName());
        adminEntity.setMiddleName(data.getMiddleName());

        adminRepository.save(adminEntity);
    }
}
