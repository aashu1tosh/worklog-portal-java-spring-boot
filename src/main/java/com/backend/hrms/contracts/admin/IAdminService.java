package com.backend.hrms.contracts.admin;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.hrms.dto.auth.AdminDTO;
import com.backend.hrms.dto.auth.AuthDTO;
import com.backend.hrms.entity.AdminEntity;

public interface IAdminService {

    void register(AdminDTO.RegisterDTO data);

    Page<AdminEntity> get(Pageable pageable, String search);

    void update(AuthDTO.ProfileUpdateDTO data, UUID id);
}
